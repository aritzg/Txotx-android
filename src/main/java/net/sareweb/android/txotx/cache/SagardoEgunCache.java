package net.sareweb.android.txotx.cache;

import java.util.List;

import net.sareweb.android.txotx.model.SagardoEgun;
import net.sareweb.android.txotx.rest.SagardoEgunRESTClient;
import net.sareweb.android.txotx.rest.TxotxConnectionData;
import android.content.Context;

public class SagardoEgunCache {

	private static String TAG = "SagardoEgunCache";
	private static SagardoEgunRESTClient sagardoEgunRESTClient;

	private static List<SagardoEgun> sagardoEgunak;

	public static void init(Context context) {
		sagardoEgunRESTClient = new SagardoEgunRESTClient(
				new TxotxConnectionData(context));
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
