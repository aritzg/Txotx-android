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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG,"onCreate");
		
		if(sagardoEgunId!=0){
			//close notification
			NotificationManager mNotificationManager =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			mNotificationManager.cancel((int)sagardoEgunId);
			
			dialog = ProgressDialog.show(this, "", "SagardoEguna eskuratzen...", true);
			dialog.show();
			getSagardoEgunak(true);
		}else{
			marraztuPantaila();
		}

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
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(sagardoEgun.getIzena());

		Tab sagardoEgunDetailTab = actionBar.newTab();
		sagardoEgunDetailTab.setText("SagardoEguna");
		sagardoEgunDetailTab.setTabListener(new SagardoEgunDetailTabListener(sagardoEgun, SagardoEgunDetailTabListener.SAGARDOEGUN_DETAIL, this));
		actionBar.addTab(sagardoEgunDetailTab);

		Tab sagardoEgunEventsTab = actionBar.newTab();
		sagardoEgunEventsTab.setText("Gertaerak");
		sagardoEgunEventsTab.setTabListener(new SagardoEgunDetailTabListener(sagardoEgun, SagardoEgunDetailTabListener.SAGARDOEGUN_GERTAERAK, this));
		actionBar.addTab(sagardoEgunEventsTab);

		if(sagardoEgunId!=0){
			actionBar.setSelectedNavigationItem(1);
		}
		else{
			actionBar.setSelectedNavigationItem(0);	
		}
	}

	@OptionsItem
	void homeSelected() {
		finish();
		if(sagardoEgunId!=0){
			SagardoEgunakActivity_.intent(this).start();
		}
	}


}
