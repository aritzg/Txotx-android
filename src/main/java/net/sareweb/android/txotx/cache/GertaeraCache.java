package net.sareweb.android.txotx.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.util.Log;

import net.sareweb.android.txotx.model.Gertaera;
import net.sareweb.android.txotx.rest.GertaeraRESTClient;
import net.sareweb.android.txotx.rest.TxotxConnectionData;
import net.sareweb.android.txotx.util.TxotxPrefs_;

public class GertaeraCache {
	private static String TAG = "GertaeraCache";
	private static TxotxPrefs_ prefs;

	private static GertaeraRESTClient gertaeraRESTClient;
	
	private static HashMap<Long, GertaeraZerrenda> sagardotegiGertaerak = new HashMap<Long, GertaeraZerrenda>();
	
	public static void init(TxotxPrefs_ preferences){
		prefs = preferences;
		gertaeraRESTClient = new GertaeraRESTClient(new TxotxConnectionData(prefs));
	}
	
	public static List<Gertaera> getSagardotegiGertaerak(long sagardotegiId, boolean refresh){
		GertaeraZerrenda zerrenda = sagardotegiGertaerak.get(sagardotegiId);
		if(!refresh && zerrenda!=null){
			Log.d(TAG, "Ez da eguneratu behar eta zerrenda ez da NULL");
			return zerrenda.getGertaerak();
		}
		else if(zerrenda==null){
			Log.d(TAG, "Zerrenda null denez eguneratu egin behar da");
			List<Gertaera> gertaerak = gertaeraRESTClient.getGertaerakOlderThanDate(sagardotegiId, 0, 100);
			GertaeraZerrenda zerrendaBerria = new  GertaeraZerrenda();
			String s = new String();
			zerrendaBerria.appendGertaerak(gertaerak);
			sagardotegiGertaerak.put(sagardotegiId, zerrendaBerria);
			return gertaerak;
		}
		else{
			Log.d(TAG, "Gertaera berriagoak ekarriko dira");
			List<Gertaera> gertaerak = gertaeraRESTClient.getGertaerakNewerThanDate(sagardotegiId, zerrenda.getUpdateDate(), 100);
			zerrenda.appendGertaerak(gertaerak);
			sagardotegiGertaerak.put(sagardotegiId, zerrenda);
			return gertaerak;
		}
	}
	
	
	public static class GertaeraZerrenda{
		private long updateDate;
		private List<Gertaera> gertaerak = new ArrayList<Gertaera>();
		
		public List<Gertaera> getGertaerak(){
			return gertaerak;
		}
		
		public void appendGertaerak(List<Gertaera> gertaeraBerriak){
			gertaerak.addAll(0, gertaeraBerriak);
			if(gertaerak.size()>0){
				Gertaera g = gertaerak.get(0);	
				updateDate = g.getCreateDate();
			}
		}
		
		public long getUpdateDate() {
			return updateDate;
		}

		
	}

}
