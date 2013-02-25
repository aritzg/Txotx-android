package net.sareweb.android.txotx.receiver;

import net.sareweb.android.txotx.service.TxotxIntentService_;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class TxotxReceiver extends BroadcastReceiver {

	
	@Override
    public final void onReceive(Context context, Intent intent) {
        TxotxIntentService_.runIntentInService(context, intent);
        setResult(Activity.RESULT_OK, null, null);
    }

}
