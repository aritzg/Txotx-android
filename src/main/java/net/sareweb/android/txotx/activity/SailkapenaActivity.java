package net.sareweb.android.txotx.activity;
import java.util.List;

import net.sareweb.android.txotx.R;
import net.sareweb.android.txotx.adapter.SailkapenaAdapter;
import net.sareweb.android.txotx.model.Sailkapena;
import net.sareweb.android.txotx.rest.SailkapenaRESTClient;
import net.sareweb.android.txotx.rest.TxotxConnectionData;
import net.sareweb.android.txotx.util.AccountUtil;
import net.sareweb.android.txotx.util.TxotxPrefs_;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

@Deprecated
@EActivity(R.layout.sailkapena_activity)
public class SailkapenaActivity extends SherlockActivity{

	private static final String TAG = "SailkapenaActivity";
	private ActionBar actionBar;
	@Pref TxotxPrefs_ prefs; 
	private List<Sailkapena> sailkapenak = null;
	ProgressDialog progressDialog;
	SailkapenaAdapter sailkapenaAdapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

	}

	@Override
	protected void onResume() {
		super.onResume();
		progressDialog = ProgressDialog.show(this, "", "Sailkapena kargatzen...", true);
		progressDialog.show();
		getSailkapenak();

	}

	@Background
	public void getSailkapenak(){
		SailkapenaRESTClient sailkapenaRESTClient = new SailkapenaRESTClient(new TxotxConnectionData(this));
		getgetSailkapenakResult(sailkapenaRESTClient.getSailkapenak());
	}

	@UiThread
	public void getgetSailkapenakResult(List<Sailkapena> sailkapenak){
		if(sailkapenaAdapter==null){
			sailkapenaAdapter = new SailkapenaAdapter(this, sailkapenak);
			ListView sailkapenaListView = (ListView) this.findViewById(R.id.sailkapena_list_view);
			sailkapenaListView.setAdapter(sailkapenaAdapter);
		}
		progressDialog.cancel();
	}

	@OptionsItem(android.R.id.home)
	void homeSelected() {
		finish();
	}
	

}