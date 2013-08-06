package net.sareweb.android.txotx.activity;

import java.util.List;

import net.sareweb.android.txotx.R;
import net.sareweb.android.txotx.adapter.SagardoEgunAdapter;
import net.sareweb.android.txotx.cache.SagardoEgunCache;
import net.sareweb.android.txotx.fragment.SagardoEgunDetailFragment;
import net.sareweb.android.txotx.listener.tab.SagardoEgunDetailTabListener;
import net.sareweb.android.txotx.model.SagardoEgun;
import net.sareweb.android.txotx.util.TxotxPrefs_;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.FragmentById;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.OptionsMenu;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

@EActivity(R.layout.sagardoegun_detail_activity)
public class SagardoEgunDetailActivity extends SherlockFragmentActivity{

	private static String TAG = "SagardoEgunDetailActivity";
	@Pref TxotxPrefs_ prefs;
	@Extra SagardoEgun sagardoEgun;
	@Extra long sagardoEgunId; //used whe comming from notification
	private ProgressDialog dialog;
	@FragmentById(R.id.sagardo_egun_detail_fragment)
	SagardoEgunDetailFragment sagardoEgunDetailFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG,"onCreate");
		
		if(sagardoEgunId!=0){
			//close notification
			NotificationManager mNotificationManager =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			mNotificationManager.cancel((int)sagardoEgunId);
			
			dialog = ProgressDialog.show(this, "", "Sagardo Eguna eskuratzen...", true);
			dialog.show();
			getSagardoEgunak(true);
		}/*else{
			
		}*/

	}
	
	@Override
	protected void onResume() {
		super.onResume();
		marraztuPantaila();
	}
	
	@Background
	public void getSagardoEgunak(boolean refresh){
		Log.d(TAG, "Gettings sagardoEgunak");
		SagardoEgunCache.init(this);
		getSagardoEgunakResult(SagardoEgunCache.getSagardoEgunak(refresh));
	}

	@UiThread
	public void getSagardoEgunakResult(List<SagardoEgun> sagardoEgunak){
		for(SagardoEgun sagardoEgunTmp : sagardoEgunak){
			if(sagardoEgunTmp.getSagardoEgunId()==sagardoEgunId){
				sagardoEgun=sagardoEgunTmp;
				break;
			}
		}
		marraztuPantaila();
		dialog.cancel();
	}
	
	private void  marraztuPantaila(){
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(sagardoEgun.getIzena());

		sagardoEgunDetailFragment.setSagardoEgunContent(sagardoEgun);
	}

	@OptionsItem
	void homeSelected() {
		finish();
		if(sagardoEgunId!=0){
			//SagardoEgunakActivity_.intent(this).start();
			TxotxActivity_.intent(this).fragmentToBeLoaded(TxotxActivity.SAGARDO_EGUNAK_FRAGMENT).start();
		}
	}


}
