package net.sareweb.android.txotx.activity;

import net.sareweb.android.txotx.R;
import net.sareweb.android.txotx.cache.SagardotegiCache;
import net.sareweb.android.txotx.cache.UserCache;
import net.sareweb.android.txotx.rest.TxotxConnectionData;
import net.sareweb.android.txotx.util.AccountUtil;
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
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

@EActivity(R.layout.settings)
public class SettingsActivity extends SherlockFragmentActivity implements OnClickListener{
	
	private static String TAG = "SettingsActivity";
	ActionBar actionBar;
	private Dialog passDialog;
	private ProgressDialog dialog;
	@Pref TxotxPrefs_ prefs;
	UserRESTClient userRESTClient;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayHomeAsUpEnabled(true);
	}
	
	@OptionsItem(android.R.id.home)
	void homeSelected() {
		finish();
	}
	
	@Click(R.id.btnPasahitza)
	public void clickOnPasahitza(){
		passDialog = new Dialog(this);
		passDialog.requestWindowFeature(Window.FEATURE_LEFT_ICON);
		passDialog.setTitle("Pasahitz berria");
		passDialog.setContentView(R.layout.update_pass_dialog);
		Button btnUpdatePass = (Button)passDialog.findViewById(R.id.btnUpdatePass);
		btnUpdatePass.setOnClickListener(this);
		passDialog.show();
	}

	@Override
	public void onClick(View v) {
		dialog = ProgressDialog.show(this, "", "Pasahitza aldatzen", true);
		dialog.show();
		EditText txPass1 = (EditText)passDialog.findViewById(R.id.txPass1);
		EditText txPass2 = (EditText)passDialog.findViewById(R.id.txPass2);
		String pass1=txPass1.getText().toString();
		String pass2=txPass2.getText().toString();
		if(pass1.length()==0){
			Toast.makeText(this, "Pasahitz hutsa ez da onartzen!", Toast.LENGTH_SHORT).show();
			dialog.cancel();
			return;			
		}
		if(!pass1.equals(pass2.toString())){
			Toast.makeText(this, "Bi pasahitzak ez dira berdinak", Toast.LENGTH_SHORT).show();
			dialog.cancel();
			return;
		}
		//pasahitzaEguneratu(pass1, pass2);
	}
	
	/*@Background
	public void pasahitzaEguneratu(String pass1, String pass2){
		if(userRESTClient==null){
			userRESTClient = new UserRESTClient(new TxotxConnectionData(this));
		}
		pasahitzaEguneratuResult(userRESTClient.updatePassword(prefs.userId().get(), pass1, pass2, false), pass1);
	}*/
	
	@UiThread
	public void pasahitzaEguneratuResult(User user, String pass){
		if(!UserUtils.isEmptyUser(user)){
			loginUser(user, pass);
			Toast.makeText(this, "Pasahitza berria ezarri da.", Toast.LENGTH_SHORT).show();
			dialog.cancel();
			passDialog.cancel();
		}
		else{
			Toast.makeText(this, "Errorea gertatu da pasahitza eguneratzerakoan.", Toast.LENGTH_SHORT).show();
			dialog.cancel();
		}
	}
	
	private void loginUser(User user, String pass){
		/*prefs.email().put(user.getEmailAddress());
		prefs.user().put(user.getScreenName());
		prefs.pass().put(pass);
		prefs.userId().put(user.getUserId());*/
	}
}