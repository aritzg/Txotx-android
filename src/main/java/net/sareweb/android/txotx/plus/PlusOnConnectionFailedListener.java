package net.sareweb.android.txotx.plus;

import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;

public class PlusOnConnectionFailedListener implements
		OnConnectionFailedListener {
	
	private static final String TAG="PlusOnConnectionFailedListener";
	
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		
		Log.d(TAG, "onConnectionFailed error code " + result.getErrorCode());
	}

}
