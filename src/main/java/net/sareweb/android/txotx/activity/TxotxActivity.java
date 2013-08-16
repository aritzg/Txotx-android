package net.sareweb.android.txotx.activity;

import net.sareweb.android.txotx.R;
import net.sareweb.android.txotx.cache.GertaeraCache;
import net.sareweb.android.txotx.cache.JarraipenCache;
import net.sareweb.android.txotx.cache.SagardoEgunCache;
import net.sareweb.android.txotx.cache.SagardotegiCache;
import net.sareweb.android.txotx.cache.UserCache;
import net.sareweb.android.txotx.custom.DashboardButton;
import net.sareweb.android.txotx.drawerToggle.DrawerToggle;
import net.sareweb.android.txotx.fragment.AboutFragment_;
import net.sareweb.android.txotx.fragment.OharraFragment_;
import net.sareweb.android.txotx.fragment.SagardoEgunakFragment_;
import net.sareweb.android.txotx.fragment.SagardotegiMapFragment_;
import net.sareweb.android.txotx.fragment.SagardotegiakFragment_;
import net.sareweb.android.txotx.fragment.SailkapenaFragment_;
import net.sareweb.android.txotx.plus.PlusConnectionCallbacks;
import net.sareweb.android.txotx.plus.PlusOnConnectionFailedListener;
import net.sareweb.android.txotx.util.AccountUtil;
import net.sareweb.android.txotx.util.TxotxPrefs_;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.PlusOneButton;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

@EActivity(R.layout.txotx_main)
public class TxotxActivity extends SherlockFragmentActivity {

	private static String TAG = "TxotxActivity";
	@Pref
	TxotxPrefs_ prefs;
	DrawerLayout mDrawerLayout;
	DrawerToggle drawerToggle;
	PlusClient mPlusClient;
	@ViewById(R.id.plus_one_button)
	PlusOneButton mPlusOneButton;

	@ViewById(R.id.btnSagardotegiak)
	DashboardButton btnSagardotegiak;
	@ViewById(R.id.btnSagardoEgunak)
	DashboardButton btnSagardoEgunak;
	
	@Extra int fragmentToBeLoaded;
	@Extra long sagardotegiId;
	@Extra long sagardoEgunId;
	@Extra long oharraId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");
		
		if(savedInstanceState!=null){
			Log.d(TAG, "Restoring state info");
			fragmentToBeLoaded = savedInstanceState.getInt(FRAGMENT, SAGARDOTEGIAK_FRAGMENT);
			Log.d(TAG, "fragmentToBeLoaded " + fragmentToBeLoaded);
		}

		PlusClient.Builder pcBuilder = new PlusClient.Builder(this,
				new PlusConnectionCallbacks(),
				new PlusOnConnectionFailedListener());
		
		mPlusClient = pcBuilder.clearScopes().build();
		
		
	}
	
	@Override
	protected void onStart() {
		super.onStart();
        mPlusClient.connect();
	}
	
	@Override
    protected void onStop() {
        super.onStop();
        mPlusClient.disconnect();
    }

	@Override
	protected void onResume() {
		super.onResume();
		
		//Notifikazioen berbideratzeak
		if(sagardotegiId!=0){
			SagardotegiDetailActivity_.intent(this).sagardotegiId(sagardotegiId).start();
			finish();
		}
		else if(sagardoEgunId!=0){
			SagardoEgunDetailActivity_.intent(this).sagardoEgunId(sagardoEgunId).start();
			finish();
		}

		mPlusOneButton.initialize(mPlusClient,
				"https://play.google.com/store/apps/details?id=net.sareweb.android.txotx", PLUS_ONE_REQUEST_CODE);

		
		switch (fragmentToBeLoaded) {
		case SAGARDOTEGIAK_FRAGMENT:
			clickOnSagardotegiList();
			break;
		
		case SAGARDO_EGUNAK_FRAGMENT:
			clickOnSagardoEgunakList();
			break;
			
		case MAP_FRAGMENT:
			clickOnSagardotegiMap();
			break;
			
		case OHARRA_FRAGMENT:
			clickOnOharra();
			break;
			
		case ABOUT_FRAGMENT:
			clickOnAbout();
			break;
			
		default:
			clickOnSagardoEgunakList();
			break;
		}
		
		btnSagardotegiak.setButtonNum(String.valueOf(SagardotegiCache.countSagardotegiak()));
		btnSagardoEgunak.setButtonNum(String.valueOf(SagardoEgunCache.countSagardoEgunak()));
		
	}
	

	@Click(R.id.btnSagardotegiak)
	public void clickOnSagardotegiList() {
		FragmentManager fragmentManager = getSupportFragmentManager();
		Fragment fragment = new SagardotegiakFragment_();

		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment).commit();

		mDrawerLayout.closeDrawers();
		fragmentToBeLoaded=SAGARDOTEGIAK_FRAGMENT;
	}

	@Click(R.id.btnSagardoEgunak)
	public void clickOnSagardoEgunakList() {
		FragmentManager fragmentManager = getSupportFragmentManager();
		Fragment fragment = new SagardoEgunakFragment_();

		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment).commit();

		mDrawerLayout.closeDrawers();
		fragmentToBeLoaded=SAGARDO_EGUNAK_FRAGMENT;
	}

	@Click(R.id.btnMap)
	public void clickOnSagardotegiMap() {
		FragmentManager fragmentManager = getSupportFragmentManager();
		Fragment fragment = new SagardotegiMapFragment_();

		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment).commit();

		mDrawerLayout.closeDrawers();
		fragmentToBeLoaded=MAP_FRAGMENT;
	}

	@Click(R.id.btnSailkapena)
	public void clickOnSailkapena() {
		FragmentManager fragmentManager = getSupportFragmentManager();
		Fragment fragment = new SailkapenaFragment_();

		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment).commit();

		mDrawerLayout.closeDrawers();
		fragmentToBeLoaded=SAILKAPENA_FRAGMENT;
	}

	@Click(R.id.btnErronka)
	public void clickOnOharra() {
		Bundle args = new Bundle();
		args.putLong("oharraId", oharraId);
		FragmentManager fragmentManager = getSupportFragmentManager();
		Fragment fragment = new OharraFragment_();
		fragment.setArguments(args);

		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment).commit();

		mDrawerLayout.closeDrawers();
		oharraId = 0;
		fragmentToBeLoaded=OHARRA_FRAGMENT;
	}
	
	@Click(R.id.btnAbout)
	public void clickOnAbout() {
		FragmentManager fragmentManager = getSupportFragmentManager();
		Fragment fragment = new AboutFragment_();

		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment).commit();

		mDrawerLayout.closeDrawers();
		fragmentToBeLoaded=ABOUT_FRAGMENT;
	}

	@OptionsItem
	boolean homeSelected() {
		mDrawerLayout.openDrawer(Gravity.LEFT);
		return true;
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		Log.d(TAG, "onPostCreate");
		if (mDrawerLayout == null) {
			mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
			drawerToggle = new DrawerToggle(this, mDrawerLayout,
					R.drawable.ic_drawer, R.string.ireki, R.string.itxi);

			mDrawerLayout.setDrawerListener(drawerToggle);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setHomeButtonEnabled(true);
		}
		drawerToggle.syncState();
		
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		drawerToggle.onConfigurationChanged(newConfig);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Log.d(TAG, "Saving current state info");
		outState.putInt(FRAGMENT, fragmentToBeLoaded);
	}


	private static final String FRAGMENT ="fragment";
	public static final int NO_FRAGMENT = -1;
	public static final int SAGARDOTEGIAK_FRAGMENT = 0;
	public static final int SAGARDO_EGUNAK_FRAGMENT = 1;
	public static final int MAP_FRAGMENT = 2;
	public static final int SAILKAPENA_FRAGMENT = 3;
	public static final int OHARRA_FRAGMENT = 4;
	public static final int ABOUT_FRAGMENT = 5;

	private static final int PLUS_ONE_REQUEST_CODE = 0;

}