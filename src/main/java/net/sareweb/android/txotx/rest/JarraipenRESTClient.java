package net.sareweb.android.txotx.rest;

import java.util.List;

import net.sareweb.android.txotx.model.Jarraipen;
import net.sareweb.lifedroid.rest.generic.LDRESTClient;

import org.springframework.http.HttpMethod;

import android.util.Log;

public class JarraipenRESTClient extends LDRESTClient<Jarraipen> {

	public JarraipenRESTClient(TxotxConnectionData connectionData) {
		super(connectionData);
	}
	
	public Jarraipen gehituJarraipenaByEmail(String emailAddress, long jarraituaId, String jarraipenMota){
		String requestURL = getBaseURL() + "/gehitu-jarraipena-by-email";
		requestURL = addParamToRequestURL(requestURL, "email-address", emailAddress);
		requestURL = addParamToRequestURL(requestURL, "jarraitua-id", jarraituaId);
		requestURL = addParamToRequestURL(requestURL, "jarraipen-mota", jarraipenMota);
		return run(requestURL, HttpMethod.POST);
	}
	
	public Jarraipen gehituJarraipena(long jarraitzaileUserId, long jarraituaId, String jarraipenMota){
		String requestURL = getBaseURL() + "/gehitu-jarraipena";
		requestURL = addParamToRequestURL(requestURL, "jarraitzaile-user-id", jarraitzaileUserId);
		requestURL = addParamToRequestURL(requestURL, "jarraitua-id", jarraituaId);
		requestURL = addParamToRequestURL(requestURL, "jarraipen-mota", jarraipenMota);
		return run(requestURL, HttpMethod.POST);
	}
	
	public List<Jarraipen> getJarraipenakByEmail(String emailAddress){
		String requestURL = getBaseURL() + "/get-jarraipenak-by-email";
		requestURL = addParamToRequestURL(requestURL, "email-address", emailAddress);
		Log.d(TAG,"requestURL : " + requestURL);
		return getList(requestURL, HttpMethod.GET);
	}
	
	public List<Jarraipen> getJarraipenak(long jarraitzaileUserId){
		String requestURL = getBaseURL() + "/get-jarraipenak";
		requestURL = addParamToRequestURL(requestURL, "jarraitzaile-user-id", jarraitzaileUserId);
		Log.d(TAG,"requestURL : " + requestURL);
		return getList(requestURL, HttpMethod.GET);
	}
	
	public List<Jarraipen> getErabiltzailearenJarraitzaileak(long jarraituaUserId){
		String requestURL = getBaseURL() + "/get-erabiltzailearen-jarraitzaileak";
		requestURL = addParamToRequestURL(requestURL, "jarraitua-user-id", jarraituaUserId);
		Log.d(TAG,"requestURL : " + requestURL);
		return getList(requestURL, HttpMethod.GET);
	}
	
	public List<Jarraipen> getSagardotegiarenJarraitzaileak(long sagardotegiId){
		String requestURL = getBaseURL() + "/get-sagardotegiaren-jarraitzaileak";
		requestURL = addParamToRequestURL(requestURL, "sagardotegi-id", sagardotegiId);
		Log.d(TAG,"requestURL : " + requestURL);
		return getList(requestURL, HttpMethod.GET);
	}
	
	public List<Jarraipen> getSagardoEgunarenJarraitzaileak(long sagardoEgunId){
		String requestURL = getBaseURL() + "/get-sagardo-egunaren-jarraitzaileak";
		requestURL = addParamToRequestURL(requestURL, "sagardo-egun-id", sagardoEgunId);
		Log.d(TAG,"requestURL : " + requestURL);
		return getList(requestURL, HttpMethod.GET);
	}
	
	public boolean deleteJarraipena(String emailAddress, long jarraituaId){
		String requestURL = getBaseURL() + "/delete-jarraipena";
		requestURL = addParamToRequestURL(requestURL, "email-address", emailAddress);
		requestURL = addParamToRequestURL(requestURL, "jarraitua-id", jarraituaId);
		Log.d(TAG, "requestURL " + requestURL);
		return runForBoolean(requestURL, HttpMethod.GET);
	}
	
	@Override
	public String getPorltetContext() {
		return "Txotx-portlet";
	}

	@Override
	public String getModelName() {
		return "jarraipen";
	}

}
