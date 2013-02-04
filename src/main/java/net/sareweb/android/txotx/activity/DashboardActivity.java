package net.sareweb.android.txotx.activity;

import net.sareweb.android.txotx.R;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockActivity;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;

@EActivity(R.layout.dashboard)
public class DashboardActivity extends SherlockActivity {

	@Extra
	boolean redirect = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(redirect){
			SagardotegiakActivity_.intent(this).start();
		}
	}

	@Click(R.id.btnList)
	public void clickOnSagardotegiList(){
		SagardotegiakActivity_.intent(this).start();
	}

	@Click(R.id.btnMap)
	public void clickOnSagardotegiMap(){
	}


	@Click(R.id.btnAbout)
	public void clickOnAbout(){
	}

}
