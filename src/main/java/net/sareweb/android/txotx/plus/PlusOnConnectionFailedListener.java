package net.sareweb.android.txotx.plus;

import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;

public class PlusOnConnectionFailedListener implements
		OnConnectionFailedListener {

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		Log.d("BBBB", "onConnectionFailed");
	}

}
