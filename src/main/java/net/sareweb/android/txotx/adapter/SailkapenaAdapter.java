package net.sareweb.android.txotx.adapter;

import java.util.List;

import net.sareweb.android.txotx.R;
import net.sareweb.android.txotx.custom.FollowUnfollowButton;
import net.sareweb.android.txotx.model.Jarraipen;
import net.sareweb.android.txotx.model.Sailkapena;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SailkapenaAdapter extends BaseAdapter{

	private Context context;
	private List<Sailkapena> sailkapenak;
	private static String TAG = "SailkapenaAdapter";

	public SailkapenaAdapter(Context context, List<Sailkapena> sailkapenak){
		this.context = context;
		this.sailkapenak = sailkapenak; 
	}

	@Override
	public int getCount() {
		if(sailkapenak!=null)return sailkapenak.size();
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if(sailkapenak!=null)return sailkapenak.get(position);
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.d(TAG, "position " + position); 
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.sailkapena_row, null);
		}
		
		Sailkapena sailkapena = sailkapenak.get(position);
		
		TextView txSailkapen = (TextView)convertView.findViewById(R.id.txSailkapen);
		txSailkapen.setText(String.valueOf(position+1));
		
		TextView txScreenName = (TextView)convertView.findViewById(R.id.txScreenName);
		txScreenName.setText(sailkapena.getScreenName() + " (" + sailkapena.getGertaeraKopurua() + ")");
		
		TextView txCountPic = (TextView)convertView.findViewById(R.id.txCountPic);
		txCountPic.setText(String.valueOf(sailkapena.getArgazkiKopurua()));
		TextView txCountMsg = (TextView)convertView.findViewById(R.id.txCountMsg);
		txCountMsg.setText(String.valueOf(sailkapena.getIruzkinKopurua()));
		TextView txCountVal = (TextView)convertView.findViewById(R.id.txCountVal);
		txCountVal.setText(String.valueOf(sailkapena.getBalorazioKopurua()));
		
		FollowUnfollowButton btnFollowUnfollow = (FollowUnfollowButton)convertView.findViewById(R.id.btnFollowUnfollow);
		btnFollowUnfollow.setJarraituaIdEtaMota(sailkapena.getUserId() , Jarraipen.JARRAIPEN_MOTA_PERTSONA);
		
		switch (position) {
		case 0:
			convertView.setBackgroundColor(context.getResources().getColor(R.color.green1));
			break;
		case 1:
			convertView.setBackgroundColor(context.getResources().getColor(R.color.green2));
			break;
		case 2:
			convertView.setBackgroundColor(context.getResources().getColor(R.color.green3));
			break;
		default:
			convertView.setBackgroundColor(context.getResources().getColor(R.color.green4));
			break;
		}
		
		return convertView;
	}

}