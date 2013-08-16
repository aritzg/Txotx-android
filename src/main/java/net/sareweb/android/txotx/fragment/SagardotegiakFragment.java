package net.sareweb.android.txotx.fragment;

import java.util.ArrayList;
import java.util.List;

import net.sareweb.android.txotx.R;
import net.sareweb.android.txotx.activity.SagardotegiDetailActivity_;
import net.sareweb.android.txotx.adapter.SagardotegiAdapter;
import net.sareweb.android.txotx.cache.SagardotegiCache;
import net.sareweb.android.txotx.model.Sagardotegi;
import net.sareweb.android.txotx.util.TxotxPrefs_;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

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
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setSagardotegiakContent(false, "");
	}

	public void setSagardotegiakContent(boolean refresh, String filter){
		if(dialog!=null)dialog.dismiss();
		dialog = ProgressDialog.show(getSherlockActivity(), "", "Sagardotegiak eskuratzen...", true);
		dialog.show();
		getSagardotegiak(refresh, filter);
	}	
	
	@Background
	public void getSagardotegiak(boolean refresh, String filter){
		Log.d(TAG, "Gettings sagardotegiak");
		List<Sagardotegi> sagardotegiak = SagardotegiCache.getSagardotegiak(refresh);
		getSagardotegiakResult(filterSagardotegiak(sagardotegiak, filter));
	}

	@UiThread
	public void getSagardotegiakResult(List<Sagardotegi> sagardotegiak){
		if(sagardotegiak!=null){
			ListView gardensListView = (ListView) getActivity().findViewById(R.id.sagardotegiak_list_view);
			gardensListView.setAdapter(new SagardotegiAdapter(getActivity(), sagardotegiak));
			SagardotegiAdapter adapter = (SagardotegiAdapter)gardensListView.getAdapter();
			adapter.notifyDataSetChanged();
			gardensListView.setOnItemClickListener(this);
		}
		else{
			Toast.makeText(getActivity(), "Errorea sagardotegiak kargatzen!!", Toast.LENGTH_LONG).show();
			SagardotegiCache.init(getSherlockActivity());
		}
		dialog.cancel();
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Sagardotegi sagardotegi = (Sagardotegi) view.getTag();
		Log.d(TAG, "Selected sagardotegi " + sagardotegi.getSagardotegiId());
		SagardotegiDetailActivity_.intent(getSherlockActivity()).sagardotegi(sagardotegi).start();
	}
	
	private List<Sagardotegi> filterSagardotegiak(List<Sagardotegi> sagardotegiak, String filter){
		List<Sagardotegi> sagardotegiakTmp = new ArrayList<Sagardotegi>();
		if(sagardotegiak!=null) {
			for(Sagardotegi sagardotegi : sagardotegiak){
				if(sagardotegi.getIzena()!=null){
					if(sagardotegi.getIzena().toLowerCase().contains(filter.toLowerCase()))sagardotegiakTmp.add(sagardotegi);
				}
			}
		}
		return sagardotegiakTmp;
	}
}
