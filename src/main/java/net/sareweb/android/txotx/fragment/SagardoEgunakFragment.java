package net.sareweb.android.txotx.fragment;

import java.util.ArrayList;
import java.util.List;

import net.sareweb.android.txotx.R;
import net.sareweb.android.txotx.activity.SagardoEgunDetailActivity_;
import net.sareweb.android.txotx.adapter.SagardoEgunAdapter;
import net.sareweb.android.txotx.cache.SagardoEgunCache;
import net.sareweb.android.txotx.model.SagardoEgun;
import net.sareweb.android.txotx.util.TxotxPrefs_;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

@EFragment(R.layout.sagardoegunak_fragment)
public class SagardoEgunakFragment extends SherlockFragment implements OnItemClickListener{

	private static String TAG = "SagardoEgunakFragment";
	@Pref TxotxPrefs_ prefs;
	private ProgressDialog dialog;

	public void setSagardoEgunakContent(boolean refresh, String filter){
		if(dialog!=null)dialog.dismiss();
		dialog = ProgressDialog.show(getSherlockActivity(), "", "SagardoEgunak eskuratzen...", true);
		dialog.show();
		getSagardoEgunak(refresh, filter);
	}	
	
	@Background
	public void getSagardoEgunak(boolean refresh, String filter){
		Log.d(TAG, "Gettings sagardoEgunak");
		List<SagardoEgun> sagardoEgunak = SagardoEgunCache.getSagardoEgunak(refresh);
		getSagardoEgunakResult(filterSagardoEgunak(sagardoEgunak, filter));
	}

	@UiThread
	public void getSagardoEgunakResult(List<SagardoEgun> sagardoEgunak){
		if(sagardoEgunak!=null){
			ListView sagardoEgunakListView = (ListView) getActivity().findViewById(R.id.sagardoegunak_list_view);
			sagardoEgunakListView.setAdapter(new SagardoEgunAdapter(getActivity(), sagardoEgunak));
			SagardoEgunAdapter adapter = (SagardoEgunAdapter)sagardoEgunakListView.getAdapter();
			adapter.notifyDataSetChanged();
			sagardoEgunakListView.setOnItemClickListener(this);
		}
		else{
			Toast.makeText(getActivity(), "Errorea sagardoEgunak kargatzen!!", Toast.LENGTH_LONG).show();
			SagardoEgunCache.init(prefs);
		}
		dialog.cancel();
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		SagardoEgun sagardoEgun = (SagardoEgun) view.getTag();
		Log.d(TAG, "Selected sagardoEgun " + sagardoEgun.getSagardoEgunId());
		//SagardoEgunDetailActivity_.intent(getSherlockActivity()).sagardoEgun(sagardoEgun).start();
	}
	
	private List<SagardoEgun> filterSagardoEgunak(List<SagardoEgun> sagardoEgunak, String filter){
		List<SagardoEgun> sagardoEgunakTmp = new ArrayList<SagardoEgun>();
		if(sagardoEgunak!=null) {
			for(SagardoEgun sagardoEgun : sagardoEgunak){
				if(sagardoEgun.getIzena()!=null){
					if(sagardoEgun.getIzena().toLowerCase().contains(filter.toLowerCase()))sagardoEgunakTmp.add(sagardoEgun);
				}
			}
		}
		return sagardoEgunakTmp;
	}
}
