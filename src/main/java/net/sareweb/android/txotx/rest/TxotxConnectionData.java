package net.sareweb.android.txotx.rest;

import android.util.Log;
import net.sareweb.android.txotx.util.Constants;
import net.sareweb.android.txotx.util.TxotxPrefs_;
import net.sareweb.lifedroid.rest.ConnectionData;

public class TxotxConnectionData extends ConnectionData {

	private String user=null;
	private String pass=null;
	private static String TAG = "TxotxConnectionData";

	public TxotxConnectionData(TxotxPrefs_ prefs){
		user = prefs.email().get();
		if(user==null || "".equals(user))user=Constants.DEFAULT_EMAIL;
		pass = prefs.pass().get();
		if(pass==null || "".equals(pass))pass=Constants.DEFAULT_PASS;
		Log.d(TAG, user + "/" + pass);
	}

	@Override
	public String getProtocol() {
		return "http";
	}

	@Override
	public String getServerURL() {
		return Constants.SERVICE_URL;
	}

	@Override
	public String getPort() {
		return String.valueOf(Constants.SERVICE_PORT);
	}

	@Override
	public String getUser() {
		return user;
	}

	@Override
	public String getPass() {
		return pass;
	}

	@Override
	public String getDefaultUser() {
		return Constants.DEFAULT_USER;
	}

	@Override
	public String getDefaultPass() {
		return Constants.DEFAULT_PASS;
	}

	@Override
	public String getCompanyId() {
		return "10154";
	}

}