package net.sareweb.android.txotx.cache;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import net.sareweb.android.txotx.model.SagardoEgun;
import net.sareweb.android.txotx.rest.SagardoEgunRESTClient;
import net.sareweb.android.txotx.rest.TxotxConnectionData;
import android.content.Context;
import android.util.Log;

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
	
	public static int countSagardoEgunak(){
		if(sagardoEgunak==null) return 0;
		return sagardoEgunak.size();
	}
	
	public static long getActiveSagardoEgunaId(){
		long activeSagardoEguna=0;
		if(sagardoEgunak!=null && sagardoEgunak.size()>0){
			SagardoEgun sagardoEgun = sagardoEgunak.get(0);
			Log.d(TAG, "Azken sagardo eguna " + sagardoEgun.getSagardoEgunId());
			Calendar cal = new GregorianCalendar();
			Date startDate = new Date(sagardoEgun.getHasieraDate());
			Date currentDate = new Date();
			cal.setTime(startDate);
			cal.add(Calendar.DAY_OF_MONTH, 1);
			cal.set(Calendar.HOUR, 0);
			cal.set(Calendar.MINUTE, 0);
			Date nextDay = cal.getTime();
			if(startDate.before(currentDate) && nextDay.after(currentDate)){
				return sagardoEgun.getSagardoEgunId();
			}
		}
		return activeSagardoEguna;
	}

}
