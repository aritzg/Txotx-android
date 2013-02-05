package net.sareweb.android.txotx.fragment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sareweb.android.txotx.R;
import net.sareweb.android.txotx.activity.DashboardActivity_;
import net.sareweb.android.txotx.adapter.GertaeraAdapter;
import net.sareweb.android.txotx.cache.GertaeraCache;
import net.sareweb.android.txotx.image.ImageLoader;
import net.sareweb.android.txotx.model.Gertaera;
import net.sareweb.android.txotx.model.Sagardotegi;
import net.sareweb.android.txotx.rest.GertaeraRESTClient;
import net.sareweb.android.txotx.rest.TxotxConnectionData;
import net.sareweb.android.txotx.util.Constants;
import net.sareweb.android.txotx.util.ImageUtils;
import net.sareweb.android.txotx.util.TxotxPrefs_;
import net.sareweb.lifedroid.model.DLFileEntry;
import net.sareweb.lifedroid.rest.DLFileEntryRESTClient;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources.Theme;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.ActionBarSherlock;
import com.actionbarsherlock.app.SherlockFragment;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.FragmentArg;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.OptionsMenu;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

@EFragment(R.layout.gertaera_fragment)
@OptionsMenu(R.menu.sagardotegi_menu)
public class GertaerakFragment extends SherlockFragment implements OnItemClickListener,OnClickListener{

	GertaeraRESTClient gertaeraRESTClient;

	private static String TAG = "GertaerakFragment";
	@Pref TxotxPrefs_ prefs;
	@FragmentArg
	Sagardotegi sagardotegi;
	Dialog dialog;
	ProgressDialog progressDialog;
	DLFileEntryRESTClient dlFileEntryRESTClient;
	String imageMessage="";
	Uri fileUri=null;
	long azkenGertaerarenData=0;
	GertaeraAdapter gertaeraAdapter = null;
	ListView gertaeraListView;
	List<Gertaera> gertaerak = new ArrayList<Gertaera>();
	ImageLoader imgLoader;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dlFileEntryRESTClient = new DLFileEntryRESTClient(new TxotxConnectionData(prefs));
		gertaeraRESTClient = new GertaeraRESTClient(new TxotxConnectionData(prefs));
		imgLoader = new ImageLoader(getActivity());
	}

	@Override
	public void onResume() {
		Log.d(TAG,"onAttach");
		super.onResume();
		if(sagardotegi.getSagardotegiId()!=0){
			load(true);
		}
	}

	void load(boolean all){
		showGertaerakKargatzen();
		if(all){
			getGertaerak();
		}
		else{
			getGertaeraBerriagoak();
		}
	}

	@Background
	void getGertaerak(){
		//this.gertaerak=gertaeraRESTClient.getGertaerakOlderThanDate(sagardotegi.getSagardotegiId(), 0, 100);
		this.gertaerak=GertaeraCache.getSagardotegiGertaerak(sagardotegi.getSagardotegiId(), false);
		getGertaerakResult();
	}

	@UiThread
	void getGertaerakResult(){
		if(gertaerak!=null){
			if(gertaerak.size()>0){
				azkenGertaerarenData = gertaerak.get(0).getCreateDate(); 
			}
			gertaeraListView = (ListView) getActivity().findViewById(R.id.gertaera_list_view);
			gertaeraAdapter = new GertaeraAdapter(getActivity(), gertaerak);
			gertaeraListView.setAdapter(gertaeraAdapter);
			gertaeraListView.setOnItemClickListener(this);
		}
		progressDialog.cancel();
	}
	
	@Background
	void getGertaeraBerriagoak(){
		Log.d(TAG, "Gertaera zerrenda eguneratzen");
		try {
			//getGertaeraBerriagoakResult(gertaeraRESTClient.getGertaerakNewerThanDate(sagardotegi.getSagardotegiId(), azkenGertaerarenData, 100));
			getGertaeraBerriagoakResult(GertaeraCache.getSagardotegiGertaerak(sagardotegi.getSagardotegiId(), true));
		} catch (Exception e) {
			getGertaeraBerriagoakResult(null);
		}
		
	}

	@UiThread
	void getGertaeraBerriagoakResult(List<Gertaera> gertaeraBerriak){
		if(gertaeraBerriak!=null && gertaeraBerriak.size()>0){
			azkenGertaerarenData = gertaeraBerriak.get(0).getCreateDate();
			gertaerak.addAll(0,gertaeraBerriak);
			gertaeraAdapter.notifyDataSetChanged();
		}
		progressDialog.cancel();
	}
	
	@OptionsItem({R.id.menu_home,android.R.id.home})
	void homeSelected() {
		DashboardActivity_.intent(getSherlockActivity()).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP).start();
	}
	
	@OptionsItem(R.id.menu_reload)
	void menuReload(){
		load(false);
	}
	
	
	@OptionsItem(R.id.menu_image)
	void addImage(){
		showAddImageDialog();
	}
	
	@OptionsItem(R.id.menu_comment)
	void addComment() {
		showAddCommentDialog();
	}
	
	@OptionsItem(R.id.menu_txoootx)
	void addBalorazio() {
		showAddBalorazioDialog();
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
			fileUri = Uri.fromFile(ImageUtils.getOutputMediaFile(prefs.user().get()));
			intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
			startActivityForResult(intent, reqCode);
			
			break;
			
		case R.id.btnComment:
			TextView txIruzkin = (TextView)dialog.findViewById(R.id.txIruzkin);
			progressDialog = ProgressDialog.show(getSherlockActivity(), "", "Iruzkina bidaltzen...", true);
			progressDialog.show();
			gehituTestuGertaera(txIruzkin.getText().toString());
			break;
			
		case R.id.btnBalorazioa:
			RatingBar rating = (RatingBar)dialog.findViewById(R.id.rating);
			progressDialog = ProgressDialog.show(getSherlockActivity(), "", "Balorazioa bidaltzen...", true);
			progressDialog.show();
			gehituBalorazioGertaera(rating.getProgress());
			break;

		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case GET_IMG_FROM_GALLERY_ACTIVITY_REQUEST_CODE_FOR_COMMENT:
			if (resultCode == getActivity().RESULT_OK) {
				Uri targetUri = data.getData();

	        	File dest = ImageUtils.getOutputMediaFile(String.valueOf(sagardotegi.getSagardotegiId()));

				try {
					Log.d(TAG, "Argazkia bidaltzen...");
					ImageUtils.copyInputStreamToFile(getActivity().getContentResolver().openInputStream(targetUri), dest);
					ImageUtils.resizeFile(dest);
					DLFileEntry dlFile = ImageUtils.composeDLFileEntry(sagardotegi, dest);
					gehituArgazkiGertaera(dlFile, dest);
				} catch (IOException e) {
					progressDialog.cancel();
					Log.e(TAG, "Error gettig/copying or uploading file",e);
				}
			}
			dialog.cancel();
			break;
		case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_FOR_COMMENT:
			if (resultCode == getActivity().RESULT_OK) {

	        	File dest = ImageUtils.getOutputMediaFile(prefs.user().get());

	        	try {
					ImageUtils.copyInputStreamToFile(getSherlockActivity().getContentResolver().openInputStream(fileUri), dest);
					ImageUtils.resizeFile(dest);

					DLFileEntry dlFile = ImageUtils.composeDLFileEntry(sagardotegi, dest);
					gehituArgazkiGertaera(dlFile, dest);
				} 
	        	catch (IOException e) {
	        		progressDialog.cancel();
					Log.e(TAG, "Error gettig/copying or uploading file",e);
				}

	        }
			dialog.cancel();
			break;
		default:
			break;
		}
	}
	
	private void showAddImageDialog(){
		dialog = new Dialog(getActivity());
		dialog.setTitle("Gallery or Camera?");
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
	
	private void showAddCommentDialog(){
		dialog = new Dialog(getActivity());
		dialog.setTitle("Iruzkinik?");
		dialog.setContentView(R.layout.iruzkin_dialog);
		dialog.setCanceledOnTouchOutside(true);
		
		Button btnComment = (Button)dialog.findViewById(R.id.btnComment);
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
	
	private void showGertaerakKargatzen(){
		progressDialog = ProgressDialog.show(getSherlockActivity(), "", "Gertaerak kargatzen...", true);
		progressDialog.show();
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
	
	

	@Background
	void gehituArgazkiGertaera(DLFileEntry dlFileEntry, File file){
		TextView txImageMessage =(TextView)dialog.findViewById(R.id.txImageMessage);
		DLFileEntry dlFile = dlFileEntryRESTClient.addFileEntry(dlFileEntry, file);
		gehituArgazkiGertaeraResult(gertaeraRESTClient.gehituArgazkiGertaera(sagardotegi.getSagardotegiId(), txImageMessage.getText().toString(), sagardotegi.getIrudiKarpetaId(), dlFileEntry.getTitle()));
	}
	
	@UiThread
	void gehituArgazkiGertaeraResult(Gertaera gertaera){
		Log.d(TAG, "gehituArgazkiGertaeraResult");
		load(false);
	}
	
	@Background
	void gehituTestuGertaera(String testua){
		gehituTestuGertaeraResult(gertaeraRESTClient.gehituTestuGertaera(sagardotegi.getSagardotegiId(), testua));
	}
	
	@UiThread
	void gehituTestuGertaeraResult(Gertaera gertaera){
		progressDialog.cancel();
		if(gertaera!=null && gertaera.getGertaeraMota()!=null && gertaera.getGertaeraMota().equals(Constants.GERTAERA_MOTA_TESTUA)){
			dialog.cancel();
			load(false);	
		}else{
			Toast.makeText(getSherlockActivity(), "Ezin izan da iruzkina bidali! :(", Toast.LENGTH_SHORT).show();
		}
	}
	
	@Background
	void gehituBalorazioGertaera(int balorazioa){
		gehituBalorazioGertaeraResult(gertaeraRESTClient.gehituBalorazioGertaera(sagardotegi.getSagardotegiId(), "", balorazioa));
	}
	
	@UiThread
	void gehituBalorazioGertaeraResult(Gertaera gertaera){
		progressDialog.cancel();
		if(gertaera!=null && gertaera.getGertaeraMota()!=null && gertaera.getGertaeraMota().equals(Constants.GERTAERA_MOTA_BALORAZIOA)){
			dialog.cancel();
			load(false);		
		}else{
			Toast.makeText(getSherlockActivity(), "Ezin izan da balorazioa bidali! :(", Toast.LENGTH_SHORT).show();
		}
	}
	
	
	
	final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_FOR_COMMENT = 100;
	final int GET_IMG_FROM_GALLERY_ACTIVITY_REQUEST_CODE_FOR_COMMENT = 101;
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Gertaera gertaera = (Gertaera) view.getTag();
		if(gertaera.getGertaeraMota().equals(Constants.GERTAERA_MOTA_ARGAZKIA)){
			showArgazkiaDialog(gertaera);
		}
	}

}