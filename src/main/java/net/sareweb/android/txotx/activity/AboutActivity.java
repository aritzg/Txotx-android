package net.sareweb.android.txotx.activity;

import net.sareweb.android.txotx.R;
import net.sareweb.android.txotx.cache.SagardotegiCache;
import net.sareweb.android.txotx.cache.UserCache;
import net.sareweb.android.txotx.rest.TxotxConnectionData;
import net.sareweb.android.txotx.util.TxotxPrefs_;
import net.sareweb.android.txotx.util.ConnectionUtils;
import net.sareweb.android.txotx.util.PrefUtils;
import net.sareweb.android.txotx.util.UserUtils;
import net.sareweb.lifedroid.model.User;
import net.sareweb.lifedroid.rest.UserRESTClient;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

@EActivity(R.layout.about)
public class AboutActivity extends SherlockFragmentActivity{
	
	private static String TAG = "AboutActivity";
	ActionBar actionBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayHomeAsUpEnabled(true);
	}
	
	@OptionsItem(android.R.id.home)
	void homeSelected() {
		finish();
	}
	
}