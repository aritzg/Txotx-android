package net.sareweb.android.txotx.cache;

import java.util.HashMap;
import java.util.Map;

import net.sareweb.android.txotx.rest.TxotxConnectionData;
import net.sareweb.lifedroid.model.User;
import net.sareweb.lifedroid.rest.UserRESTClient;
import android.content.Context;

public class UserCache {

	private static UserRESTClient userRESTClient;

	private static Map<String, User> usersByMail = new HashMap<String, User>();
	private static Map<Long, User> usersById = new HashMap<Long, User>();

	public static void init(Context context){
		userRESTClient = new UserRESTClient(new TxotxConnectionData(context));
	}

	public static User getUser(String emailAddress){
		return getUser(emailAddress, false);
	}
	
	public static User getUser(String emailAddress, boolean refresh){
		if(usersByMail.containsKey(emailAddress) && !refresh){
			return usersByMail.get(emailAddress);
		}
		else{
			User user = userRESTClient.getUserByEmailAddress(emailAddress);
			if(user!=null){
				usersByMail.put(emailAddress, user);
				usersById.put(user.getUserId(), user);
			}
			return user;
		}
	}
	
	public static User getUserById(long userId){
		return getUserById(userId, false);
	}
	
	public static User getUserById(long userId, boolean refresh){
		if(usersById.containsKey(userId) && !refresh){
			return usersById.get(userId);
		}
		else{
			User user = userRESTClient.getUserById(userId);
			if(user!=null){
				usersById.put(userId, user);
				usersByMail.put(user.getEmailAddress(), user);
			}
			return user;
		}
	}

}
