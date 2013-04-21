package net.sareweb.android.txotx.rest;

import java.util.List;

import org.springframework.http.HttpMethod;

import android.util.Log;

import net.sareweb.android.txotx.model.SagardoEgun;
import net.sareweb.lifedroid.rest.generic.LDRESTClient;

public class SagardoEgunRESTClient extends LDRESTClient<SagardoEgun> {

	public SagardoEgunRESTClient(TxotxConnectionData connectionData) {
		super(connectionData);
	}
	
	public SagardoEgun getSagardoEgun(long sagardoEgunId){
		String requestURL = getBaseURL() + "/get-sagardo-egun";
		requestURL = addParamToRequestURL(requestURL, "sagardo-egun-id", sagardoEgunId);
		return run(requestURL, HttpMethod.GET);
	}
	
	public List<SagardoEgun> getSagardoEgunak(){
		String requestURL = getBaseURL() + "/get-sagardo-egunak";
		Log.d(TAG,"requestURL: " + requestURL);
		return getList(requestURL, HttpMethod.GET);
	}
	
	
	@Override
	public String getPorltetContext() {
		return "Txotx-portlet";
	}

	@Override
	public String getModelName() {
		return "sagardoegun";
	}

}
