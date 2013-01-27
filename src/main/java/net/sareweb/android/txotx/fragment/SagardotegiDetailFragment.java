package net.sareweb.android.txotx.fragment;

import net.sareweb.android.txotx.R;
import net.sareweb.android.txotx.image.ImageLoader;
import net.sareweb.android.txotx.model.Sagardotegi;
import net.sareweb.android.txotx.rest.SagardotegiRESTClient;
import net.sareweb.android.txotx.rest.TxotxConnectionData;
import net.sareweb.android.txotx.util.ImageUtils;
import net.sareweb.android.txotx.util.TxotxPrefs_;
import net.sareweb.lifedroid.rest.DLFileEntryRESTClient;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.FragmentArg;
import com.googlecode.androidannotations.annotations.FragmentById;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

@EFragment(R.layout.sagardotegi_detail_fragment)
public class SagardotegiDetailFragment extends SherlockFragment{

	private static String TAG = "SagardotegiDetailFragment";
	@Pref TxotxPrefs_ prefs;
	DLFileEntryRESTClient dlFileEntryRESTClient;
	SagardotegiRESTClient sagardotegiRESTClient;
	
	@ViewById
	TextView txIzena;
	@ViewById
	ImageView imgSagardotegi;
	@ViewById
	ImageView imgMap;
	@FragmentArg
	Sagardotegi sagardotegi;
	@FragmentById
	SupportMapFragment mapFragment;
	ImageLoader imgLoader;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dlFileEntryRESTClient = new DLFileEntryRESTClient(new TxotxConnectionData(prefs));
		sagardotegiRESTClient = new SagardotegiRESTClient(new TxotxConnectionData(prefs));

		imgLoader = new ImageLoader(getActivity());

	}

	@Override
	public void onResume() {
		super.onResume();
		if(sagardotegi!=null)setSagardotegiContent(sagardotegi);
	}

	public void setSagardotegiContent(Sagardotegi sagardotegi){
		this.sagardotegi=sagardotegi;
		txIzena.setText(sagardotegi.getIzena());
		imgLoader.displayImage(ImageUtils.getSagardotegiImageUrl(sagardotegi), imgSagardotegi);
		showHideMap(sagardotegi);
	}
	
	
	private void showHideMap(Sagardotegi sagardotegi){
		if(sagardotegi.getLat()==0L && sagardotegi.getLng()==0L){
			mapFragment.getFragmentManager().beginTransaction().hide(mapFragment).commit();
			imgMap.setVisibility(View.VISIBLE);
		}
		else{
			mapFragment.getFragmentManager().beginTransaction().show(mapFragment).commit();

			GoogleMap map = mapFragment.getMap();
			map.clear();
			LatLng latLng = new LatLng(sagardotegi.getLat(), sagardotegi.getLng());
			CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(latLng, 8);
			map.moveCamera(cu);
			MarkerOptions mo =new MarkerOptions();
			mo.position(latLng);
			map.addMarker(mo);
			imgMap.setVisibility(View.GONE);
		}
	}
	
	
}