package net.sareweb.android.txotx.rest;

import java.util.List;

import net.sareweb.lifedroid.rest.generic.LDRESTClient;
import net.sareweb.txotx.model.Jarraipen;

import org.springframework.http.HttpMethod;

import com.liferay.portal.kernel.exception.SystemException;

import android.util.Log;

public class JarraipenRESTClient extends LDRESTClient<Jarraipen> {

	public JarraipenRESTClient(TxotxConnectionData connectionData) {
		super(connectionData);
	}
	
	public Jarraipen gehituJarraipena(long jarraitzaileUserId, long jarraigaiId, String jarraipenMota){
		String requestURL = getBaseURL() + "/gehitu-jarraipena";
		requestURL = addParamToRequestURL(requestURL, "jarraitzaile-user-id", jarraitzaileUserId);
		requestURL = addParamToRequestURL(requestURL, "jarraigai-id", jarraigaiId);
		requestURL = addParamToRequestURL(requestURL, "jarraipen-mota", jarraipenMota);
		return run(requestURL, HttpMethod.POST);
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
	
	@Override
	public String getPorltetContext() {
		return "Txotx-portlet";
	}

	@Override
	public String getModelName() {
		return "jarraipen";
	}

}
