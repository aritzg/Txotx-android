package net.sareweb.android.txotx.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.hamcrest.core.IsInstanceOf;

import com.actionbarsherlock.app.SherlockFragment;
import com.googlecode.androidannotations.annotations.Background;
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
import android.widget.TextView;

public class GertaeraAdapter extends BaseAdapter implements OnClickListener{

	private Context context;
	private SherlockFragment fragment;
	private List<Gertaera> gertaerak;
	private static String TAG = "GertaeraAdapter";
	private ImageLoader imgLoader;
	private Handler handler_ = new Handler();
	ExecutorService executorService;
	
	public GertaeraAdapter(Context context, List<Gertaera> gertaerak, SherlockFragment fragment){
		Log.d(TAG, "gertaera prestatzen " + gertaerak.size() );
		this.context = context;
		this.fragment = fragment;
		this.gertaerak = gertaerak;
		imgLoader = new ImageLoader(context);
		executorService=Executors.newFixedThreadPool(5);
	}

	@Override
	public int getCount() {
		if(gertaerak!=null)return gertaerak.size();
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if(gertaerak!=null)return gertaerak.get(position);
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.gertaera_row, null);
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd hh:mm");
		Gertaera gertaera = gertaerak.get(position);

		TextView txGertaeraDate = (TextView) convertView.findViewById(R.id.txGertaeraDate);
		Date tmpDate = new Date(gertaera.getCreateDate());
		txGertaeraDate.setText(gertaera.getScreenName() + " @ " + sdf.format(tmpDate));

		if(gertaera.getGertaeraMota().equals(Constants.GERTAERA_MOTA_TESTUA)){
			drawGertaeraTestua(convertView, gertaera);
		}
		else if(gertaera.getGertaeraMota().equals(Constants.GERTAERA_MOTA_ARGAZKIA)){
			drawGertaeraIrudia(convertView, gertaera);
		}
		else if(gertaera.getGertaeraMota().equals(Constants.GERTAERA_MOTA_BALORAZIOA)){
			drawGertaeraBalorazioa(convertView, gertaera);
		}
		
		ImageView imgReply = (ImageView) convertView.findViewById(R.id.imgReply);
		imgReply.setTag("@" + gertaera.getScreenName());
		imgReply.setOnClickListener(this);	

		convertView.setTag(gertaerak.get(position));
		return convertView;
	}


	public void drawGertaeraTestua(View convertView, Gertaera gertaera){
		TextView txGertaeraText = (TextView) convertView.findViewById(R.id.txGertaeraText);
		String testua =gertaera.getScreenName() +  "-(e)k iruzkin huts bat jarri du! \\o/";
		if(!gertaera.getTestua().equals("") && !gertaera.getTestua().equals("ERROR")) testua = gertaera.getTestua();
		txGertaeraText.setText(testua);
		ImageView imgGertaera = (ImageView) convertView.findViewById(R.id.imgGertaera);
		imgLoader.displayImage(ImageUtils.getGertaeraImageUrl(gertaera), imgGertaera, R.drawable.ic_launcher);
		
		PortraitUserLoader portraitUserLoader = new PortraitUserLoader(imgGertaera, gertaera);
		BackgroundExecutor.execute(portraitUserLoader);
		
		ImageView imgArgazki = (ImageView) convertView.findViewById(R.id.imgArgazki);
		imgArgazki.setVisibility(View.GONE);
	}

	public void drawGertaeraIrudia(View convertView, Gertaera gertaera){
		TextView txGertaeraText = (TextView) convertView.findViewById(R.id.txGertaeraText);
		String testua =gertaera.getScreenName() +  "-(e)k ez dio argazkiari iruzkinik jarri!";
		if(!gertaera.getTestua().equals("") && !gertaera.getTestua().equals("ERROR")) testua = gertaera.getTestua();
		txGertaeraText.setText(testua);
		
		ImageView imgGertaera = (ImageView) convertView.findViewById(R.id.imgGertaera);
		PortraitUserLoader portraitUserLoader = new PortraitUserLoader(imgGertaera, gertaera);
		executorService.submit(portraitUserLoader);
		//BackgroundExecutor.execute(portraitUserLoader);
		
		ImageView imgArgazki = (ImageView) convertView.findViewById(R.id.imgArgazki);
		imgLoader.displayImage(ImageUtils.getGertaeraImageUrl(gertaera), imgArgazki, R.drawable.ic_launcher);
		imgArgazki.setVisibility(View.VISIBLE);
		
	}

	public void drawGertaeraBalorazioa(View convertView, Gertaera gertaera){
		TextView txGertaeraText = (TextView) convertView.findViewById(R.id.txGertaeraText);
		txGertaeraText.setText(gertaera.getScreenName() + "-k " + gertaera.getBalorazioa() + " izar eman dizkio sagardotegi honi.");
		ImageView imgGertaera = (ImageView) convertView.findViewById(R.id.imgGertaera);
		imgLoader.displayImage(ImageUtils.getGertaeraImageUrl(gertaera), imgGertaera, R.drawable.rating);
		
		ImageView imgArgazki = (ImageView) convertView.findViewById(R.id.imgArgazki);
		imgArgazki.setVisibility(View.GONE);
	}
	
	class PortraitUserLoader implements Runnable{
		Gertaera gertaera;
		ImageView imageView;
		public PortraitUserLoader(ImageView imageView, Gertaera gertaera){
			this.imageView=imageView;
			this.gertaera=gertaera;
		}
		@Override
		public void run() {
			Log.d(TAG, "Getting user " + gertaera.getUserId());
			User user = UserCache.getUserById(gertaera.getUserId());
			drawPortrait(imageView, user);
		}
		
	}
	
	public void drawPortrait(final ImageView imageView,final User user){
		handler_.post(new Runnable() {
            @Override
            public void run() {
                try {
                	imgLoader.displayImage(ImageUtils.getPortraitImageUrl(user), imageView, R.drawable.ic_launcher);
                } catch (RuntimeException e) {
                    Log.e(TAG, "A runtime exception was thrown while executing code in a runnable", e);
                }
            }

        }
        );
	}

	@Override
	public void onClick(View v) {
		String izena = (String)v.getTag();
		Log.d(TAG, izena);
		if(fragment!=null && fragment instanceof SagardoEgunDetailFragment)
			((SagardoEgunDetailFragment)fragment).showAddCommentDialog(izena);
		if(fragment!=null && fragment instanceof GertaerakFragment)
			((GertaerakFragment)fragment).showAddCommentDialog(izena);
	}

}


