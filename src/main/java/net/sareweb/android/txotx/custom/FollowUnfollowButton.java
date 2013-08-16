package net.sareweb.android.txotx.custom;

import net.sareweb.android.txotx.R;
import net.sareweb.android.txotx.cache.JarraipenCache;
import net.sareweb.android.txotx.model.Jarraipen;
import net.sareweb.android.txotx.rest.JarraipenRESTClient;
import net.sareweb.android.txotx.rest.TxotxConnectionData;
import net.sareweb.android.txotx.util.AccountUtil;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.view.View.OnClickListener;

import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EViewGroup;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.follow_unfollow_button)
public class FollowUnfollowButton extends RelativeLayout implements OnClickListener{

	private String TAG = "FollowUnfollowButton";
	private Context context;
	@ViewById ImageView imgPin;
	private long jarraituaId =0;
	private String jarraipenMota = "";
	private JarraipenRESTClient jarraipenRESTClient;
	private boolean followingFlag = false;

	public FollowUnfollowButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		imgPin.setOnClickListener(this);
		jarraipenRESTClient = new JarraipenRESTClient(new TxotxConnectionData(context));
		if(followingFlag){
			imgPin.setImageResource(R.drawable.pin_true);
		}
		else{
			imgPin.setImageResource(R.drawable.pin_false);
		}
	}

	@Override
	public void onClick(View v) {
		if(followingFlag){
			unfollow();
		}else{
			follow();	
		}
	}

	public void setJarraituaIdEtaMota(long jarraituaId,String jarraipenMota){
		this.jarraituaId=jarraituaId;
		this.jarraipenMota=jarraipenMota;
		if(JarraipenCache.jarraitzenDut(AccountUtil.getGoogleEmail(context), jarraituaId, false)){
			Log.d(TAG, "Jarraitzen dut");
			imgPin.setImageResource(R.drawable.pin_true);
			followingFlag=true;
		}
		else{
			Log.d(TAG, "Ez dut jarraitzen");
			imgPin.setImageResource(R.drawable.pin_false);
			followingFlag=false;
		}
	}

	@Background
	public void follow(){
		Jarraipen jarraipen = jarraipenRESTClient.gehituJarraipenaByEmail(AccountUtil.getGoogleEmail(context), jarraituaId, jarraipenMota);
		followResult(jarraipen);
	}

	@UiThread
	public void followResult(Jarraipen jarraipen){
		if(jarraipen!=null){
			imgPin.setImageResource(R.drawable.pin_true);
			followingFlag=true;
			JarraipenCache.gehituJarraipena(jarraipen);
			if(jarraipenMota.equals(Jarraipen.JARRAIPEN_MOTA_PERTSONA)){
				Toast.makeText(context, "Erabiltzaile honen gertaerak iritsiko zaizkizu!", Toast.LENGTH_SHORT).show();
			}
			else if(jarraipenMota.equals(Jarraipen.JARRAIPEN_MOTA_SAGARDO_EGUNA)){
				Toast.makeText(context, "Sagardo egun honetako gertaeren oharrak iritsiko zaizkizu!", Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Background
	public void unfollow(){
		if(jarraipenRESTClient.deleteJarraipena(AccountUtil.getGoogleEmail(context), jarraituaId)){
			unfollowResult();
		}
	}

	@UiThread
	public void unfollowResult(){
		imgPin.setImageResource(R.drawable.star_false);
		followingFlag=false;
		JarraipenCache.ezabatuJarraipena(jarraituaId);
		if(jarraipenMota.equals(Jarraipen.JARRAIPEN_MOTA_PERTSONA)){
			Toast.makeText(context, "Ez zaizkizu gehiago erabiltzaile honen gertaerak iritsiko.", Toast.LENGTH_SHORT).show();
		}
		else if(jarraipenMota.equals(Jarraipen.JARRAIPEN_MOTA_SAGARDO_EGUNA)){
			Toast.makeText(context, "Ez zaizkizu gehiago sagardo egun honen gertaerak iritsiko.", Toast.LENGTH_SHORT).show();
		}
	}
}