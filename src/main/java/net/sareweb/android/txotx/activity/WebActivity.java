package net.sareweb.android.txotx.activity;

import net.sareweb.android.txotx.R;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.rest.Accept;

@EActivity(R.layout.web)
public class WebActivity extends SherlockActivity{

	ActionBar actionBar;
	@ViewById
	WebView webview;
	@Extra
	String webUrl;
	
	private static String TAG = "WebActivity";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");

		actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayHomeAsUpEnabled(true);
	}
	
	@AfterViews
	public void afterViews(){
		webview.loadUrl("http://" + webUrl);
	}
	
	@OptionsItem(android.R.id.home)
	void homeSelected() {
		finish();
	}

}
