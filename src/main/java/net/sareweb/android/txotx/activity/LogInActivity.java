package net.sareweb.android.txotx.activity;

import net.sareweb.android.txotx.R;
import net.sareweb.android.txotx.rest.SagardotegiRESTClient;
import net.sareweb.android.txotx.util.AccountUtil;
import net.sareweb.android.txotx.util.ConnectionUtils;
import net.sareweb.android.txotx.util.Constants;
import net.sareweb.android.txotx.util.TxotxPrefs_;
import net.sareweb.lifedroid.rest.UserRESTClient;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

@Deprecated
@EActivity(R.layout.log_in)
public class LogInActivity extends Activity{
	
	private static String TAG = "LogInActivity";
	@ViewById TextView txEmailAddress;
	@Pref TxotxPrefs_ prefs;
	UserRESTClient userRESTClient;
	SagardotegiRESTClient sagardotegiRESTClient;
	
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
		finish();
		TxotxActivity_.intent(this).start();
	}
	
		
	private void registerDevice() {
		Intent registrationIntent = new Intent("com.google.android.c2dm.intent.REGISTER");
		// sets the app name in the intent
		registrationIntent.putExtra("app", PendingIntent.getBroadcast(this, 0, new Intent(), 0));
		registrationIntent.putExtra("sender", Constants.SENDER_ID);
		startService(registrationIntent);
	}
	


}