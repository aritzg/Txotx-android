package net.sareweb.android.txotx.activity;

import java.io.ByteArrayOutputStream;

import org.apache.commons.lang.StringUtils;

import net.sareweb.android.txotx.R;
import net.sareweb.android.txotx.cache.UserCache;
import net.sareweb.android.txotx.image.ImageLoader;
import net.sareweb.android.txotx.rest.TxootxRESTClient;
import net.sareweb.android.txotx.rest.TxotxConnectionData;
import net.sareweb.android.txotx.util.AccountUtil;
import net.sareweb.android.txotx.util.ImageUtils;
import net.sareweb.lifedroid.model.User;
import net.sareweb.lifedroid.rest.UserRESTClient;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;

@EActivity(R.layout.settings)
public class SettingsActivity extends SherlockActivity{

	private static String TAG = "SettingsActivity";
	@ViewById(R.id.txScreenName)
	EditText txScreenName;
	@ViewById(R.id.imgPortrait)
	ImageView imgPortrait;
	@ViewById(R.id.btnPortrait)
	Button btnPortrait;
	ImageLoader imgLoader;
	boolean imageChanged=false;
	String originalName;
	ActionBar actionBar;
	UserRESTClient userRESTClient;
	TxootxRESTClient txootxRESTClient; 
	byte[] imageBytes;
	private ProgressDialog dialog;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");

		actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		imgLoader = new ImageLoader(this);
		
	}
	
	@AfterViews
	public void afterViews(){
		getUser();
		originalName = txScreenName.getText().toString();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "onResume");
		userRESTClient = new UserRESTClient(new TxotxConnectionData(this));
		txootxRESTClient  = new TxootxRESTClient(new TxotxConnectionData(this));
	}
	

	@OptionsItem(android.R.id.home)
	void homeSelected() {
		finish();
	}
	
	@Background
	public void getUser(){
		boolean refresh = true;
		User user = UserCache.getUser(AccountUtil.getGoogleEmail(this), refresh);
		if(user!=null){
			gotUser(user);
		}
	}
	
	@UiThread
	public void gotUser(User user){
		txScreenName.setText(user.getScreenName());
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
		dialog = ProgressDialog.show(this, "", "Datuak gordetzen...", true);
		updateData();
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
			
			imageChanged=true;
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
	public void updateData(){
		String newName = txScreenName.getText().toString();
		if(imageChanged){
			Log.d(TAG, "Uploading portrait");
			//Delete portrait so image id, and thus its url will change, to avoid cached file
			userRESTClient.deletePortrait(UserCache.getUser(AccountUtil.getGoogleEmail(this)).getUserId());
			userRESTClient.updatePortrait(UserCache.getUser(AccountUtil.getGoogleEmail(this)).getUserId(), imageBytes);
		}
		if(originalName!=null && !StringUtils.isEmpty(newName) && !originalName.equals(newName)){
			Log.d(TAG, "Updating screen name");
			txootxRESTClient.updateScreenName(UserCache.getUser(AccountUtil.getGoogleEmail(this)).getUserId(), newName);
		}
		dataUpdated();
	}
	
	@UiThread
	public void dataUpdated(){
		dialog.dismiss();
		Toast.makeText(this, "Zure profilaren datuak eguneratu dira.", Toast.LENGTH_SHORT).show();
		finish();
		SettingsActivity_.intent(this).start();
	}
	
	
	final int GET_IMG_FROM_GALLERY_ACTIVITY_REQUEST_CODE = 101;
	final int GET_IMG_FROM_CROP_ACTIVITY_REQUEST_CODE = 102;

	
	
}
