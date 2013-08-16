package net.sareweb.android.txotx.activity;

import net.sareweb.android.txotx.R;
import android.os.Bundle;
import android.util.Log;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.OptionsMenu;

@EActivity(R.layout.no_connection)
@OptionsMenu(R.menu.no_conn_menu)
public class NoConnActivity extends SherlockFragmentActivity {

	private static String TAG = "NoConnActivity";

	ActionBar actionBar;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");

		actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayHomeAsUpEnabled(true);

	}

	@OptionsItem(android.R.id.home)
	void homeSelected() {
		finish();
	}
	
	@OptionsItem(R.id.menu_reload)
	void reloadSelected(){
		finish();
		LoadingActivity_.intent(this).start();
	}
	
}
