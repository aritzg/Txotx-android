package net.sareweb.android.txotx.listener.tab;

import net.sareweb.android.txotx.fragment.GertaerakFragment_;
import net.sareweb.android.txotx.fragment.SagardotegiDetailFragment_;
import net.sareweb.android.txotx.model.Sagardotegi;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;

public class SagardoEgunDetailTabListener implements TabListener {

	int selectedTab=SAGARDOTEGI_DETAIL;
	private Fragment mFragment;
	public Activity mActivity;
	private Sagardotegi sagardotegi;

	public SagardoEgunDetailTabListener( Sagardotegi sagardotegi, int selectedTab, Activity activity){
		this.selectedTab=selectedTab;
		mActivity = activity;
		this.sagardotegi=sagardotegi;
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction fragmentTransaction) {

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction fragmentTransaction) {
		switch (selectedTab) {
		case SAGARDOTEGI_DETAIL:
			if (mFragment == null) {
				Bundle bundle= new Bundle();
				bundle.putSerializable("sagardotegi", sagardotegi);
				mFragment = (SagardotegiDetailFragment_)Fragment.instantiate(mActivity, SagardotegiDetailFragment_.class.getName(),bundle);
				fragmentTransaction.add(android.R.id.content, mFragment);
			}
			else{
				fragmentTransaction.attach(mFragment);
			}
			break;
		case SAGARDOTEGI_GERTAERAK:
			if (mFragment == null) {
				Bundle bundle= new Bundle();
				bundle.putSerializable("sagardotegi", sagardotegi);
				mFragment = (GertaerakFragment_)Fragment.instantiate(mActivity, GertaerakFragment_.class.getName(),bundle);
				fragmentTransaction.add(android.R.id.content, mFragment);
			}
			else{
				fragmentTransaction.attach(mFragment);
			}
			break;
		}
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction fragmentTransaction) {
		if (mFragment != null) {
			fragmentTransaction.detach(mFragment);
        }

	}

	public static final int SAGARDOTEGI_DETAIL=0;
	public static final int SAGARDOTEGI_GERTAERAK=1;

}