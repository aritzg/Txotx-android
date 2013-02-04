package net.sareweb.android.txotx.activity;

import net.sareweb.android.txotx.R;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockActivity;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;

@EActivity(R.layout.dashboard)
public class DashboardActivity extends SherlockActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}


	@Click(R.id.btnList)
	public void clickOnSagardotegiList(){
		SagardotegiakActivity_.intent(this).showHomeBack(true).start();
	}

	@Click(R.id.btnMap)
	public void clickOnSagardotegiMap(){
	}


	@Click(R.id.btnAbout)
	public void clickOnAbout(){
	}

}
