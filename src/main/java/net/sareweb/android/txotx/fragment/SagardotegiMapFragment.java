package net.sareweb.android.txotx.fragment;

import java.util.HashMap;
import java.util.List;

import net.sareweb.android.txotx.adapter.SagardotegiInfoWindowAdapter;
import net.sareweb.android.txotx.cache.SagardotegiCache;
import net.sareweb.android.txotx.model.Sagardotegi;
import net.sareweb.android.txotx.util.Constants;
import net.sareweb.android.txotx.util.TxotxPrefs_;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

@EFragment
public class SagardotegiMapFragment extends SupportMapFragment implements OnMarkerClickListener{

	private static final String TAG = "SagardotegiMapFragment";
	@Pref TxotxPrefs_ prefs; 
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onResume() {
		Log.d(TAG, "onResume");
		super.onResume();
		LatLng position = new LatLng(Constants.DEFAULT_LAT, Constants.DEFAULT_LNG);

		CameraPosition cameraPosition = new CameraPosition(position, 10, 0, 0);
		CameraUpdate cu = CameraUpdateFactory.newCameraPosition(cameraPosition);
		getMap().moveCamera(cu);

		getSagardotegiak();
	}
	
	@Background
	public void getSagardotegiak(){
		getSagardotegiaksResult(SagardotegiCache.getSagardotegiak(false));
	}

	@UiThread
	public void getSagardotegiaksResult(List<Sagardotegi> sagardotegiak){
		Log.d(TAG, "Sagardotegiak mapan jartzen");
		getMap().clear();
		HashMap<String, Sagardotegi> sagardotegiHM = new HashMap<String, Sagardotegi>();
		for(Sagardotegi sagardotegi : sagardotegiak){
			if(sagardotegi.getLat()!=0 && sagardotegi.getLng()!=0){
				LatLng sagardotegiPosition = new LatLng(sagardotegi.getLat(), sagardotegi.getLng());
				MarkerOptions mo = new MarkerOptions();
				mo.position(sagardotegiPosition);
				sagardotegiHM.put(getMap().addMarker(mo).getId(), sagardotegi);
			}
		}
		SagardotegiInfoWindowAdapter siwAdapter = new SagardotegiInfoWindowAdapter(getActivity() , sagardotegiHM);
		getMap().setInfoWindowAdapter(siwAdapter);
		getMap().setOnInfoWindowClickListener(siwAdapter);
	}
	
	@Override
	public boolean onMarkerClick(Marker arg0) {
		return false;
	}

}
