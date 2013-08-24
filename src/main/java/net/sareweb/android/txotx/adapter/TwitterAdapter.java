package net.sareweb.android.txotx.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hamcrest.core.IsInstanceOf;

import twitter4j.Status;

import com.actionbarsherlock.app.SherlockFragment;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.api.BackgroundExecutor;

import net.sareweb.android.txotx.R;
import net.sareweb.android.txotx.cache.UserCache;
import net.sareweb.android.txotx.custom.UserPortrait;
import net.sareweb.android.txotx.custom.UserPortrait_;
import net.sareweb.android.txotx.fragment.GertaerakFragment;
import net.sareweb.android.txotx.fragment.GertaerakFragment_;
import net.sareweb.android.txotx.fragment.SagardoEgunDetailFragment;
import net.sareweb.android.txotx.image.ImageLoader;
import net.sareweb.android.txotx.model.Gertaera;
import net.sareweb.android.txotx.util.AccountUtil;
import net.sareweb.android.txotx.util.Constants;
import net.sareweb.android.txotx.util.ImageUtils;
import net.sareweb.lifedroid.model.User;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class TwitterAdapter extends BaseAdapter {

	private Context context;
	private List<Status> statuses;
	private static String TAG = "TwitterAdapter";
	private ImageLoader imgLoader;

	public TwitterAdapter(Context context, List<Status> statuses){
		this.context = context;
		this.statuses = statuses;
		imgLoader = new ImageLoader(context);
	}

	@Override
	public int getCount() {
		if(statuses!=null)return statuses.size();
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if(statuses!=null)return statuses.get(position);
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void addItems(List<Status> statuses){
		this.statuses.addAll(0,statuses);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.twitter_row, null);
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd hh:mm");
		Status status = statuses.get(position);

		TextView txStatusDate = (TextView) convertView.findViewById(R.id.txStatusDate);
		String dateAndUser = status.getUser().getScreenName() + " @ " + sdf.format(status.getCreatedAt());
		txStatusDate.setText(dateAndUser);
		
		TextView txStatusText = (TextView) convertView.findViewById(R.id.txStatusText);
		txStatusText.setText(status.getText());
		
		ImageView imgStatus = (ImageView) convertView.findViewById(R.id.imgStatus);
		imgLoader.displayImage(status.getUser().getMiniProfileImageURL(), imgStatus);

		return convertView;
	}



	

	


	

}


