package net.sareweb.android.txotx.activity;

import net.sareweb.android.txotx.R;
import net.sareweb.android.txotx.fragment.SagardotegiDetailFragment;
import net.sareweb.android.txotx.listener.tab.SagardotegiDetailTabListener;
import net.sareweb.android.txotx.model.Sagardotegi;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.FragmentById;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.OptionsMenu;

@EActivity(R.layout.sagardotegi_detail_activity)
public class SagardotegiDetailActivity extends SherlockFragmentActivity{

	private static String TAG = "SagardotegiDetailActivity";
	@Extra Sagardotegi sagardotegi;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG,"onCreate");
		
		/*if(savedInstanceState!=null){
			//Workarond: Pantaila giratu da eta fragment kabiatuek erroreak ematen dituzte.
			finish();
			return;
		}*/
		
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(sagardotegi.getIzena());

		Tab gardenDetailTab = actionBar.newTab();
		gardenDetailTab.setText("Sagardotegia");
		gardenDetailTab.setTabListener(new SagardotegiDetailTabListener(sagardotegi, SagardotegiDetailTabListener.SAGARDOTEGI_DETAIL, this));
		actionBar.addTab(gardenDetailTab);

		Tab gardenEventsTab = actionBar.newTab();
		gardenEventsTab.setText("Gertaerak");
		gardenEventsTab.setTabListener(new SagardotegiDetailTabListener(sagardotegi, SagardotegiDetailTabListener.SAGARDOTEGI_GERTAERAK, this));
		actionBar.addTab(gardenEventsTab);

		actionBar.setSelectedNavigationItem(0);
	}

	@Override
	protected void onResume() {
		Log.d(TAG,"onResume");
		super.onResume();
		//gardenDetailFragment.setGardenContent(garden);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG,"onPause");
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		Log.d(TAG,"onStop");
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG,"onDestry");
	}

	@OptionsItem
	void homeSelected() {
		finish();
	}


}
