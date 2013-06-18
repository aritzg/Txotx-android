package net.sareweb.android.txotx.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;

import net.sareweb.android.txotx.model.SagardoEgun;
import net.sareweb.android.txotx.rest.SagardoEgunRESTClient;
import net.sareweb.android.txotx.rest.TxotxConnectionData;
import net.sareweb.android.txotx.util.TxotxPrefs_;
import net.sareweb.lifedroid.model.User;
import net.sareweb.lifedroid.rest.UserRESTClient;

public class SagardoEgunCache {

	private static TxotxPrefs_ prefs;
	private static String TAG = "SagardoEgunCache";
	private static SagardoEgunRESTClient sagardoEgunRESTClient;

	private static List<SagardoEgun> sagardoEgunak;

	public static void init(TxotxPrefs_ preferences) {
		prefs = preferences;
		sagardoEgunRESTClient = new SagardoEgunRESTClient(
				new TxotxConnectionData(prefs));
	}

	public static List<SagardoEgun> getSagardoEgunak(boolean refresh) {
		try {

			if (sagardoEgunak == null || sagardoEgunak.size() == 0 || refresh) {
				sagardoEgunak = sagardoEgunRESTClient.getSagardoEgunak();
			}
			return sagardoEgunak;
		} catch (Exception e) {
			return null;
		}
	}

}
