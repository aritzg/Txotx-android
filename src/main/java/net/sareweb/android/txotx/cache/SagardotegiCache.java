package net.sareweb.android.txotx.cache;

import java.util.List;

import net.sareweb.android.txotx.model.Sagardotegi;
import net.sareweb.android.txotx.rest.SagardotegiRESTClient;
import net.sareweb.android.txotx.rest.TxotxConnectionData;
import android.content.Context;

public class SagardotegiCache {

	private static String TAG = "SagardotegiCache";
	private static SagardotegiRESTClient sagardotegiRESTClient;

	private static List<Sagardotegi> sagardotegiak;

	public static void init(Context context) {
		sagardotegiRESTClient = new SagardotegiRESTClient(
				new TxotxConnectionData(context));
	}

	public static List<Sagardotegi> getSagardotegiak(boolean refresh) {
		try {

			if (sagardotegiak == null || sagardotegiak.size() == 0 || refresh) {
				sagardotegiak = sagardotegiRESTClient.getSagardotegiak();
			}
			return sagardotegiak;
		} catch (Exception e) {
			return null;
		}
	}
	
	public static int countSagardotegiak(){
		if(sagardotegiak==null) return 0;
		return sagardotegiak.size();
	}

}
