package net.sareweb.android.txotx.adapter;

import java.util.HashMap;

import net.sareweb.android.txotx.R;
import net.sareweb.android.txotx.activity.SagardotegiDetailActivity_;
import net.sareweb.android.txotx.model.Sagardotegi;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.model.Marker;

public class SagardotegiInfoWindowAdapter implements InfoWindowAdapter, OnInfoWindowClickListener {

	private static final String TAG = "SagardotegiInfoWindowAdapter";
	Activity activity;
	HashMap<String, Sagardotegi> sagardotegiHM;
	
	public SagardotegiInfoWindowAdapter(Activity activity, HashMap<String, Sagardotegi> sagardotegiHM){
		this.activity=activity;
		this.sagardotegiHM=sagardotegiHM;
	}
	
	@Override
	public View getInfoContents(Marker arg0) {
		return null;
	}

	@Override
	public View getInfoWindow(Marker arg0) {
		Log.d(TAG, "sagardotegiHM.size() " + sagardotegiHM.size()  + " " + arg0.getId());
		View iWindow = activity.getLayoutInflater().inflate(R.layout.info_window, null);
		
		Sagardotegi sagardotegi = sagardotegiHM.get(arg0.getId());
		
		TextView txIzena = (TextView)iWindow.findViewById(R.id.txIzena);
		txIzena.setText(sagardotegi.getIzena());
		
		TextView txHelbidea = (TextView)iWindow.findViewById(R.id.txHelbidea);
		txHelbidea.setText(sagardotegi.getHelbidea());
		
		TextView txTlf = (TextView)iWindow.findViewById(R.id.txTlf);
		txTlf.setText(sagardotegi.getTelefonoa());
		
		return iWindow;
	}

	@Override
	public void onInfoWindowClick(Marker arg0) {
		Sagardotegi sagardotegi = sagardotegiHM.get(arg0.getId());
		SagardotegiDetailActivity_.intent(activity).sagardotegi(sagardotegi).start();
	}

}
