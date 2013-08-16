package net.sareweb.android.txotx.activity;

import net.sareweb.android.txotx.R;
import net.sareweb.android.txotx.cache.GertaeraCache;
import net.sareweb.android.txotx.cache.JarraipenCache;
import net.sareweb.android.txotx.cache.SagardoEgunCache;
import net.sareweb.android.txotx.cache.SagardotegiCache;
import net.sareweb.android.txotx.cache.UserCache;
import net.sareweb.android.txotx.fragment.dialog.VersionDialogFragment;
import net.sareweb.android.txotx.model.APKVersion;
import net.sareweb.android.txotx.rest.APKVersionRESTClient;
import net.sareweb.android.txotx.rest.TxotxConnectionData;
import net.sareweb.android.txotx.util.AccountUtil;
import net.sareweb.android.txotx.util.ConnectionUtils;
import net.sareweb.android.txotx.util.Constants;
import net.sareweb.android.txotx.util.TxotxPrefs_;
import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

@EActivity(R.layout.log_in)
public class LoadingActivity extends SherlockFragmentActivity implements VersionDialogFragment.VersionDialogListener{
	
	private static String TAG = "LoadingActivity";
	APKVersion version;
	@Pref TxotxPrefs_ prefs;
	DialogFragment dialog;
	boolean mandatory;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(!ConnectionUtils.isOnline(this)){
			finish();
			NoConnActivity_.intent(this).start();
			return;
		}
		initAccountToken();
	}
	
	
	
	@Background
	void initAccountToken(){
		AccountUtil.getGoogleAuthToken(this);
		gotAccoutToken();
	}
	
	@UiThread
	void gotAccoutToken(){
		registerDevice();
		loadData();
	}
	
	@Background
	void loadData(){
		UserCache.init(this);
		SagardotegiCache.init(this);
		SagardoEgunCache.init(this);
		GertaeraCache.init(this);
		JarraipenCache.init(this);
		
		JarraipenCache.eskuratuJarraipenak(AccountUtil.getGoogleEmail(this));
		SagardotegiCache.getSagardotegiak(true);
		SagardoEgunCache.getSagardoEgunak(true);
		APKVersionRESTClient apkVersionRESTClient = new APKVersionRESTClient(new TxotxConnectionData(this));
		version = apkVersionRESTClient.getLastAPKVersion();
		checkVersion();
	}
	
	@UiThread
	void checkVersion(){
		int currentVersionCode;
		try {
			currentVersionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			//Error obteniendo num version. Se intentara continuar
			finish();
			TxotxActivity_.intent(this).start();
			return;
		}
		
		if(version!=null && currentVersionCode<version.getSupportedMinVersion().intValue()){
			Log.d(TAG, "Mandatory upgrade");
			mandatory=true;
			dialog = new VersionDialogFragment(mandatory);
		    dialog.show(getSupportFragmentManager(), "VersionDialogFragment");
		}
		else if(version!=null && prefs.rejectedVersion().getOr(0)<version.getCurrentVersion().intValue() && currentVersionCode<version.getCurrentVersion().intValue()){
			Log.d(TAG, "Sugest upgrade");
			mandatory=false;
			dialog = new VersionDialogFragment(mandatory);
		    dialog.show(getSupportFragmentManager(), "VersionDialogFragment");
		}
		else{
			finish();
			TxotxActivity_.intent(this).start();
		}
		
	}
	
		
	private void registerDevice() {
		Intent registrationIntent = new Intent("com.google.android.c2dm.intent.REGISTER");
		// sets the app name in the intent
		registrationIntent.putExtra("app", PendingIntent.getBroadcast(this, 0, new Intent(), 0));
		registrationIntent.putExtra("sender", Constants.SENDER_ID);
		startService(registrationIntent);
	}



	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		Log.d(TAG, "Instalazioa hasiko da.");
		dialog.dismiss();
		finish();
		Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=net.sareweb.android.txotx"));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
	}



	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		Log.d(TAG, "Try to continue anyway");
		dialog.dismiss();
		finish();
		if(!mandatory){
			if(version!=null){
				prefs.rejectedVersion().put(version.getCurrentVersion());
			}
			TxotxActivity_.intent(this).start();
		}
	}
	


}