package net.sareweb.android.txotx.activity;

import net.sareweb.android.txotx.R;
import net.sareweb.android.txotx.fragment.SagardotegiDetailFragment;
import net.sareweb.android.txotx.listener.tab.SagardotegiDetailTabListener;
import net.sareweb.android.txotx.model.Sagardotegi;
import android.os.Bundle;

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

	@Extra
	Sagardotegi sagardotegi;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

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

		//actionBar.setSelectedNavigationItem(0);
	}

	@Override
	protected void onResume() {
		super.onResume();
		//gardenDetailFragment.setGardenContent(garden);
	}

	@OptionsItem
	void homeSelected() {
		finish();
	}


}
