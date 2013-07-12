package net.sareweb.android.txotx.rest;

import android.content.Context;
import android.util.Log;
import net.sareweb.android.txotx.util.AccountUtil;
import net.sareweb.android.txotx.util.Constants;
import net.sareweb.android.txotx.util.TxotxPrefs_;
import net.sareweb.lifedroid.rest.ConnectionData;

public class TxotxConnectionData extends ConnectionData {

	private String user=null;
	private String pass=null;
	private static String TAG = "TxotxConnectionData";

	public TxotxConnectionData(Context context){
		this.user=AccountUtil.getGoogleEmail(context);
		this.pass=AccountUtil.getGoogleAuthToken(context);
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
	public String getCompanyId() {
		return "10154";
	}

}