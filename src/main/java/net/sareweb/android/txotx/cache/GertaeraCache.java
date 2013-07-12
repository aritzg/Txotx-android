package net.sareweb.android.txotx.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sareweb.android.txotx.model.Gertaera;
import net.sareweb.android.txotx.rest.GertaeraRESTClient;
import net.sareweb.android.txotx.rest.TxotxConnectionData;
import net.sareweb.android.txotx.util.AccountUtil;
import android.content.Context;
import android.util.Log;

public class GertaeraCache {
	private static String TAG = "GertaeraCache";

	private static GertaeraRESTClient gertaeraRESTClient;
	
	private static HashMap<Long, GertaeraZerrenda> gertaerak = new HashMap<Long, GertaeraZerrenda>();
	
	public static void init(Context context){
		gertaeraRESTClient = new GertaeraRESTClient(new TxotxConnectionData(context));
	}
	
	public static List<Gertaera> getGertaerak(long lekuId, boolean refresh){
		GertaeraZerrenda zerrenda = gertaerak.get(lekuId);
		if(!refresh && zerrenda!=null){
			Log.d(TAG, "Ez da eguneratu behar eta zerrenda ez da NULL");
			return zerrenda.getGertaerak();
		}
		else if(zerrenda==null){
			Log.d(TAG, "Zerrenda null denez eguneratu egin behar da");
			List<Gertaera> gertaeraEskuratuak = gertaeraRESTClient.getGertaerakOlderThanDate(lekuId, 0, 100);
			GertaeraZerrenda zerrendaBerria = new  GertaeraZerrenda();
			String s = new String();
			zerrendaBerria.appendGertaerak(gertaeraEskuratuak);
			gertaerak.put(lekuId, zerrendaBerria);
			return gertaeraEskuratuak;
		}
		else{
			Log.d(TAG, "Gertaera berriagoak ekarriko dira");
			List<Gertaera> gertaeraEskuratuak = gertaeraRESTClient.getGertaerakNewerThanDate(lekuId, zerrenda.getUpdateDate(), 100);
			zerrenda.appendGertaerak(gertaeraEskuratuak);
			gertaerak.put(lekuId, zerrenda);
			return gertaeraEskuratuak;
		}
	}
	
	public static void gehituGertaera(Gertaera gertaera){
		if(gertaera==null)return;
		gertaerak.get(gertaera.getSagardotegiId()).appendGertaera(gertaera);
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
		
		public void appendGertaera(Gertaera gertaeraBerria){
			gertaerak.add(0, gertaeraBerria);
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
