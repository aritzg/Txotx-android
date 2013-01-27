package net.sareweb.android.txotx.rest;

import java.util.List;

import net.sareweb.android.txotx.model.Gertaera;
import net.sareweb.lifedroid.rest.generic.LDRESTClient;

import org.springframework.http.HttpMethod;

import android.util.Log;

public class GertaeraRESTClient extends LDRESTClient<Gertaera> {

	public GertaeraRESTClient(TxotxConnectionData connectionData) {
		super(connectionData);
	}
	
	public Gertaera gehituTestuGertaera(long sagardotegiId, String testua){
		String requestURL = getBaseURL() + "/gehitu-testu-gertaera";
		requestURL = addParamToRequestURL(requestURL, "sagardotegi-id", sagardotegiId);
		requestURL = addParamToRequestURL(requestURL, "testua", testua, true);
		return run(requestURL, HttpMethod.POST);
	}
	
	public Gertaera gehituArgazkiGertaera(long sagardotegiId, String testua, long irudiKarpetaId, String irudia){
		String requestURL = getBaseURL() + "/gehitu-argazki-gertaera";
		requestURL = addParamToRequestURL(requestURL, "sagardotegi-id", sagardotegiId);
		requestURL = addParamToRequestURL(requestURL, "testua", testua, true);
		requestURL = addParamToRequestURL(requestURL, "irudi-karpeta-id", irudiKarpetaId);
		requestURL = addParamToRequestURL(requestURL, "irudia", irudia);
		return run(requestURL, HttpMethod.POST);
	}
	
	public Gertaera gehituBalorazioGertaera(long sagardotegiId, String testua, long balorazioa){
		String requestURL = getBaseURL() + "/gehitu-balorazio-gertaera";
		requestURL = addParamToRequestURL(requestURL, "sagardotegi-id", sagardotegiId);
		requestURL = addParamToRequestURL(requestURL, "testua", testua, true);
		requestURL = addParamToRequestURL(requestURL, "balorazioa", balorazioa);
		return run(requestURL, HttpMethod.POST);
	}
	
	public List<Gertaera> getGertaerakOlderThanDate(long sagardotegiId, long date, int blockSize){
		String requestURL = getBaseURL() + "/get-gertaerak-older-than-date";
		requestURL = addParamToRequestURL(requestURL, "sagardotegi-id", sagardotegiId);
		requestURL = addParamToRequestURL(requestURL, "date", date);
		requestURL = addParamToRequestURL(requestURL, "block-size", blockSize);
		Log.d(TAG,"requestURL : " + requestURL);
		return getList(requestURL, HttpMethod.GET);
	}
	
	public List<Gertaera> getGertaerakNewerThanDate(long sagardotegiId, long date, int blockSize){
		String requestURL = getBaseURL() + "/get-gertaerak-older-than-date";
		requestURL = addParamToRequestURL(requestURL, "sagardotegi-id", sagardotegiId);
		requestURL = addParamToRequestURL(requestURL, "date", date);
		requestURL = addParamToRequestURL(requestURL, "block-size", blockSize);
		return getList(requestURL, HttpMethod.GET);
	}
	
	@Override
	public String getPorltetContext() {
		return "Txotx-portlet";
	}

	@Override
	public String getModelName() {
		// TODO Auto-generated method stub
		return "gertaera";
	}

}
