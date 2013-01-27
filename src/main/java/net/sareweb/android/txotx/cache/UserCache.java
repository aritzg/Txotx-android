package net.sareweb.android.txotx.cache;

import java.util.HashMap;
import java.util.Map;

import net.sareweb.android.txotx.rest.TxotxConnectionData;
import net.sareweb.android.txotx.util.TxotxPrefs_;
import net.sareweb.lifedroid.model.User;
import net.sareweb.lifedroid.rest.UserRESTClient;

public class UserCache {

	private static TxotxPrefs_ prefs;

	private static UserRESTClient userRESTClient;

	private static Map<Long, User> users = new HashMap<Long, User>();

	public static void init(TxotxPrefs_ preferences){
		prefs = preferences;
		userRESTClient = new UserRESTClient(new TxotxConnectionData(prefs));
	}


	public static User getUser(long userId){
		if(users.containsKey(userId)){
			return users.get(userId);
		}
		else{
			User user = userRESTClient.getUserById(userId);
			if(user!=null){
				users.put(userId, user);
			}
			return user;
		}
	}

}
