package net.sareweb.android.txotx.util;

public class PrefUtils {
	
	public static boolean isUserLogged(TxotxPrefs_ prefs){
		if(prefs.user().getOr("").equals(""))return false;
		return true;
	}
	
	public static void clearUserPrefs(TxotxPrefs_ prefs){
		prefs.user().put("");
		prefs.pass().put("");
	}

}
