package net.sareweb.android.txotx.rest;

import java.util.List;

import org.springframework.http.HttpMethod;

import android.util.Log;

import net.sareweb.android.txotx.model.Sagardotegi;
import net.sareweb.lifedroid.rest.generic.LDRESTClient;

public class SagardotegiRESTClient extends LDRESTClient<Sagardotegi> {

	public SagardotegiRESTClient(TxotxConnectionData connectionData) {
		super(connectionData);
	}
	
	public Sagardotegi getSagardotegia(long sagardotegiId){
		String requestURL = getBaseURL() + "/get-sagardotegia";
		requestURL = addParamToRequestURL(requestURL, "sagardotegi-id", sagardotegiId);
		return run(requestURL, HttpMethod.GET);
	}
	
	public List<Sagardotegi> getSagardotegiak(){
		String requestURL = getBaseURL() + "/get-sagardotegiak";
		Log.d(TAG,"requestURL: " + requestURL);
		return getList(requestURL, HttpMethod.GET);
	}
	
	@Override
	public String getPorltetContext() {
		return "Txotx-portlet";
	}

	@Override
	public String getModelName() {
		// TODO Auto-generated method stub
		return "sagardotegi";
	}

}
