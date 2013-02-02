package net.sareweb.android.txotx.adapter;

import java.util.List;

import net.sareweb.android.txotx.R;
import net.sareweb.android.txotx.model.Sagardotegi;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SagardotegiAdapter extends BaseAdapter{

	private Context context;
	private List<Sagardotegi> sagardotegiak;
	private static String TAG = "SagardotegiAdapter";
	
	public SagardotegiAdapter(Context context, List<Sagardotegi> sagardotegiak){
		this.context = context;
		this.sagardotegiak = sagardotegiak; 
	}
	
	@Override
	public int getCount() {
		if(sagardotegiak!=null)return sagardotegiak.size();
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if(sagardotegiak!=null)return sagardotegiak.get(position);
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
			convertView = inflater.inflate(R.layout.sagardotegi_row, null);
		}
		Sagardotegi sagardotegi = sagardotegiak.get(position);
		
		TextView txSagardotegiName = (TextView) convertView.findViewById(R.id.txSagardotegiName);
		String name=sagardotegi.getIzena();
		String herria=sagardotegi.getHerria();
		txSagardotegiName.setText(name + " (" + herria + ")");
		
		TextView txBB = (TextView) convertView.findViewById(R.id.txBB);
		Double bb = sagardotegi.getBalorazioenBB();
		txBB.setText(String.format("%.1f", sagardotegi.getBalorazioenBB()));
		
		convertView.setTag(sagardotegiak.get(position));
		return convertView;
	}

}
