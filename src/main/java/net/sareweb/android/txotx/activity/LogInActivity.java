package net.sareweb.android.txotx.activity;

import net.sareweb.android.txotx.R;
import net.sareweb.android.txotx.cache.GertaeraCache;
import net.sareweb.android.txotx.cache.SagardotegiCache;
import net.sareweb.android.txotx.cache.UserCache;
import net.sareweb.android.txotx.rest.SagardotegiRESTClient;
import net.sareweb.android.txotx.rest.TxotxConnectionData;
import net.sareweb.android.txotx.util.Constants;
import net.sareweb.android.txotx.util.TxotxPrefs_;
import net.sareweb.android.txotx.util.ConnectionUtils;
import net.sareweb.android.txotx.util.PrefUtils;
import net.sareweb.android.txotx.util.UserUtils;
import net.sareweb.lifedroid.model.User;
import net.sareweb.lifedroid.rest.UserRESTClient;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

@EActivity(R.layout.log_in)
public class LogInActivity extends Activity implements OnClickListener{
	
	private static String TAG = "LogInActivity";
	@ViewById TextView txEmailAddress;
	@ViewById EditText txPass;
	@Pref TxotxPrefs_ prefs;
	UserRESTClient userRESTClient;
	SagardotegiRESTClient sagardotegiRESTClient;
	private ProgressDialog dialog;
	private Dialog registerDialog;
	private Dialog resetDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(!ConnectionUtils.isOnline(this)){
			finish();
			NoConnActivity_.intent(this).start();
			return;
		}
		
		if(PrefUtils.isUserLogged(prefs)){
			registerDevice();
			finish();
			DashboardActivity_.intent(this).redirect(true).start();
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		AccountManager am = AccountManager.get(this);
		Account[] accounts = am.getAccountsByType("com.google");

		if(accounts!=null && accounts.length > 0){
			Account account = (Account)accounts[0];
			txEmailAddress.setText(account.name);
		}
		else{
			Toast.makeText(this, "Mugikorrak ez du Google konturik esleiturik!", Toast.LENGTH_LONG).show();
		}
	}
	
	@Click(R.id.btnSignIn)
	void clickOnSignIn(){
		if(ConnectionUtils.isOnline(this)){
			dialog = ProgressDialog.show(this, "", "Sartzen...", true);
			dialog.show();
			validateUser();
		}
		else{
			Toast.makeText(this, "Ez dago internetara sarbiderik!!", Toast.LENGTH_SHORT).show();
		}
	}
	
	@Click(R.id.btnCreateAccount)
	void clickOnCreateAccount(){
		AccountManager am = AccountManager.get(this);
		Account[] accounts = am.getAccountsByType("com.google");

		if(accounts!=null && accounts.length > 0){
			Account account = (Account)accounts[0];
			registerDialog = new Dialog(this);
			registerDialog.requestWindowFeature(Window.FEATURE_LEFT_ICON);
			registerDialog.setTitle("Kontua sortu");
			registerDialog.setContentView(R.layout.register_dialog);
			TextView txEmailAddress = (TextView)registerDialog.findViewById(R.id.txEmailAddress);
			txEmailAddress.setText(account.name);
			prefs.email().put(account.name);
			registerDialog.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_launcher);
			registerDialog.setCanceledOnTouchOutside(true);
			Button createAccountButton = (Button)registerDialog.findViewById(R.id.btnCreateAccount);
			createAccountButton.setOnClickListener(this);
			registerDialog.show();
		}
		else{
			Toast.makeText(this, "Mugikorrak ez du Google konturik esleiturik!", Toast.LENGTH_LONG).show();
		}
		
	}
	
	@Click(R.id.btnResetPass)
	void clickOnResetPass(){
		AccountManager am = AccountManager.get(this);
		Account[] accounts = am.getAccountsByType("com.google");

			if(accounts!=null && accounts.length > 0){
				Account account = (Account)accounts[0];
				resetDialog = new Dialog(this);
				resetDialog.requestWindowFeature(Window.FEATURE_LEFT_ICON);
				resetDialog.setTitle("Pasahitza berrezarri");
				resetDialog.setContentView(R.layout.pass_reset_dialog);
				TextView txEmailAddress = (TextView)resetDialog.findViewById(R.id.txEmailAddress);
				txEmailAddress.setText(account.name);
				resetDialog.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_launcher);
				resetDialog.setCanceledOnTouchOutside(true);
				Button btnResetConfirm = (Button)resetDialog.findViewById(R.id.btnResetConfirm);
				btnResetConfirm.setOnClickListener(this);
				resetDialog.show();
		}
	}
	
	
	@Background
	void validateUser(){
		if(txPass.getText().toString().equals("")){
			validateUserResult(null, null);
		}
		else{
			prefs.email().put(txEmailAddress.getText().toString());
			prefs.pass().put(txPass.getText().toString());
			userRESTClient = new UserRESTClient(new TxotxConnectionData(prefs));
			User user = userRESTClient.getUserByEmailAddress(txEmailAddress.getText().toString());
			
			validateUserResult(user, txPass.getText().toString());
		}
	}
	
	@UiThread
	void validateUserResult(User user, String pass){
		if(UserUtils.isEmptyUser(user)){
			prefs.user().put("");
			prefs.pass().put("");
			prefs.userId().put(0);
			Toast.makeText(this, "Erabiltzaile edo pasahitza ez dira zuzenak!", Toast.LENGTH_SHORT).show();
		}
		else{
			Log.d(TAG, "got user email:" + user.getEmailAddress() + " screenName:" + user.getScreenName());
			loginUser(user, pass);
			registerDevice();
			finish();
			DashboardActivity_.intent(this).redirect(true).start();
			//SagardotegiakActivity_.intent(this).start();
		}
		dialog.cancel();
	}
	
	private void loginUser(User user, String pass){
		prefs.email().put(user.getEmailAddress());
		prefs.user().put(user.getScreenName());
		prefs.pass().put(pass);
		prefs.userId().put(user.getUserId());
	}


	@Override
	public void onClick(View v) {
		if(ConnectionUtils.isOnline(this)){
			switch (v.getId()) {
			case R.id.btnCreateAccount:
				EditText txName = (EditText)registerDialog.findViewById(R.id.txName);
				EditText txSurname = (EditText)registerDialog.findViewById(R.id.txSurname);
				TextView txEmailAddress = (TextView)registerDialog.findViewById(R.id.txEmailAddress);
				EditText txUserName = (EditText)registerDialog.findViewById(R.id.txUserName);
				EditText txPass1 = (EditText)registerDialog.findViewById(R.id.txPass1);
				EditText txPass2 = (EditText)registerDialog.findViewById(R.id.txPass2);
				if(validRegisterForm(txEmailAddress.getText().toString(), txName.getText().toString(), txSurname.getText().toString(),txUserName.getText().toString(), txPass1.getText().toString(), txPass2.getText().toString())){
					registerDialog.cancel();
					dialog = ProgressDialog.show(this, "", "Kontua sortzen...", true);
					dialog.show();
					createAccount(txEmailAddress.getText().toString(), txName.getText().toString(), txSurname.getText().toString(), txUserName.getText().toString(), txPass1.getText().toString());
				}
				break;
			case R.id.btnResetConfirm:
				sagardotegiRESTClient = new SagardotegiRESTClient(new TxotxConnectionData());
				TextView txEmailAddress2 = (TextView)resetDialog.findViewById(R.id.txEmailAddress);
				resetPass(txEmailAddress2.getText().toString());
				resetDialog.cancel();
				break;	
			

			default:
				break;
			}
			
			
		}
		else{
			Toast.makeText(this, "Ez dago internetara sarbiderik!!", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	private boolean validRegisterForm(String emailAddress, String name, String surname, String userName, String pass1, String pass2) {
		if(name.equals("")){
			Toast.makeText(this, "Izena beharrezkoa da!", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(surname.equals("")){
			Toast.makeText(this, "Abizena beharrezkoa da!", Toast.LENGTH_SHORT).show();
			return false;
		}
		
		if(!android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()){
			Toast.makeText(this, "Not valid email address!", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(userName.equals("")){
			Toast.makeText(this, "Erabiltzaile izena beharrezkoa da!", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(pass1.length()==0){
			Toast.makeText(this, "Pasahitz huts ez da onartzen!", Toast.LENGTH_SHORT).show();
			return false;			
		}
		if(!pass1.equals(pass2.toString())){
			Toast.makeText(this, "Pasahitzak ez dira berdinak!", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	
	@Background
	void createAccount(String emailAddress, String name, String surname, String userName, String pass){
		PrefUtils.clearUserPrefs(prefs);
		userRESTClient = new UserRESTClient(new TxotxConnectionData(prefs));
		User user = userRESTClient.getUserByEmailAddress(emailAddress);
		if(!UserUtils.isEmptyUser(user)){//Retrieve user
			createAccountResult(USER_EXISTS_1);
			return;
		}
		user = userRESTClient.getUserByScreenName(userName);
		if(!UserUtils.isEmptyUser(user)){//Retrieve user
			createAccountResult(USER_EXISTS_2);
			return;
		}
		user = userRESTClient.addUser(
				false, 
				pass, 
				pass,
				false,
				userName,
				emailAddress,
				0,
				"openId",
				"es_ES",
				name,
				null,
				surname,
				1,
				1,
				true,
				1,
				1,
				2000,
				null,
				null,
				null,
				null,
				null,
				true);
		if(!UserUtils.isEmptyUser(user)){
			//When creating users email address is not returned??
			user.setEmailAddress(emailAddress);
			loginUser(user, pass);
			Log.d(TAG, prefs.email().get()  + "/" + prefs.pass().get());
			createAccountResult(REGISTER_OK);
		}
		else{
			createAccountResult(REGISTER_ERROR);
		}
	}
	
	@UiThread
	void createAccountResult(int result){
		switch (result) {
		case USER_EXISTS_1:
			Toast.makeText(this, "Dagoeneko existitzen da erabiltzaile bat email honekin!", Toast.LENGTH_LONG).show();
			dialog.cancel();
			break;
		case USER_EXISTS_2:
			Toast.makeText(this, "Erabiltzaile izena hartuta dago. Aukeratu beste erabiltzaile izen bat mesedez!", Toast.LENGTH_LONG).show();
			dialog.cancel();
			break;
		case REGISTER_OK:
			Toast.makeText(this, "Erabiltzailea sortua!!", Toast.LENGTH_LONG).show();
			dialog.cancel();
			registerDevice();
			finish();
			DashboardActivity_.intent(this).redirect(true).start();
			//SagardotegiakActivity_.intent(this).start();
			break;
		case REGISTER_ERROR:
			Toast.makeText(this, "Errorea gertatu da erabiltzailea sortzerakoan! :(", Toast.LENGTH_LONG).show();
			dialog.cancel();
			break;
		}
	}
	
	@Background
	void resetPass(String emailAddress){
		sagardotegiRESTClient.resetPassword(emailAddress);
		resetPassResult();
	}
	
	@UiThread
	void resetPassResult(){
		Toast.makeText(this, "Pasahitz berria zure emailera bidaliko da.", Toast.LENGTH_LONG).show();
	}
	
	private void registerDevice() {
		Intent registrationIntent = new Intent("com.google.android.c2dm.intent.REGISTER");
		// sets the app name in the intent
		registrationIntent.putExtra("app", PendingIntent.getBroadcast(this, 0, new Intent(), 0));
		registrationIntent.putExtra("sender", Constants.SENDER_ID);
		startService(registrationIntent);
	}

	private static final int REGISTER_OK = 0;
	private static final int USER_EXISTS_1 = 1;
	private static final int USER_EXISTS_2 = 2;
	private static final int REGISTER_ERROR = 3;

}