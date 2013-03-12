package net.sareweb.android.txotx.fragment;

import net.sareweb.android.txotx.R;
import net.sareweb.android.txotx.cache.GertaeraCache;
import net.sareweb.android.txotx.image.ImageLoader;
import net.sareweb.android.txotx.model.Sagardotegi;
import net.sareweb.android.txotx.rest.SagardotegiRESTClient;
import net.sareweb.android.txotx.rest.TxotxConnectionData;
import net.sareweb.android.txotx.util.ImageUtils;
import net.sareweb.android.txotx.util.TxotxPrefs_;
import net.sareweb.lifedroid.rest.DLFileEntryRESTClient;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.googlecode.androidannotations.annotations.Background;
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
	ImageView imgSagardotegi;
	@ViewById
	ImageView imgMap;
	@ViewById TextView txTlf;
	@ViewById LinearLayout txTlfRow;
	@ViewById TextView txEmail;
	@ViewById LinearLayout txEmailRow;
	@ViewById TextView txWeb;
	@ViewById LinearLayout txWebRow;
	@ViewById TextView txHelbide;
	@ViewById LinearLayout txHelbideRow;
	@ViewById TextView txEdukiera;
	@ViewById LinearLayout txEdukieraRow;
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
		
		gertaerakAldezAurretikEkarri();
	}
	
	@Override
	public View onCreateView(android.view.LayoutInflater inflater, android.view.ViewGroup container, Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	

	@Override
	public void onResume() {
		super.onResume();
		if(sagardotegi!=null)setSagardotegiContent(sagardotegi);
	}
	
	@Override
	public void onDestroyView() {
		try {
			FragmentTransaction transaction = getSherlockActivity()
					.getSupportFragmentManager().beginTransaction();

			transaction.remove(mapFragment);

			transaction.commit();
		} catch (Exception e) {
		}

		super.onDestroyView();
	}

	
	public void setSagardotegiContent(Sagardotegi sagardotegi){
		this.sagardotegi=sagardotegi;
		if(sagardotegi.getTelefonoa()!=null && !sagardotegi.getTelefonoa().equals("")){
			txTlf.setText(sagardotegi.getTelefonoa());
			txTlfRow.setVisibility(View.VISIBLE);
		}
		else{
			txTlfRow.setVisibility(View.GONE);
		}
		
		if(sagardotegi.getEmaila()!=null && !sagardotegi.getEmaila().equals("")){
			txEmail.setText(sagardotegi.getEmaila());
			txEmailRow.setVisibility(View.VISIBLE);
		}
		else{
			txEmailRow.setVisibility(View.GONE);
		}
		
		if(sagardotegi.getWeborria()!=null && !sagardotegi.getWeborria().equals("")){
			txWeb.setText(sagardotegi.getWeborria());
			txWebRow.setVisibility(View.VISIBLE);
		}
		else{
			txWebRow.setVisibility(View.GONE);
		}
		
		if(sagardotegi.getHelbidea()!=null && !sagardotegi.getHelbidea().equals("")){
			txHelbide.setText(sagardotegi.getHelbidea());
			txHelbideRow.setVisibility(View.VISIBLE);
		}
		else{
			txHelbideRow.setVisibility(View.GONE);
		}
		
		if(sagardotegi.getEdukiera()!=0){
			txEdukiera.setText(sagardotegi.getEdukiera() + " lagun");
		}else{
			txEdukiera.setText("Edukiera ezezaguna");
		}
		
		
		imgLoader.displayImage(ImageUtils.getSagardotegiImageUrl(sagardotegi), imgSagardotegi);
		if(sagardotegi.getIrudia() == null || sagardotegi.getIrudia().equals("")){
			imgSagardotegi.setVisibility(View.GONE);
		}else{
			imgSagardotegi.setVisibility(View.VISIBLE);
			imgSagardotegi.setRotation(-10);
		}
		
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
			MarkerOptions mo =new MarkerOptions();
			mo.position(latLng);
			map.addMarker(mo);
			
			
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
			map.animateCamera(CameraUpdateFactory.scrollBy(20,20));
			
			
			
			imgMap.setVisibility(View.GONE);
		}
	}
	
	@Background
	public void gertaerakAldezAurretikEkarri(){
		try {
			GertaeraCache.getSagardotegiGertaerak(sagardotegi.getSagardotegiId(), true);
		} catch (Exception e) {
			Log.e(TAG, "Errorea gertaerak aldez aurretik kargatzen");
		}
	}
	
	
}