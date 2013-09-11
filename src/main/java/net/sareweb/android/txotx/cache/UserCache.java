package net.sareweb.android.txotx.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sareweb.android.txotx.rest.TxotxConnectionData;
import net.sareweb.android.txotx.util.Constants;
import net.sareweb.lifedroid.model.User;
import net.sareweb.lifedroid.rest.UserRESTClient;
import android.content.Context;
import android.util.Log;

public class UserCache {

	private static String TAG = "UserCache";
	private static UserRESTClient userRESTClient;

	private static Map<String, User> usersByMail = new HashMap<String, User>();
	private static Map<Long, User> usersById = new HashMap<Long, User>();

	public static void init(Context context){
		userRESTClient = new UserRESTClient(new TxotxConnectionData(context));
	}

	public static void preloadUsers(){
		try {
			List<User> users = userRESTClient.getGroupUsers(Constants.GROUP);	
			if(users!=null){
				for(User user : users){
					usersByMail.put(user.getEmailAddress(), user);
					usersById.put(user.getUserId(), user);
				}
			}
		} catch (Exception e) {
			Log.e(TAG, "Error preloading users", e);
		}
		
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
