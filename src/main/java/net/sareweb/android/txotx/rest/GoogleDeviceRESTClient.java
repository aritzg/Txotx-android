package net.sareweb.android.txotx.rest;

import net.sareweb.android.txotx.model.GoogleDevice;
import net.sareweb.lifedroid.rest.generic.LDRESTClient;

import org.springframework.http.HttpMethod;

import android.util.Log;

public class GoogleDeviceRESTClient extends LDRESTClient<GoogleDevice> {

	public GoogleDeviceRESTClient(TxotxConnectionData connectionData) {
		super(connectionData);
	}
	
	public GoogleDevice addGoogeDevice(String emailAddress, String registrationId){
		String requestURL = getBaseURL() + "/add-google-device";
		requestURL = addParamToRequestURL(requestURL, "email-address", emailAddress);
		requestURL = addParamToRequestURL(requestURL, "registration-id", registrationId);
		Log.d(TAG,"requestURL : " + requestURL);
		return run(requestURL, HttpMethod.GET);
	}
	
	@Override
	public String getPorltetContext() {
		return "Txotx-portlet";
	}

	@Override
	public String getModelName() {
		return "googledevice";
	}

}
