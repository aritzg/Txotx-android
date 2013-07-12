package net.sareweb.android.txotx.cache;

import java.util.HashMap;
import java.util.Map;

import net.sareweb.android.txotx.rest.TxotxConnectionData;
import net.sareweb.lifedroid.model.User;
import net.sareweb.lifedroid.rest.UserRESTClient;
import android.content.Context;

public class UserCache {

	private static UserRESTClient userRESTClient;

	private static Map<Long, User> users = new HashMap<Long, User>();

	public static void init(Context context){
		userRESTClient = new UserRESTClient(new TxotxConnectionData(context));
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
