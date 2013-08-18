package net.sareweb.android.txotx.fragment;

import java.io.File;
import java.io.IOException;

import net.sareweb.android.txotx.R;
import net.sareweb.android.txotx.cache.UserCache;
import net.sareweb.android.txotx.image.ImageLoader;
import net.sareweb.android.txotx.model.Oharra;
import net.sareweb.android.txotx.rest.OharraRESTClient;
import net.sareweb.android.txotx.rest.TxotxConnectionData;
import net.sareweb.android.txotx.util.AccountUtil;
import net.sareweb.android.txotx.util.ImageUtils;
import net.sareweb.android.txotx.util.TxotxPrefs_;
import net.sareweb.lifedroid.model.DLFileEntry;
import net.sareweb.lifedroid.model.User;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.FragmentArg;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

@EFragment(R.layout.settings)
public class SettingsFragment extends SherlockFragment {

	private static final String TAG = "SettingsFragment";
	
	@ViewById(R.id.imgPortrait)
	ImageView imgPortrait;
	@ViewById(R.id.textView1)
	TextView textView1;
	ImageLoader imgLoader;
	Uri croppedImageUri;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		imgLoader = new ImageLoader(getSherlockActivity());
		getUser();
	}

	@Override
	public void onResume() {
		Log.d(TAG, "onResume");
		super.onResume();
		
	}
	
	@Background
	public void getUser(){
		User user = UserCache.getUser(AccountUtil.getGoogleEmail(getSherlockActivity()));
		if(user!=null){
			gotUser(user);
		}
	}
	
	@UiThread
	public void gotUser(User user){
		imgLoader.displayImage(ImageUtils.getPortraitImageUrl(user), imgPortrait, R.drawable.ic_launcher);
	}
	
	@Click(R.id.imgPortrait)
	void clickOnPortrait(){
		int reqCode=GET_IMG_FROM_GALLERY_ACTIVITY_REQUEST_CODE;
		Intent intent = new Intent(
				Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(intent,
				reqCode);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case GET_IMG_FROM_GALLERY_ACTIVITY_REQUEST_CODE:
			if (resultCode == getActivity().RESULT_OK) {
				Log.d(TAG, "Got image");
				/*textView1.setText("Got image");
				Uri imageUri = data.getData();
				imgPortrait.setImageURI(imageUri);*/
				//performCrop(imageUri);
			}
			
			break;
		case GET_IMG_FROM_CROP_ACTIVITY_REQUEST_CODE:
			
			/*Bundle extras = data.getExtras();
			Bitmap cropped = extras.getParcelable("data");*/
			
			croppedImageUri = Uri.fromFile(ImageUtils.getOutputTmpJpgFile());
			Log.d(TAG, "Image has been cropped " + croppedImageUri);
			imgPortrait.setImageDrawable(null);
			imgPortrait.setImageURI(croppedImageUri);
			imgPortrait.setVisibility(View.GONE);
			imgPortrait.invalidate();
			
			
			break;
		}
	
	}
	
	private void performCrop(Uri imageUri){
		try {
		    //call the standard crop action intent (the user device may not support it)
			Intent cropIntent = new Intent("com.android.camera.action.CROP");
			    //indicate image type and Uri
			cropIntent.setDataAndType(imageUri, "image/*");
			    //set crop properties
			cropIntent.putExtra("crop", "true");
			    //indicate aspect of desired crop
			cropIntent.putExtra("aspectX", 1);
			cropIntent.putExtra("aspectY", 1);
			    //indicate output X and Y
			cropIntent.putExtra("outputX", 256);
			cropIntent.putExtra("outputY", 256);
			    //retrieve data on return
			cropIntent.putExtra("return-data", true);
			
			croppedImageUri = Uri.fromFile(ImageUtils.getOutputTmpJpgFile());
			cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, croppedImageUri);
			
			    //start the activity - we handle returning in onActivityResult
			startActivityForResult(cropIntent, GET_IMG_FROM_CROP_ACTIVITY_REQUEST_CODE);
		}
		catch(ActivityNotFoundException anfe){
		    //display an error message
		    String errorMessage = "Zotz! Zure segapotoak ez du funtzio hau onartzen!";
		    Toast toast = Toast.makeText(getSherlockActivity(), errorMessage, Toast.LENGTH_SHORT);
		    toast.show();
		}
	}
	
	@Override
	public void onPause() {
		Log.d(TAG, "onPause");
		super.onPause();
	}
	

	
	final int GET_IMG_FROM_GALLERY_ACTIVITY_REQUEST_CODE = 101;
	final int GET_IMG_FROM_CROP_ACTIVITY_REQUEST_CODE = 102;

}
