package net.sareweb.android.txotx.fragment;

import net.sareweb.android.txotx.R;
import net.sareweb.android.txotx.image.ImageLoader;
import net.sareweb.android.txotx.model.Oharra;
import net.sareweb.android.txotx.rest.OharraRESTClient;
import net.sareweb.android.txotx.rest.TxotxConnectionData;
import net.sareweb.android.txotx.util.ImageUtils;
import net.sareweb.android.txotx.util.TxotxPrefs_;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.FragmentArg;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

@EFragment(R.layout.oharra_fragment)
public class OharraFragment extends SherlockFragment {

	private static final String TAG = "OharraFragment";
	@Pref TxotxPrefs_ prefs; 
	ProgressDialog progressDialog;
	@FragmentArg long oharraId;
	@ViewById TextView txOharra;
	@ViewById ImageView imgOharra;
	private ImageLoader imgLoader;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		imgLoader = new ImageLoader(getActivity());
	}
	

	@Override
	public void onResume() {
		super.onResume();
		progressDialog = ProgressDialog.show(getActivity(), "", "Kargatzen...", true);
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
			NotificationManager mNotificationManager =(NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
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
			getSherlockActivity().getSupportActionBar().setTitle(oharra.getIzenburua());
			txOharra.setText(oharra.getTestua());
			
			imgLoader.displayImage(ImageUtils.getOharraImageUrl(oharra), imgOharra, R.drawable.erronka);
		}
	}
	


}
