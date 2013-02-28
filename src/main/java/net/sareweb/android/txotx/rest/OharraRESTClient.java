package net.sareweb.android.txotx.rest;

import java.util.List;

import net.sareweb.android.txotx.model.Oharra;
import net.sareweb.lifedroid.rest.generic.LDRESTClient;
import net.sareweb.txotx.service.OharraLocalServiceUtil;
import net.sareweb.txotx.util.mezulari.OharraMezulariThread;

import org.springframework.http.HttpMethod;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;

import android.util.Log;

public class OharraRESTClient extends LDRESTClient<Oharra> {

	public OharraRESTClient(TxotxConnectionData connectionData) {
		super(connectionData);
	}
	
	public Oharra getAzkenErronka(){
		String requestURL = getBaseURL() + "/get-azken-erronka";
		Log.d(TAG,"requestURL : " + requestURL);
		return run(requestURL, HttpMethod.GET);
	}
	
	public Oharra getAzkenOharra(){
		String requestURL = getBaseURL() + "/get-azken-oharra";
		Log.d(TAG,"requestURL : " + requestURL);
		return run(requestURL, HttpMethod.GET);
	}
	
	public Oharra getOharra(long oharraId){
		String requestURL = getBaseURL() + "/get-oharra";
		requestURL = addParamToRequestURL(requestURL, "oharra-id", oharraId);
		Log.d(TAG,"requestURL : " + requestURL);
		return run(requestURL, HttpMethod.GET);
	}
	
	public void bidaliOharra(long oharraId){
		String requestURL = getBaseURL() + "/bidali-oharra";
		requestURL = addParamToRequestURL(requestURL, "oharra-id", oharraId);
		Log.d(TAG,"requestURL : " + requestURL);
		run(requestURL, HttpMethod.GET);
		return;
	}
	
	@Override
	public String getPorltetContext() {
		return "Txotx-portlet";
	}

	@Override
	public String getModelName() {
		return "oharra";
	}

}
