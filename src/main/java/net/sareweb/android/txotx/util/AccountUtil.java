package net.sareweb.android.txotx.util;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.auth.GoogleAuthUtil;

/**
 * Created by aritz on 11/06/13.
 */
public class AccountUtil {

    private static String token = null;

    public static String getGoogleEmail(Context context) {
        AccountManager am = AccountManager.get(context);
        Account[] accounts = am.getAccountsByType("com.google");

        if (accounts != null && accounts.length > 0) {
            Account account = (Account) accounts[0];
            return account.name;
        } else {
            return null;
        }

    }
    
    public static String getUserName(Context context) {
        String googleEmail = getGoogleEmail(context);
        if(googleEmail!=null && googleEmail.indexOf("@")!=-1){
        	return googleEmail.substring(0, googleEmail.indexOf("@"));
        }
        return "";
    }

    public static String getGoogleAuthToken(Context context) {
        if (token == null) {
            try {
                token = GoogleAuthUtil.getToken(context, getGoogleEmail(context), "audience:server:client_id:" + Constants.CLIENT_ID);
            } catch (Exception e) {
                e.printStackTrace();
                token = null;
            }
        }

        return token;
    }

}