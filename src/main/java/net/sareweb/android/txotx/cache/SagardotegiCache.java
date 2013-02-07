package net.sareweb.android.txotx.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;

import net.sareweb.android.txotx.model.Sagardotegi;
import net.sareweb.android.txotx.rest.SagardotegiRESTClient;
import net.sareweb.android.txotx.rest.TxotxConnectionData;
import net.sareweb.android.txotx.util.TxotxPrefs_;
import net.sareweb.lifedroid.model.User;
import net.sareweb.lifedroid.rest.UserRESTClient;

public class SagardotegiCache {

	private static TxotxPrefs_ prefs;
	private static String TAG = "SagardotegiCache";
	private static SagardotegiRESTClient sagardotegiRESTClient;

	private static List<Sagardotegi> sagardotegiak;

	public static void init(TxotxPrefs_ preferences) {
		prefs = preferences;
		sagardotegiRESTClient = new SagardotegiRESTClient(
				new TxotxConnectionData(prefs));
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

}
