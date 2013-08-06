package net.sareweb.android.txotx.activity;

import net.sareweb.android.txotx.R;
import net.sareweb.android.txotx.fragment.SagardoEgunakFragment;
import net.sareweb.android.txotx.util.Constants;
import net.sareweb.android.txotx.util.PrefUtils;
import net.sareweb.android.txotx.util.TxotxPrefs_;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.OnQueryTextListener;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.FragmentById;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.OptionsMenu;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

@Deprecated
@EActivity(R.layout.sagardoegunak_activity)
@OptionsMenu(R.menu.sagardoegunak_menu)
public class SagardoEgunakActivity extends SherlockFragmentActivity implements
		OnNavigationListener, OnQueryTextListener{

	private static String TAG = "SagardoEgunakActivity";

	//@FragmentById
	SagardoEgunakFragment sagardoEgunakFragment;
	@Pref
	TxotxPrefs_ prefs;
	ActionBar actionBar;
	private SearchView mSearchView;

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
		loadSagardoEgunak(false);
	}


	private void loadSagardoEgunak(boolean refresh) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		sagardoEgunakFragment.setSagardoEgunakContent(refresh, "");
		fragmentTransaction.commitAllowingStateLoss();
	}


	@OptionsItem({R.id.menu_home,android.R.id.home})
	void homeSelected() {
		finish();
	}
	
	@OptionsItem(R.id.menu_reload)
	void reloadSelected(){
		loadSagardoEgunak(true);
	}

	@OptionsItem(R.id.menu_about)
	void aboutSelected() {
		AboutActivity_.intent(this).start();
	}

	@OptionsItem(R.id.menu_log_out)
	void logOutSelected() {
		PrefUtils.clearUserPrefs(prefs);
		finish();
		unregisterDevice();
		LogInActivity_.intent(this).start();
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        mSearchView = (SearchView) searchItem.getActionView();
        mSearchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onNavigationItemSelected(int arg0, long arg1) {
		// TODO Auto-generated method stub
		return false;
	}
	
	private void unregisterDevice() {
		Intent registrationIntent = new Intent("com.google.android.c2dm.intent.UNREGISTER");
		// sets the app name in the intent
		registrationIntent.putExtra("app", PendingIntent.getBroadcast(this, 0, new Intent(), 0));
		registrationIntent.putExtra("sender", Constants.SENDER_ID);
		startService(registrationIntent);
	}
	
	@Override
	public boolean onQueryTextChange(String text) {
		sagardoEgunakFragment.setSagardoEgunakContent(false, text);
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String text) {
		Log.d(TAG, "searching submit: " + text);
		return false;
	}

}