package net.sareweb.android.txotx.plus;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;

public class PlusConnectionCallbacks implements ConnectionCallbacks {

	@Override
	public void onConnected(Bundle arg0) {
		Log.d("AAAA", "onConnected");

	}

	@Override
	public void onDisconnected() {
		Log.d("AAAA", "onDisconnected");
	}

}
