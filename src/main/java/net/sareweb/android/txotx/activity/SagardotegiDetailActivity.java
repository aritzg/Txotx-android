package net.sareweb.android.txotx.activity;

import java.util.List;

import net.sareweb.android.txotx.R;
import net.sareweb.android.txotx.adapter.SagardotegiAdapter;
import net.sareweb.android.txotx.cache.SagardotegiCache;
import net.sareweb.android.txotx.fragment.SagardotegiDetailFragment;
import net.sareweb.android.txotx.listener.tab.SagardotegiDetailTabListener;
import net.sareweb.android.txotx.model.Sagardotegi;
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

@EActivity(R.layout.sagardotegi_detail_activity)
public class SagardotegiDetailActivity extends SherlockFragmentActivity{

	private static String TAG = "SagardotegiDetailActivity";
	@Pref TxotxPrefs_ prefs;
	@Extra Sagardotegi sagardotegi;
	@Extra long sagardotegiId; //used whe comming from notification
	private ProgressDialog dialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG,"onCreate");
		
		if(sagardotegiId!=0){
			//close notification
			NotificationManager mNotificationManager =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			mNotificationManager.cancel((int)sagardotegiId);
			
			dialog = ProgressDialog.show(this, "", "Sagardotegia eskuratzen...", true);
			dialog.show();
			getSagardotegiak(true);
		}else{
			marraztuPantaila();
		}

	}
	
	@Background
	public void getSagardotegiak(boolean refresh){
		Log.d(TAG, "Gettings sagardotegiak");
		SagardotegiCache.init(prefs);
		getSagardotegiakResult(SagardotegiCache.getSagardotegiak(refresh));
	}

	@UiThread
	public void getSagardotegiakResult(List<Sagardotegi> sagardotegiak){
		for(Sagardotegi sagardotegiTmp : sagardotegiak){
			if(sagardotegiTmp.getSagardotegiId()==sagardotegiId){
				sagardotegi=sagardotegiTmp;
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
		actionBar.setTitle(sagardotegi.getIzena());

		Tab sagardotegiDetailTab = actionBar.newTab();
		sagardotegiDetailTab.setText("Sagardotegia");
		sagardotegiDetailTab.setTabListener(new SagardotegiDetailTabListener(sagardotegi, SagardotegiDetailTabListener.SAGARDOTEGI_DETAIL, this));
		actionBar.addTab(sagardotegiDetailTab);

		Tab sagardotegiEventsTab = actionBar.newTab();
		sagardotegiEventsTab.setText("Gertaerak");
		sagardotegiEventsTab.setTabListener(new SagardotegiDetailTabListener(sagardotegi, SagardotegiDetailTabListener.SAGARDOTEGI_GERTAERAK, this));
		actionBar.addTab(sagardotegiEventsTab);

		if(sagardotegiId!=0){
			actionBar.setSelectedNavigationItem(1);
		}
		else{
			actionBar.setSelectedNavigationItem(0);	
		}
	}

	@OptionsItem
	void homeSelected() {
		finish();
		if(sagardotegiId!=0){
			SagardotegiakActivity_.intent(this).start();
		}
	}


}
