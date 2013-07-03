package net.sareweb.android.txotx.fragment;

import java.util.List;

import net.sareweb.android.txotx.R;
import net.sareweb.android.txotx.adapter.SailkapenaAdapter;
import net.sareweb.android.txotx.model.Sailkapena;
import net.sareweb.android.txotx.rest.SailkapenaRESTClient;
import net.sareweb.android.txotx.rest.TxotxConnectionData;
import net.sareweb.android.txotx.util.TxotxPrefs_;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

@EFragment(R.layout.sailkapena_fragment)
public class SailkapenaFragment extends SherlockFragment {

	private static final String TAG = "SailkapenaFragment";
	@Pref
	TxotxPrefs_ prefs;
	private List<Sailkapena> sailkapenak = null;
	ProgressDialog progressDialog;
	SailkapenaAdapter sailkapenaAdapter = null;

	@Override
	public void onResume() {
		super.onResume();
		progressDialog = ProgressDialog.show(getActivity(), "",
				"Sailkapena kargatzen...", true);
		progressDialog.show();
		getSailkapenak();
	}
	
	@Background
	public void getSailkapenak(){
		SailkapenaRESTClient sailkapenaRESTClient = new SailkapenaRESTClient(new TxotxConnectionData(prefs));
		getgetSailkapenakResult(sailkapenaRESTClient.getSailkapenak());
	}

	@UiThread
	public void getgetSailkapenakResult(List<Sailkapena> sailkapenak){
		if(sailkapenaAdapter==null){
			sailkapenaAdapter = new SailkapenaAdapter(getActivity(), sailkapenak);
			ListView sailkapenaListView = (ListView) getActivity().findViewById(R.id.sailkapena_list_view);
			sailkapenaListView.setAdapter(sailkapenaAdapter);
		}
		progressDialog.cancel();
	}

}
