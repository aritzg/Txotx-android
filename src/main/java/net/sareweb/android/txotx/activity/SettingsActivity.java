package net.sareweb.android.txotx.activity;

import java.io.ByteArrayOutputStream;

import net.sareweb.android.txotx.R;
import net.sareweb.android.txotx.cache.UserCache;
import net.sareweb.android.txotx.image.ImageLoader;
import net.sareweb.android.txotx.rest.TxotxConnectionData;
import net.sareweb.android.txotx.util.AccountUtil;
import net.sareweb.android.txotx.util.ImageUtils;
import net.sareweb.lifedroid.model.User;
import net.sareweb.lifedroid.rest.UserRESTClient;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;

@EActivity(R.layout.settings)
public class SettingsActivity extends SherlockActivity {

	private static String TAG = "SettingsActivity";
	@ViewById(R.id.imgPortrait)
	ImageView imgPortrait;
	@ViewById(R.id.btnPortrait)
	Button btnPortrait;
	ImageLoader imgLoader;
	boolean changed=false;
	ActionBar actionBar;
	UserRESTClient userRESTClient; 
	byte[] imageBytes;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");

		actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		imgLoader = new ImageLoader(this);
		getUser();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "onResume");
		userRESTClient = new UserRESTClient(new TxotxConnectionData(this));
	}
	

	@OptionsItem(android.R.id.home)
	void homeSelected() {
		finish();
	}
	
	@Background
	public void getUser(){
		User user = UserCache.getUser(AccountUtil.getGoogleEmail(this));
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
	
	@Click(R.id.btnPortrait)
	void clickOnBtnPortrait(){
		if(changed){
			updatePortrait();
		}
		else{
			clickOnPortrait();
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case GET_IMG_FROM_GALLERY_ACTIVITY_REQUEST_CODE:
			if (resultCode == this.RESULT_OK) {
				Log.d(TAG, "Got image");
				Uri imageUri = data.getData();
				performCrop(imageUri);
			}
			
			break;
		case GET_IMG_FROM_CROP_ACTIVITY_REQUEST_CODE:
			
			Bundle extras = data.getExtras();
			Bitmap cropped = extras.getParcelable("data");
			imgPortrait.setImageBitmap(cropped);
			
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			cropped.compress(Bitmap.CompressFormat.PNG, 100, stream);
			imageBytes = stream.toByteArray();
			
			changed=true;
			btnPortrait.setText("Gorde aldaketak");
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
			
			//start the activity - we handle returning in onActivityResult
			startActivityForResult(cropIntent, GET_IMG_FROM_CROP_ACTIVITY_REQUEST_CODE);
		}
		catch(ActivityNotFoundException anfe){
		    //display an error message
		    String errorMessage = "Zotz! Zure segapotoak ez du funtzio hau onartzen!";
		    Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
		    toast.show();
		}
	}
	
	@Background
	public void updatePortrait(){
		Log.d(TAG, "Uploading portrait");
		userRESTClient.updatePortrait(UserCache.getUser(AccountUtil.getGoogleEmail(this)).getUserId(), imageBytes);
	}
	
	@UiThread
	public void portraitUpdated(){
		
	}
	
	final int GET_IMG_FROM_GALLERY_ACTIVITY_REQUEST_CODE = 101;
	final int GET_IMG_FROM_CROP_ACTIVITY_REQUEST_CODE = 102;
	
}
