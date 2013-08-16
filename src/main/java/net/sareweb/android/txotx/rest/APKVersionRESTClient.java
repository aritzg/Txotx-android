package net.sareweb.android.txotx.rest;

import net.sareweb.android.txotx.model.APKVersion;
import net.sareweb.lifedroid.rest.generic.LDRESTClient;

import org.springframework.http.HttpMethod;

import android.util.Log;

public class APKVersionRESTClient extends LDRESTClient<APKVersion> {

	public APKVersionRESTClient(TxotxConnectionData connectionData) {
		super(connectionData);
	}
	
	public APKVersion getLastAPKVersion(){
		String requestURL = getBaseURL() + "/get-last-apk-version";
		Log.d(TAG,"requestURL : " + requestURL);
		return run(requestURL, HttpMethod.GET);
	}
	
	@Override
	public String getPorltetContext() {
		return "Txotx-portlet";
	}

	@Override
	public String getModelName() {
		return "apkversion";
	}

}
