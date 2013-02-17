package net.sareweb.android.txotx.rest;

import java.util.List;

import net.sareweb.android.txotx.model.Sailkapena;
import net.sareweb.lifedroid.rest.generic.LDRESTClient;

import org.springframework.http.HttpMethod;

import android.util.Log;

public class SailkapenaRESTClient extends LDRESTClient<Sailkapena> {

	public SailkapenaRESTClient(TxotxConnectionData connectionData) {
		super(connectionData);
	}
	
	public List<Sailkapena> getSailkapenak(){
		String requestURL = getBaseURL() + "/get-sailkapenak";
		Log.d(TAG,"requestURL : " + requestURL);
		return getList(requestURL, HttpMethod.GET);
	}
	
	@Override
	public String getPorltetContext() {
		return "Txotx-portlet";
	}

	@Override
	public String getModelName() {
		// TODO Auto-generated method stub
		return "sailkapena";
	}

}
