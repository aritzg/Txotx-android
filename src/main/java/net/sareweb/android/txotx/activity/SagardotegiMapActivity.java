package net.sareweb.android.txotx.activity;
import java.util.HashMap;
import java.util.List;

import net.sareweb.android.txotx.adapter.SagardotegiInfoWindowAdapter;
import net.sareweb.android.txotx.cache.SagardotegiCache;
import net.sareweb.android.txotx.model.Sagardotegi;
import net.sareweb.android.txotx.util.Constants;
import net.sareweb.android.txotx.util.TxotxPrefs_;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

@EActivity
public class SagardotegiMapActivity extends SherlockFragmentActivity  implements OnMarkerClickListener{

	private static final String TAG = "SagardotegiMapActivity";
	private SupportMapFragment mapFragment;
	private ActionBar actionBar;
	@Pref TxotxPrefs_ prefs; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		LinearLayout layout = new LinearLayout(this);
		layout.setId(1111);

		mapFragment = SupportMapFragment.newInstance();
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.add(layout.getId(), mapFragment);
		ft.commit();

		setContentView(layout);

	}

	@Override
	protected void onResume() {
		super.onResume();
		LatLng position = new LatLng(Constants.DEFAULT_LAT, Constants.DEFAULT_LNG);

		CameraPosition cameraPosition = new CameraPosition(position, 10, 0, 0);
		CameraUpdate cu = CameraUpdateFactory.newCameraPosition(cameraPosition);
		mapFragment.getMap().moveCamera(cu);

		getSagardotegiak();

	}

	@Background
	public void getSagardotegiak(){
		getSagardotegiaksResult(SagardotegiCache.getSagardotegiak(false));
	}

	@UiThread
	public void getSagardotegiaksResult(List<Sagardotegi> sagardotegiak){
		GoogleMap map = mapFragment.getMap();
		HashMap<Marker, Sagardotegi> sagardotegiHM = new HashMap<Marker, Sagardotegi>();
		for(Sagardotegi sagardotegi : sagardotegiak){
			if(sagardotegi.getLat()!=0 && sagardotegi.getLng()!=0){
				LatLng sagardotegiPosition = new LatLng(sagardotegi.getLat(), sagardotegi.getLng());
				MarkerOptions mo = new MarkerOptions();
				mo.position(sagardotegiPosition);
				sagardotegiHM.put(map.addMarker(mo), sagardotegi);
			}
		}
		map.setInfoWindowAdapter(new SagardotegiInfoWindowAdapter(this, sagardotegiHM));
	
	}

	@OptionsItem(android.R.id.home)
	void homeSelected() {
		finish();
	}
	

	@Override
	public boolean onMarkerClick(Marker arg0) {
		// TODO Auto-generated method stub
		return false;
	}

}