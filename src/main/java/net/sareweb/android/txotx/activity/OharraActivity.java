package net.sareweb.android.txotx.activity;
import java.util.List;

import net.sareweb.android.txotx.R;
import net.sareweb.android.txotx.adapter.SailkapenaAdapter;
import net.sareweb.android.txotx.image.ImageLoader;
import net.sareweb.android.txotx.model.Oharra;
import net.sareweb.android.txotx.model.Sailkapena;
import net.sareweb.android.txotx.rest.OharraRESTClient;
import net.sareweb.android.txotx.rest.SailkapenaRESTClient;
import net.sareweb.android.txotx.rest.TxotxConnectionData;
import net.sareweb.android.txotx.util.ImageUtils;
import net.sareweb.android.txotx.util.TxotxPrefs_;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

@EActivity(R.layout.oharra_activity)
public class OharraActivity extends SherlockActivity{

	private static final String TAG = "OharraActivity";
	private ActionBar actionBar;
	@Pref TxotxPrefs_ prefs; 
	ProgressDialog progressDialog;
	@Extra long oharraId;
	@ViewById TextView txOharra;
	@ViewById ImageView imgOharra;
	private ImageLoader imgLoader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		imgLoader = new ImageLoader(this);

	}

	@Override
	protected void onResume() {
		super.onResume();
		progressDialog = ProgressDialog.show(this, "", "Kargatzen...", true);
		progressDialog.show();
		getOharra();
	}

	@Background
	void getOharra(){
		OharraRESTClient oharraRESTClient = new OharraRESTClient(new TxotxConnectionData(prefs));
		if(oharraId==0){//erronka da
			getOharraResult(oharraRESTClient.getAzkenErronka());
		}
		else{//Oharra da
			getOharraResult(oharraRESTClient.getOharra(oharraId));
			NotificationManager mNotificationManager =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			mNotificationManager.cancel((int)oharraId);
		}
	}

	@UiThread
	void getOharraResult(Oharra oharra){
		progressDialog.cancel();
		if(oharra==null || oharra.getOharraId()==0){
			txOharra.setText("Oraindik ez dago oharrik edota erronkarik!");
		}
		else{
			actionBar.setTitle(oharra.getIzenburua());
			txOharra.setText(oharra.getTestua());
			
			imgLoader.displayImage(ImageUtils.getOharraImageUrl(oharra), imgOharra, R.drawable.erronka);
		}
	}
	
	@OptionsItem(android.R.id.home)
	void homeSelected() {
		finish();
		if(oharraId!=0){
			DashboardActivity_.intent(this).start();
		}
	}
	

}