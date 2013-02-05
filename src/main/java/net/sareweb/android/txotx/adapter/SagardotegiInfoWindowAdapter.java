package net.sareweb.android.txotx.adapter;

import java.util.HashMap;

import net.sareweb.android.txotx.R;
import net.sareweb.android.txotx.model.Sagardotegi;
import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;

public class SagardotegiInfoWindowAdapter implements InfoWindowAdapter {

	Activity activity;
	HashMap<Marker, Sagardotegi> sagardotegiHM;
	
	public SagardotegiInfoWindowAdapter(Activity activity, HashMap<Marker, Sagardotegi> sagardotegiHM){
		this.activity=activity;
		this.sagardotegiHM=sagardotegiHM;
	}
	
	@Override
	public View getInfoContents(Marker arg0) {
		return null;
	}

	@Override
	public View getInfoWindow(Marker arg0) {
		View iWindow = activity.getLayoutInflater().inflate(R.layout.info_window, null);
		TextView txIzena = (TextView)iWindow.findViewById(R.id.txIzena);
		txIzena.setText(sagardotegiHM.get(arg0).getIzena());
		
		TextView txHelbidea = (TextView)iWindow.findViewById(R.id.txHelbidea);
		txHelbidea.setText(sagardotegiHM.get(arg0).getHelbidea());
		
		TextView txTlf = (TextView)iWindow.findViewById(R.id.txTlf);
		txTlf.setText(sagardotegiHM.get(arg0).getTelefonoa());
		
		return iWindow;
	}

}
