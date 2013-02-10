package net.sareweb.android.txotx.fragment;

import java.util.List;

import net.sareweb.android.txotx.R;
import net.sareweb.android.txotx.activity.SagardotegiDetailActivity_;
import net.sareweb.android.txotx.adapter.SagardotegiAdapter;
import net.sareweb.android.txotx.cache.SagardotegiCache;
import net.sareweb.android.txotx.model.Sagardotegi;
import net.sareweb.android.txotx.util.TxotxPrefs_;
import android.app.ProgressDialog;
import android.content.Intent;
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

	public void setSagardotegiakContent(boolean refresh){
		dialog = ProgressDialog.show(getSherlockActivity(), "", "Sagardotegiak eskuratzen...", true);
		dialog.show();
		getSagardotegiak(refresh);
	}

	@Background
	public void getSagardotegiak(boolean refresh){
		Log.d(TAG, "Gettings sagardotegiak");
		getSagardotegiakResult(SagardotegiCache.getSagardotegiak(refresh));
	}

	@UiThread
	public void getSagardotegiakResult(List<Sagardotegi> sagardotegiak){
		if(sagardotegiak!=null){
			ListView gardensListView = (ListView) getActivity().findViewById(R.id.sagardotegiak_list_view);
			gardensListView.setAdapter(new SagardotegiAdapter(getActivity(), sagardotegiak));
			gardensListView.setOnItemClickListener(this);
		}
		else{
			Toast.makeText(getActivity(), "Errorea sagardotegiak kargatzen!!", Toast.LENGTH_LONG).show();
			SagardotegiCache.init(prefs);
		}
		dialog.cancel();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Sagardotegi sagardotegi = (Sagardotegi) view.getTag();
		Log.d(TAG, "Selected sagardotegi " + sagardotegi.getSagardotegiId());
		SagardotegiDetailActivity_.intent(getSherlockActivity()).sagardotegi(sagardotegi).start();
	}
}
