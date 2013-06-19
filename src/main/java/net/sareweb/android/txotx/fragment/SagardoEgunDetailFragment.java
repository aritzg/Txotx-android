package net.sareweb.android.txotx.fragment;

import net.sareweb.android.txotx.R;
import net.sareweb.android.txotx.cache.GertaeraCache;
import net.sareweb.android.txotx.image.ImageLoader;
import net.sareweb.android.txotx.model.SagardoEgun;
import net.sareweb.android.txotx.rest.SagardoEgunRESTClient;
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
import com.googlecode.androidannotations.api.SdkVersionHelper;

@EFragment(R.layout.sagardoegun_detail_fragment)
public class SagardoEgunDetailFragment extends SherlockFragment{

	private static String TAG = "SagardoEgunDetailFragment";
	@Pref TxotxPrefs_ prefs;
	DLFileEntryRESTClient dlFileEntryRESTClient;
	SagardoEgunRESTClient sagardoEgunRESTClient;
	
	@ViewById
	ImageView imgSagardoEgun;
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
	SagardoEgun sagardoEgun;
	@FragmentById
	SupportMapFragment mapFragment;
	ImageLoader imgLoader;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        
		dlFileEntryRESTClient = new DLFileEntryRESTClient(new TxotxConnectionData(prefs));
		sagardoEgunRESTClient = new SagardoEgunRESTClient(new TxotxConnectionData(prefs));

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
		if(sagardoEgun!=null)setSagardoEgunContent(sagardoEgun);
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

	
	public void setSagardoEgunContent(SagardoEgun sagardoEgun){
		this.sagardoEgun=sagardoEgun;
	
		
		if(sagardoEgun.getEmaila()!=null && !sagardoEgun.getEmaila().equals("")){
			txEmail.setText(sagardoEgun.getEmaila());
			txEmailRow.setVisibility(View.VISIBLE);
		}
		else{
			txEmailRow.setVisibility(View.GONE);
		}
		
		if(sagardoEgun.getWeborria()!=null && !sagardoEgun.getWeborria().equals("")){
			txWeb.setText(sagardoEgun.getWeborria());
			txWebRow.setVisibility(View.VISIBLE);
		}
		else{
			txWebRow.setVisibility(View.GONE);
		}
		
		imgLoader.displayImage(ImageUtils.getSagardoEgunImageUrl(sagardoEgun), imgSagardoEgun);
		if(sagardoEgun.getIrudia() == null || sagardoEgun.getIrudia().equals("")){
			imgSagardoEgun.setVisibility(View.GONE);
		}else{
			imgSagardoEgun.setVisibility(View.VISIBLE);
			if(SdkVersionHelper.getSdkInt()>11){
				imgSagardoEgun.setRotation(-10);
			}
		}
		
		showHideMap(sagardoEgun);
	}
	
	
	private void showHideMap(SagardoEgun sagardoEgun){
		if(sagardoEgun.getLat()==0L && sagardoEgun.getLng()==0L){
			mapFragment.getFragmentManager().beginTransaction().hide(mapFragment).commit();
			imgMap.setVisibility(View.VISIBLE);
		}
		else{
			mapFragment.getFragmentManager().beginTransaction().show(mapFragment).commit();

			GoogleMap map = mapFragment.getMap();
			if(map!=null){
				map.clear();
				
				LatLng latLng = new LatLng(sagardoEgun.getLat(), sagardoEgun.getLng());
				MarkerOptions mo =new MarkerOptions();
				mo.position(latLng);
				map.addMarker(mo);
				
				map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
				map.animateCamera(CameraUpdateFactory.scrollBy(20,20));
				
				imgMap.setVisibility(View.GONE);
			}
		}
	}
	
	@Background
	public void gertaerakAldezAurretikEkarri(){
		try {
			GertaeraCache.getGertaerak(sagardoEgun.getSagardoEgunId(), true);
		} catch (Exception e) {
			Log.e(TAG, "Errorea gertaerak aldez aurretik kargatzen");
		}
	}
	
	
}