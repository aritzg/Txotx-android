package net.sareweb.android.txotx.fragment;

import java.util.List;

import net.sareweb.android.txotx.R;
import net.sareweb.android.txotx.adapter.SagardotegiAdapter;
import net.sareweb.android.txotx.model.Sagardotegi;
import net.sareweb.android.txotx.rest.SagardotegiRESTClient;
import net.sareweb.android.txotx.rest.TxotxConnectionData;
import net.sareweb.android.txotx.util.TxotxPrefs_;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

@EFragment(R.layout.sagardotegiak_fragment)
public class SagardotegiakFragment extends SherlockFragment implements OnItemClickListener{

	private static String TAG = "SagardotegiakFragment";
	@Pref TxotxPrefs_ prefs;
	private ProgressDialog dialog;

	public void setSagardotegiakContent(){
		dialog = ProgressDialog.show(getSherlockActivity(), "", "Loading sagardotegiak...", true);
		dialog.show();
		getSagardotegiak();
	}

	@Background
	public void getSagardotegiak(){
		Log.d(TAG, "Gettings sagardotegiak");
		SagardotegiRESTClient sagardotegiRestClient = new SagardotegiRESTClient(new TxotxConnectionData(prefs));
		getSagardotegiakResult(sagardotegiRestClient.getSagardotegiak());
	}

	@UiThread
	public void getSagardotegiakResult(List<Sagardotegi> sagardotegiak){
		ListView gardensListView = (ListView) getActivity().findViewById(R.id.sagardotegiak_list_view);
		gardensListView.setAdapter(new SagardotegiAdapter(getActivity(), sagardotegiak));
		gardensListView.setOnItemClickListener(this);
		dialog.cancel();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Sagardotegi sagardotegi = (Sagardotegi) view.getTag();
		Log.d(TAG, "Selected sagardotegi " + sagardotegi.getSagardotegiId());
		//TODO:gertaera zerrenda
	}
}