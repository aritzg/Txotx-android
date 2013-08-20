package net.sareweb.android.txotx.cache;

import java.util.HashMap;
import java.util.Map;

import net.sareweb.android.txotx.rest.TxotxConnectionData;
import net.sareweb.lifedroid.model.User;
import net.sareweb.lifedroid.rest.UserRESTClient;
import android.content.Context;

public class UserCache {

	private static UserRESTClient userRESTClient;

	private static Map<String, User> users = new HashMap<String, User>();

	public static void init(Context context){
		userRESTClient = new UserRESTClient(new TxotxConnectionData(context));
	}

	public static User getUser(String emailAddress){
		return getUser(emailAddress, false);
	}
	
	public static User getUser(String emailAddress, boolean refresh){
		if(users.containsKey(emailAddress) && !refresh){
			return users.get(emailAddress);
		}
		else{
			User user = userRESTClient.getUserByEmailAddress(emailAddress);
			if(user!=null){
				users.put(emailAddress, user);
			}
			return user;
		}
	}

}
