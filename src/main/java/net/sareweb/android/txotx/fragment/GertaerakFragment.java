package net.sareweb.android.txotx.fragment;

import java.io.File;
import java.io.IOException;
import java.util.List;

import net.sareweb.android.txotx.R;
import net.sareweb.android.txotx.adapter.GertaeraAdapter;
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
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dlFileEntryRESTClient = new DLFileEntryRESTClient(new TxotxConnectionData(prefs));
		gertaeraRESTClient = new GertaeraRESTClient(new TxotxConnectionData(prefs));
	}

	@Override
	public void onResume() {
		Log.d(TAG,"onAttach");
		super.onResume();
		if(sagardotegi.getSagardotegiId()!=0)getGertaerak(sagardotegi.getSagardotegiId());
	}


	public void setGertaeraContent(long sagardotegiId){
		getGertaerak(sagardotegiId);
	}

	@Background
	void getGertaerak(long sagardotegiId){
		getGertaerakResult(gertaeraRESTClient.getGertaerakOlderThanDate(sagardotegiId, System.currentTimeMillis(), 100));
	}

	@UiThread
	void getGertaerakResult(List<Gertaera> gertaerak){
		if(gertaerak!=null){
			Log.d(TAG, "gertaerak " + gertaerak.size());
			ListView gertaeraListView = (ListView) getActivity().findViewById(R.id.gertaera_list_view);
			gertaeraListView.setAdapter(new GertaeraAdapter(getActivity(), gertaerak));
			gertaeraListView.setOnItemClickListener(this);
		}
	}
	
	@OptionsItem(R.id.menu_image)
	void addImage(){
		showAddImageDialog();
	}
	
	@OptionsItem(R.id.menu_comment)
	void addComment() {
		showAddCommentDialog();
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
			Uri fileUri = Uri.fromFile(new File(""));
			intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
			startActivityForResult(intent, reqCode);
			
			break;
			
		case R.id.btnComment:
			TextView txIruzkin = (TextView)dialog.findViewById(R.id.txIruzkin);
			progressDialog = ProgressDialog.show(getSherlockActivity(), "", "Iruzkina bidaltzen...", true);
			progressDialog.show();
			gehituTestuGertaera(txIruzkin.getText().toString());
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
					progressDialog = ProgressDialog.show(getSherlockActivity(), "", "Iruzkina bidaltzen...", true);
					progressDialog.show();
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

	@Background
	void gehituArgazkiGertaera(DLFileEntry dlFileEntry, File file){
		TextView txImageMessage =(TextView)dialog.findViewById(R.id.txImageMessage);
		DLFileEntry dlFile = dlFileEntryRESTClient.addFileEntry(dlFileEntry, file);
		gehituArgazkiGertaeraResult(gertaeraRESTClient.gehituArgazkiGertaera(sagardotegi.getSagardotegiId(), txImageMessage.getText().toString(), sagardotegi.getIrudiKarpetaId(), dlFileEntry.getTitle()));
	}
	
	@UiThread
	void gehituArgazkiGertaeraResult(Gertaera gertaera){
		progressDialog.cancel();
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
			//TODO: add gertaera to array adapter ?!?		
		}else{
			Toast.makeText(getSherlockActivity(), "Ezin izan da iruzkina bidali! :(", Toast.LENGTH_SHORT).show();
		}
	}
	
	final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_FOR_COMMENT = 100;
	final int GET_IMG_FROM_GALLERY_ACTIVITY_REQUEST_CODE_FOR_COMMENT = 101;
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

	}

}