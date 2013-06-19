package net.sareweb.android.txotx.adapter;

import java.util.List;

import net.sareweb.android.txotx.R;
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
	
	public SagardoEgunAdapter(Context context, List<SagardoEgun> sagardoEgunak){
		this.context = context;
		this.sagardoEgunak = sagardoEgunak; 
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
		
		convertView.setTag(sagardoEgunak.get(position));
		return convertView;
	}

}
