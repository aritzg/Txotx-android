package net.sareweb.android.txotx.listener.tab;

import net.sareweb.android.txotx.fragment.GertaerakFragment_;
import net.sareweb.android.txotx.fragment.SagardoEgunDetailFragment_;
import net.sareweb.android.txotx.fragment.SagardotegiDetailFragment_;
import net.sareweb.android.txotx.model.SagardoEgun;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;

public class SagardoEgunDetailTabListener implements TabListener {

	int selectedTab=SAGARDOEGUN_DETAIL;
	private Fragment mFragment;
	public Activity mActivity;
	private SagardoEgun sagardoEgun;

	public SagardoEgunDetailTabListener( SagardoEgun sagardoEgun, int selectedTab, Activity activity){
		this.selectedTab=selectedTab;
		mActivity = activity;
		this.sagardoEgun=sagardoEgun;
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction fragmentTransaction) {

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction fragmentTransaction) {
		switch (selectedTab) {
		case SAGARDOEGUN_DETAIL:
			if (mFragment == null) {
				Bundle bundle= new Bundle();
				bundle.putSerializable("sagardoEgun", sagardoEgun);
				mFragment = (SagardoEgunDetailFragment_)Fragment.instantiate(mActivity, SagardoEgunDetailFragment_.class.getName(),bundle);
				fragmentTransaction.add(android.R.id.content, mFragment);
			}
			else{
				fragmentTransaction.attach(mFragment);
			}
			break;
		case SAGARDOEGUN_GERTAERAK:
			if (mFragment == null) {
				Bundle bundle= new Bundle();
				bundle.putSerializable("sagardoEgun", sagardoEgun);
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

	public static final int SAGARDOEGUN_DETAIL=0;
	public static final int SAGARDOEGUN_GERTAERAK=1;

}