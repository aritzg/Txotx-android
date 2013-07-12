package net.sareweb.android.txotx.activity;

import net.sareweb.android.txotx.R;
import net.sareweb.android.txotx.cache.GertaeraCache;
import net.sareweb.android.txotx.cache.SagardoEgunCache;
import net.sareweb.android.txotx.cache.SagardotegiCache;
import net.sareweb.android.txotx.cache.UserCache;
import net.sareweb.android.txotx.util.TxotxPrefs_;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockActivity;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

@Deprecated
@EActivity(R.layout.dashboard)
public class DashboardActivity extends SherlockActivity {

	@Pref TxotxPrefs_ prefs;
	@Extra boolean redirect = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		UserCache.init(this);
		SagardotegiCache.init(this);
		SagardoEgunCache.init(this);
		GertaeraCache.init(this);
		
		if(redirect){
			//SagardotegiakActivity_.intent(this).start();
			SagardoEgunakActivity_.intent(this).start();
		}
	}

	@Click(R.id.btnList)
	public void clickOnSagardotegiList(){
		SagardotegiakActivity_.intent(this).start();
	}

	@Click(R.id.btnMap)
	public void clickOnSagardotegiMap(){
		SagardotegiMapActivity_.intent(this).start();
	}

	@Click(R.id.btnSailkapena)
	public void clickOnSailkapena(){
		SailkapenaActivity_.intent(this).start();
	}

	@Click(R.id.btnErronka)
	public void clickOnErronka(){
		OharraActivity_.intent(this).start();
	}
	
	@Click(R.id.btnAbout)
	public void clickOnAbout(){
		AboutActivity_.intent(this).start();
	}
	
	@Click(R.id.btnSettings)
	public void clickOnSettings(){
		SettingsActivity_.intent(this).start();
	}

}
