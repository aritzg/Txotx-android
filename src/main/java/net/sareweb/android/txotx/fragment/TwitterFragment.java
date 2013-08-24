package net.sareweb.android.txotx.fragment;

import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.api.TweetsResources;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;
import net.sareweb.android.txotx.R;
import net.sareweb.android.txotx.adapter.TwitterAdapter;
import net.sareweb.android.txotx.custom.PullToRefreshListView;
import net.sareweb.android.txotx.custom.PullToRefreshListView.OnRefreshListener;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;

@EFragment(R.layout.twitter)
public class TwitterFragment extends SherlockFragment implements OnRefreshListener{

	private static final String TAG = "TwitterFragment";
	ConfigurationBuilder t4jCb = new ConfigurationBuilder();
	Twitter twitter;
	TwitterAdapter twitterAdapter;
	@ViewById(R.id.twitter_list_view)
	ListView twitterListView;
	private ProgressDialog dialog;
	
	private long sinceId = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		t4jCb.setOAuthConsumerKey(TWITTER_CONSUMER_KEY)
			.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET)
			.setOAuthAccessToken(TWITTER_ACCESS_TOKEN)
			.setOAuthAccessTokenSecret(TWITTER_ACCESS_TOKEN_SECRET);
		TwitterFactory tf = new TwitterFactory(t4jCb.build());
		twitter = tf.getInstance();
	}

	@Override
	public void onResume() {
		super.onResume();
		((PullToRefreshListView) twitterListView).setOnRefreshListener(this);
		dialog = ProgressDialog.show(getSherlockActivity(), "", "Txioak eskuratzen...", true);
		dialog.show();
		readFromTwitter();
	}
	
	@Background
	public void readFromTwitter(){
		Query query = new Query("from:txootx OR #txotx OR txootx");
		query.setSinceId(sinceId);
		try {
			QueryResult queryResult = twitter.search(query);
			Log.d(TAG, "Since " + sinceId + " got " +  queryResult.getTweets().size() + " tweets.");
			
			if(sinceId==0){
				showTweets(queryResult.getTweets());
			}
			else{
				showNewTweets(queryResult.getTweets());
			}
			//Set new since
			sinceId = queryResult.getMaxId();
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@UiThread
	public void showTweets(List<Status> statuses){
		twitterAdapter = new TwitterAdapter(getSherlockActivity(), statuses);
		twitterListView.setAdapter(twitterAdapter);
		((PullToRefreshListView) twitterListView).onRefreshComplete();
		if(dialog!=null)dialog.dismiss();
	}
	
	@UiThread
	public void showNewTweets(List<Status> statuses){
		twitterAdapter.addItems(statuses);
		twitterAdapter.notifyDataSetChanged();
		((PullToRefreshListView) twitterListView).onRefreshComplete();
		if(dialog!=null)dialog.dismiss();
	}
	
	@Override
	public void onRefresh() {
		readFromTwitter();
	}
	
	
	
	public static final String TWITTER_CONSUMER_KEY = "KtiMXXOQXQ4XwpbGrlKUZg";
	public static final String TWITTER_CONSUMER_SECRET = "h47ob2XJo4W7vuIK82POOQFf19Cszora42CoyrobHU";
	public static final String TWITTER_ACCESS_TOKEN = "11872392-rKExl7WHCzSbFKpAClXtNIPEqNk8aF8b4dNvHD9a8";
	public static final String TWITTER_ACCESS_TOKEN_SECRET = "BPpsp7fXbl3TKFOX8wXGUWXX1hFL0OgeaollYlgPwM";


	
}
