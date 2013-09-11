package net.sareweb.android.txotx.adapter;

import java.text.SimpleDateFormat;
import java.util.List;

import net.sareweb.android.txotx.R;
import net.sareweb.android.txotx.custom.FollowUnfollowButton;
import net.sareweb.android.txotx.model.Jarraipen;
import net.sareweb.android.txotx.model.SagardoEgun;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SagardoEgunAdapter extends BaseAdapter{

	private Context context;
	private List<SagardoEgun> sagardoEgunak;
	private static String TAG = "SagardoEgunAdapter";
	private SimpleDateFormat sdf;
	
	public SagardoEgunAdapter(Context context, List<SagardoEgun> sagardoEgunak){
		this.context = context;
		this.sagardoEgunak = sagardoEgunak; 
		sdf = new SimpleDateFormat("yy/MM/dd");
	}
	
	@Override
	public int getCount() {
		if(sagardoEgunak!=null)return sagardoEgunak.size();
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if(sagardoEgunak!=null)return sagardoEgunak.get(position);
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
			convertView = inflater.inflate(R.layout.sagardoegun_row, null);
		}
		SagardoEgun sagardoEgun = sagardoEgunak.get(position);
		
		TextView txSagardoEgunDate = (TextView) convertView.findViewById(R.id.txSagardoEgunDate);
		txSagardoEgunDate.setText(sdf.format(sagardoEgun.getHasieraDate()));
		
		TextView txSagardoEgunName = (TextView) convertView.findViewById(R.id.txSagardoEgunName);
		String name=sagardoEgun.getIzena();
		String herria=sagardoEgun.getHerria();
		txSagardoEgunName.setText(name + " (" + herria + ")");
		
		TextView txBB = (TextView) convertView.findViewById(R.id.txBB);
		Double bb = sagardoEgun.getBalorazioenBB();
		txBB.setText(String.format("%.1f", sagardoEgun.getBalorazioenBB()));
		
		TextView txCountPic = (TextView) convertView.findViewById(R.id.txCountPic);
		txCountPic.setText(String.valueOf(sagardoEgun.getIrudiKopurua()));
		
		TextView txCountMsg = (TextView) convertView.findViewById(R.id.txCountMsg);
		txCountMsg.setText(String.valueOf(sagardoEgun.getIruzkinKopurua()));
		
		TextView txCountVal = (TextView) convertView.findViewById(R.id.txCountVal);
		txCountVal.setText(String.valueOf(sagardoEgun.getBalorazioKopurua()));
		
		FollowUnfollowButton btnFollowUnfollow = (FollowUnfollowButton)convertView.findViewById(R.id.btnFollowUnfollow);
		btnFollowUnfollow.setJarraituaIdEtaMota(sagardoEgun.getSagardoEgunId(), Jarraipen.JARRAIPEN_MOTA_SAGARDO_EGUNA);
		
		convertView.setTag(sagardoEgunak.get(position));
		return convertView;
	}

}
