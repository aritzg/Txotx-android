package net.sareweb.android.txotx.custom;

import net.sareweb.android.txotx.R;
import net.sareweb.android.txotx.cache.UserCache;
import net.sareweb.android.txotx.image.ImageLoader;
import net.sareweb.android.txotx.util.Constants;
import net.sareweb.lifedroid.model.User;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EViewGroup;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.user_portrait_small)
public class UserPortrait extends RelativeLayout {

	private String TAG ="UserPortrait";
	@ViewById
	TextView txMemberName;
	@ViewById
	ImageView imgMember;
	private ImageLoader imgLoader;

	public UserPortrait(Context context, AttributeSet attrs) {
		super(context, attrs);
		imgLoader = new ImageLoader(context);
	}

	/*public void updateForUser(long userId){
		getUser(userId);
	}*/

	@Background
	void getUser(String  emailAddress){
		User user = UserCache.getUser(emailAddress);
		getUserResult(user);
	}

	@UiThread
	void getUserResult(User user){
		if(user!=null){
			txMemberName.setText(user.getScreenName());
			imgLoader.displayImage("http://" + Constants.SERVICE_URL + ":" + Constants.SERVICE_PORT + "/image/user_portrait?img_id="+user.getPortraitId(), imgMember);
		}
		else{
			imgLoader.displayImage(null, imgMember);
		}
	}
}
