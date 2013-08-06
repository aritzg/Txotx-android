package net.sareweb.android.txotx.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sareweb.android.txotx.model.Jarraipen;
import net.sareweb.android.txotx.rest.JarraipenRESTClient;
import net.sareweb.android.txotx.rest.TxotxConnectionData;

import android.content.Context;
import android.util.Log;

public class JarraipenCache {

	private static JarraipenRESTClient jarraipenRESTClient;
	private static Map<Long, Jarraipen> jarraipenak = new HashMap<Long, Jarraipen>();
	private static String TAG = "JarraipenCache";

	public static void init(Context context){
		jarraipenRESTClient = new JarraipenRESTClient(new TxotxConnectionData(context));
	}
	
	public static void eskuratuJarraipenak(String emailAddress){
		List<Jarraipen> jarraipenZerrenda = jarraipenRESTClient.getJarraipenakByEmail(emailAddress);
		jarraipenak = new HashMap<Long, Jarraipen>();
		if(jarraipenZerrenda!=null){
			for(Jarraipen jarraipen : jarraipenZerrenda){
				Log.d(TAG,"Jarraitzen dut " + jarraipen.getJarraipenId());
				if(!jarraipenak.containsKey(jarraipen.getJarraipenId())){
					jarraipenak.put(jarraipen.getJarraituaId(), jarraipen);
				}
			}
		}
	}

	public static boolean jarraitzenDut(String emailAddress, long jarraituaId, boolean forceRefresh){
		if(forceRefresh){
			eskuratuJarraipenak(emailAddress);
		}
		boolean jarraitzenDu = jarraipenak.containsKey(jarraituaId); 
		Log.d(TAG, emailAddress + "-ek jarraitzen du " + jarraituaId + "?" + Boolean.toString(jarraitzenDu));
		return jarraitzenDu;
	}
	
	public static void gehituJarraipena(Jarraipen jarraipen){
		if(!jarraipenak.containsKey(jarraipen.getJarraipenId())){
			jarraipenak.put(jarraipen.getJarraituaId(), jarraipen);
		}
	}
	
	public static void ezabatuJarraipena(long jarraituaId){
		if(!jarraipenak.containsKey(jarraituaId)){
			jarraipenak.remove(jarraituaId);
		}
	}

}
