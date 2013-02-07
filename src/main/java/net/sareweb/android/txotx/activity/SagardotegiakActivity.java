package net.sareweb.android.txotx.activity;

import net.sareweb.android.txotx.R;
import net.sareweb.android.txotx.fragment.SagardotegiakFragment;
import net.sareweb.android.txotx.util.PrefUtils;
import net.sareweb.android.txotx.util.TxotxPrefs_;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.FragmentById;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.OptionsMenu;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;


@EActivity(R.layout.sagardotegiak_activity)
@OptionsMenu(R.menu.sagardotegiak_menu)
public class SagardotegiakActivity extends SherlockFragmentActivity implements
		OnNavigationListener {

	private static String TAG = "SagardotegiakActivity";

	@FragmentById
	SagardotegiakFragment sagardotegiakFragment;
	@Pref
	TxotxPrefs_ prefs;
	ActionBar actionBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.d(TAG, "onCreate");

		actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayHomeAsUpEnabled(true);

	}

	@Override
	protected void onResume() {
		super.onResume();
		loadSagardotegiak(false);
	}


	private void loadSagardotegiak(boolean refresh) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		sagardotegiakFragment.setSagardotegiakContent(refresh);
		fragmentTransaction.commitAllowingStateLoss();
	}


	@OptionsItem({R.id.menu_home,android.R.id.home})
	void homeSelected() {
		finish();
	}
	
	@OptionsItem(R.id.menu_reload)
	void reloadSelected(){
		loadSagardotegiak(true);
	}

	@OptionsItem(R.id.menu_about)
	void aboutSelected() {
		AboutActivity_.intent(this).start();
	}

	@OptionsItem(R.id.menu_log_out)
	void logOutSelected() {
		PrefUtils.clearUserPrefs(prefs);
		finish();
		LogInActivity_.intent(this).start();
	}

	@Override
	public boolean onNavigationItemSelected(int arg0, long arg1) {
		// TODO Auto-generated method stub
		return false;
	}

}