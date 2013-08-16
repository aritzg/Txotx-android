package net.sareweb.android.txotx.fragment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sareweb.android.txotx.R;
import net.sareweb.android.txotx.adapter.GertaeraAdapter;
import net.sareweb.android.txotx.cache.GertaeraCache;
import net.sareweb.android.txotx.image.ImageLoader;
import net.sareweb.android.txotx.model.Gertaera;
import net.sareweb.android.txotx.model.SagardoEgun;
import net.sareweb.android.txotx.plus.PlusConnectionCallbacks;
import net.sareweb.android.txotx.plus.PlusOnConnectionFailedListener;
import net.sareweb.android.txotx.rest.GertaeraRESTClient;
import net.sareweb.android.txotx.rest.SagardoEgunRESTClient;
import net.sareweb.android.txotx.rest.TxotxConnectionData;
import net.sareweb.android.txotx.util.AccountUtil;
import net.sareweb.android.txotx.util.Constants;
import net.sareweb.android.txotx.util.ImageUtils;
import net.sareweb.android.txotx.util.TxotxPrefs_;
import net.sareweb.lifedroid.model.DLFileEntry;
import net.sareweb.lifedroid.rest.DLFileEntryRESTClient;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.PlusOneButton;
import com.google.android.gms.plus.PlusShare;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.FragmentArg;
import com.googlecode.androidannotations.annotations.FragmentById;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.OptionsMenu;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;
import com.googlecode.androidannotations.api.SdkVersionHelper;

@SuppressLint("NewApi") @EFragment(R.layout.sagardoegun_detail_fragment)
@OptionsMenu(R.menu.sagardoegun_menu)
public class SagardoEgunDetailFragment extends SherlockFragment implements OnClickListener,OnItemClickListener{

	private static String TAG = "SagardoEgunDetailFragment";
	@Pref TxotxPrefs_ prefs;
	DLFileEntryRESTClient dlFileEntryRESTClient;
	SagardoEgunRESTClient sagardoEgunRESTClient;
	GertaeraRESTClient gertaeraRESTClient;
	
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
	@ViewById ListView gertaeraListView;
	List<Gertaera> gertaerak = new ArrayList<Gertaera>();
	GertaeraAdapter gertaeraAdapter = null;
	@FragmentArg
	SagardoEgun sagardoEgun;
	@FragmentById
	SupportMapFragment mapFragment;
	ImageLoader imgLoader;
	PlusClient mPlusClient;
	@ViewById(R.id.plus_one_button_sagardo_egun)
	PlusOneButton mPlusOneButton;
	Dialog dialog;
	Uri fileUri=null;
	ProgressDialog progressDialog;
	ProgressDialog imageProgressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        
		dlFileEntryRESTClient = new DLFileEntryRESTClient(new TxotxConnectionData(getSherlockActivity()));
		sagardoEgunRESTClient = new SagardoEgunRESTClient(new TxotxConnectionData(getSherlockActivity()));
		gertaeraRESTClient = new GertaeraRESTClient(new TxotxConnectionData(getSherlockActivity()));

		imgLoader = new ImageLoader(getActivity());
		
		PlusClient.Builder pcBuilder = new PlusClient.Builder(getActivity(),
				new PlusConnectionCallbacks(),
				new PlusOnConnectionFailedListener());
		
		mPlusClient = pcBuilder.clearScopes().build();
		mPlusClient.connect();
		
		
	}
	
	/*@Override
	public View onCreateView(android.view.LayoutInflater inflater, android.view.ViewGroup container, Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}*/
	
	@Override
	public void onStart() {
		super.onStart();
		//if(sagardoEgun!=null)
		//setSagardoEgunContent(sagardoEgun);
	}

	@Override
	public void onResume() {
		super.onResume();
		if(sagardoEgun!=null){
			setSagardoEgunContent(sagardoEgun);
		}
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
		mPlusClient.disconnect();
		super.onDestroyView();
	}

	
	public void setSagardoEgunContent(SagardoEgun sagardoEgun){
		this.sagardoEgun=sagardoEgun;
		mPlusOneButton.initialize(mPlusClient,
				"http://" + Constants.SERVICE_URL + "/Txotx-portlet/content?type=se&sagardoEgunId="+ sagardoEgun.getSagardoEgunId() , PLUS_ONE_REQUEST_CODE);
		
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
		getGertaerak(false);
	}
	
	@OptionsItem(R.id.menu_reload)
	void menuReload(){
		getGertaerak(true);
	}
	
	@OptionsItem(R.id.menu_image)
	void addImage(){
		showAddImageDialog();
	}
	
	@OptionsItem(R.id.menu_comment)
	void addComment() {
		showAddCommentDialog("");
	}
	
	@OptionsItem(R.id.menu_txoootx)
	void addBalorazio() {
		showAddBalorazioDialog();
	}
	
	@OptionsItem(R.id.menu_zabaldu)
	void menuZabaldu(){
		Intent shareIntent = new PlusShare.Builder(getActivity())
        .setType("text/plain")
        .setText(sagardoEgun.getIzena())
        .setContentUrl(Uri.parse("http://" + Constants.SERVICE_URL + "/Txotx-portlet/content?type=se&sagardoEgunId="+ sagardoEgun.getSagardoEgunId()))
        .setContentDeepLinkId("sagardoEgun"+sagardoEgun.getSagardoEgunId())
        .getIntent();

		startActivityForResult(shareIntent, 0);
		
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
	public void getGertaerak(boolean refresh){
		
		try {
			gertaerak = GertaeraCache.getGertaerak(sagardoEgun.getSagardoEgunId(), refresh);
			getGertaerakResult();
		} catch (Exception e) {
			Log.e(TAG, "Errorea gertaerak aldez aurretik kargatzen", e);
		}
	}
	
	@UiThread
	public void getGertaerakResult(){
		if(gertaerak!=null){
			gertaeraAdapter = new GertaeraAdapter(getActivity(), gertaerak, this);
			gertaeraListView.setAdapter(gertaeraAdapter);
			gertaeraListView.setOnItemClickListener(this);
		}
	}
	
	private void showAddImageDialog(){
		dialog = new Dialog(getActivity());
		dialog.setTitle("Irudi galeria edo kamera?");
		dialog.setContentView(R.layout.img_camera_dialog);
		dialog.setCanceledOnTouchOutside(true);
		
		TextView txImageMessage =(TextView)dialog.findViewById(R.id.txImageMessage);
		txImageMessage.setVisibility(View.VISIBLE);
	
		ImageView imgGallery = (ImageView)dialog.findViewById(R.id.imgGallery);
		imgGallery.setOnClickListener(this);
		ImageView imgCamera = (ImageView)dialog.findViewById(R.id.imgCamera);
		imgCamera.setOnClickListener(this);
		dialog.show();
	}
	
	public void showAddCommentDialog(String iruzkin){
		dialog = new Dialog(getActivity());
		dialog.setTitle("Iruzkinik?");
		dialog.setContentView(R.layout.iruzkin_dialog);
		dialog.setCanceledOnTouchOutside(true);
		
		Button btnComment = (Button)dialog.findViewById(R.id.btnComment);
		
		if(iruzkin!=null && !iruzkin.equals("")){
			EditText txIruzkin = (EditText)dialog.findViewById(R.id.txIruzkin);
			txIruzkin.setText(iruzkin + " ");
			txIruzkin.setSelection(txIruzkin.getText().length());
		}
		
		btnComment.setOnClickListener(this);
		dialog.show();
	}
	
	private void showAddBalorazioDialog(){
		dialog = new Dialog(getActivity());
		dialog.setTitle("Zure balorazioa?");
		dialog.setContentView(R.layout.balorazio_dialog);
		dialog.setCanceledOnTouchOutside(true);
		
		Button btnComment = (Button)dialog.findViewById(R.id.btnBalorazioa);
		btnComment.setOnClickListener(this);
		dialog.show();
	}
	
	@Override
	public void onClick(View v) {
		Intent intent;
		TextView txImageMessage =(TextView)dialog.findViewById(R.id.txImageMessage);

		int reqCode; 
		switch (v.getId()) {
		case R.id.imgGallery:

			reqCode=GET_IMG_FROM_GALLERY_ACTIVITY_REQUEST_CODE_FOR_COMMENT;
	
			intent = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(intent,
					reqCode);
			break;

		case R.id.imgCamera:

			reqCode=CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_FOR_COMMENT;

			intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			fileUri = Uri.fromFile(ImageUtils.getOutputTmpJpgFile());
			intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
			startActivityForResult(intent, reqCode);
			
			break;
			
		case R.id.btnComment:
			TextView txIruzkin = (TextView)dialog.findViewById(R.id.txIruzkin);
			String iruzkina = txIruzkin.getText().toString();
			if(iruzkina!=null && !iruzkina.equals("")){
				progressDialog = ProgressDialog.show(getSherlockActivity(), "", "Iruzkina bidaltzen...", true);
				progressDialog.show();
				gehituTestuGertaera(iruzkina);
			}
			else{
				Toast.makeText(getSherlockActivity(), "Komentariorik ez?! :(", Toast.LENGTH_SHORT).show();
			}
			break;
			
		case R.id.btnBalorazioa:
			RatingBar rating = (RatingBar)dialog.findViewById(R.id.rating);
			progressDialog = ProgressDialog.show(getSherlockActivity(), "", "Balorazioa bidaltzen...", true);
			progressDialog.show();
			gehituBalorazioGertaera(rating.getProgress());
			break;

		}
	}
	

	@Background
	void gehituArgazkiGertaera(DLFileEntry dlFileEntry, File file){
		TextView txImageMessage =(TextView)dialog.findViewById(R.id.txImageMessage);
		DLFileEntry dlFile = dlFileEntryRESTClient.addFileEntry(dlFileEntry, file);
		gehituArgazkiGertaeraResult(gertaeraRESTClient.gehituArgazkiGertaeraSagardoEgunean(sagardoEgun.getSagardoEgunId(), txImageMessage.getText().toString(), sagardoEgun.getIrudiKarpetaId(), dlFileEntry.getTitle()));
	}
	
	@UiThread
	void gehituArgazkiGertaeraResult(Gertaera gertaera){
		imageProgressDialog.cancel();
		
		Log.d(TAG, "gehituArgazkiGertaeraResult");
		try {
			if(gertaera!=null && gertaera.getGertaeraMota()!=null && gertaera.getGertaeraMota().equals(Constants.GERTAERA_MOTA_ARGAZKIA)){
				GertaeraCache.gehituGertaera(gertaera);
				getGertaerak(false);	
			}else{
				Toast.makeText(getSherlockActivity(), "Ezin izan da argazkia bidali! :(", Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			Log.e(TAG, "Errorea gehituArgazkiGertaeraResult");
		}
	}
	
	@Background
	void gehituTestuGertaera(String testua){
		gehituTestuGertaeraResult(gertaeraRESTClient.gehituTestuGertaeraSagardoEgunean(sagardoEgun.getSagardoEgunId(), testua));
	}
	
	@UiThread
	void gehituTestuGertaeraResult(Gertaera gertaera){
		progressDialog.cancel();
		if(gertaera!=null && gertaera.getGertaeraMota()!=null && gertaera.getGertaeraMota().equals(Constants.GERTAERA_MOTA_TESTUA)){
			dialog.cancel();
			GertaeraCache.gehituGertaera(gertaera);
			getGertaerak(false);		
		}else{
			Toast.makeText(getSherlockActivity(), "Ezin izan da iruzkina bidali! :(", Toast.LENGTH_SHORT).show();
		}
	}
	
	@Background
	void gehituBalorazioGertaera(int balorazioa){
		gehituBalorazioGertaeraResult(gertaeraRESTClient.gehituBalorazioGertaeraSagardoEgunean(sagardoEgun.getSagardoEgunId(), "", balorazioa));
	}
	
	@UiThread
	void gehituBalorazioGertaeraResult(Gertaera gertaera){
		progressDialog.cancel();
		if(gertaera!=null && gertaera.getGertaeraMota()!=null && gertaera.getGertaeraMota().equals(Constants.GERTAERA_MOTA_BALORAZIOA)){
			dialog.cancel();
			GertaeraCache.gehituGertaera(gertaera);
			getGertaerak(false);	
		}else{
			Toast.makeText(getSherlockActivity(), "Ezin izan da balorazioa bidali! :(", Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case GET_IMG_FROM_GALLERY_ACTIVITY_REQUEST_CODE_FOR_COMMENT:
			if (resultCode == getActivity().RESULT_OK) {
				
				imageProgressDialog = ProgressDialog.show(getSherlockActivity(), "", "Argazkia bidaltzen...", true, true);
				imageProgressDialog.show();
				
				Uri targetUri = data.getData();

	        	File dest = ImageUtils.getOutputMediaFile(String.valueOf(sagardoEgun.getSagardoEgunId()));

				try {
					Log.d(TAG, "Argazkia bidaltzen...");
					ImageUtils.copyInputStreamToFile(getActivity().getContentResolver().openInputStream(targetUri), dest);
					ImageUtils.resizeFile(dest);
					DLFileEntry dlFile = ImageUtils.composeDLFileEntry(sagardoEgun.getIrudiKarpetaId(), dest);
					
					gehituArgazkiGertaera(dlFile, dest);
				} catch (IOException e) {
					imageProgressDialog.cancel();
					Log.e(TAG, "Error gettig/copying or uploading file",e);
				}
			}
			dialog.cancel();
			break;
		case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_FOR_COMMENT:
			if (resultCode == getActivity().RESULT_OK) {
				
				imageProgressDialog = ProgressDialog.show(getSherlockActivity(), "", "Argazkia bidaltzen...", true);
				imageProgressDialog.show();
				
				
				fileUri = Uri.fromFile(ImageUtils.getOutputTmpJpgFile());
	        	File dest = ImageUtils.getOutputMediaFile(AccountUtil.getUserName(getSherlockActivity()));

	        	try {
					ImageUtils.copyInputStreamToFile(getSherlockActivity().getContentResolver().openInputStream(fileUri), dest);
					ImageUtils.resizeFile(dest);	
					
					DLFileEntry dlFile = ImageUtils.composeDLFileEntry(sagardoEgun.getIrudiKarpetaId(), dest);
					gehituArgazkiGertaera(dlFile, dest);
				} 
	        	catch (IOException e) {
	        		imageProgressDialog.cancel();
					Log.e(TAG, "Error gettig/copying or uploading file",e);
				}

	        }
			dialog.cancel();
			break;
		default:
			break;
		}
	}
	
	final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_FOR_COMMENT = 100;
	final int GET_IMG_FROM_GALLERY_ACTIVITY_REQUEST_CODE_FOR_COMMENT = 101;
	
	private static final int PLUS_ONE_REQUEST_CODE = 0;

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Gertaera gertaera = (Gertaera) view.getTag();
		if(gertaera.getGertaeraMota().equals(Constants.GERTAERA_MOTA_ARGAZKIA)){
			showArgazkiaDialog(gertaera);
		}
	}
	
	private void showArgazkiaDialog(Gertaera gertaera){
		dialog = new Dialog(getActivity(),R.style.Theme_Sherlock_Light);
		dialog.setContentView(R.layout.image_dialog);
		dialog.setCanceledOnTouchOutside(true);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd hh:mm");
		Date tmpDate = new Date(gertaera.getCreateDate());
		TextView txTitle =(TextView)dialog.findViewById(R.id.txTitle);
		txTitle.setText(gertaera.getScreenName() + " @ " + sdf.format(tmpDate));
		
		ImageView imgGertaera = (ImageView)dialog.findViewById(R.id.imgGertaera);
		imgLoader.displayImage(ImageUtils.getGertaeraImageUrl(gertaera), imgGertaera, R.drawable.ic_launcher);
		
		TextView txImageMessage =(TextView)dialog.findViewById(R.id.txImageMessage);
		txImageMessage.setText(gertaera.getTestua());
	
		dialog.show();
	}
}