package net.sareweb.android.txotx.rest;

import net.sareweb.android.txotx.model.Oharra;
import net.sareweb.lifedroid.rest.generic.LDRESTClient;

import org.springframework.http.HttpMethod;

import android.util.Log;

public class TxootxRESTClient extends LDRESTClient<Oharra> {

	public TxootxRESTClient(TxotxConnectionData connectionData) {
		super(connectionData);
	}
	
	public boolean updateScreenName(long userId, String screenName){
		String requestURL = getBaseURL() + "/update-screen-name";
		requestURL = addParamToRequestURL(requestURL, "user-id", userId);
		requestURL = addParamToRequestURL(requestURL, "screen-name", screenName);
		Log.d(TAG,"requestURL : " + requestURL);
		return runForBoolean(requestURL, HttpMethod.GET);
	}
	
	
	@Override
	public String getPorltetContext() {
		return "Txotx-portlet";
	}

	@Override
	public String getModelName() {
		return "txootx";
	}

}
