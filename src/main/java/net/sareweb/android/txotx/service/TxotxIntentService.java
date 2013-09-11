package net.sareweb.android.txotx.service;

import net.sareweb.android.txotx.model.GoogleDevice;
import net.sareweb.android.txotx.notification.TxotxNotifications;
import net.sareweb.android.txotx.rest.GoogleDeviceRESTClient;
import net.sareweb.android.txotx.rest.TxotxConnectionData;
import net.sareweb.android.txotx.util.AccountUtil;
import net.sareweb.android.txotx.util.TxotxPrefs_;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

import com.googlecode.androidannotations.annotations.EService;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

@EService
public class TxotxIntentService extends IntentService {

	private static final String TAG = "TxotxIntentService";
	private static PowerManager.WakeLock sWakeLock;
    private static final Object LOCK = TxotxIntentService.class;
    private GoogleDeviceRESTClient googleDeviceRESTClient;
    private static Context context;
	
    @Pref TxotxPrefs_ prefs;
    
    public TxotxIntentService() {
		super("txootx");
	}
    
    public static void runIntentInService(Context ctx, Intent intent) {
    	context=ctx;
        synchronized(LOCK) {
            if (sWakeLock == null) {
                PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
                sWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "txotx_wakelock");
            }
        }
        sWakeLock.acquire();
        intent.setClassName(context, TxotxIntentService_.class.getName());
        context.startService(intent);
    }
    
    @Override
    public final void onHandleIntent(Intent intent) {
    	Log.d(TAG, "onHandleIntent");
        try {
            String action = intent.getAction();
            if (action.equals("com.google.android.c2dm.intent.REGISTRATION")) {
                handleRegistration(intent);
            } else if (action.equals("com.google.android.c2dm.intent.RECEIVE")) {
                handleMessage(intent);
            }
        } finally {
            synchronized(LOCK) {
                sWakeLock.release();
            }
        }
    }

	private void handleMessage(Intent intent) {
		String messageType = intent.getExtras().getString("messageType");
		if(messageType==null) return;
		if(messageType.equals("registration")){
			TxotxNotifications.showDeviceResgistration(this, intent);
		}
		else if(messageType.equals("mention") || messageType.equals("followed")){
			Log.d(TAG, "Handling mention");
			TxotxNotifications.showMezua(this, intent);
		}
		else if(messageType.equals("oharra")){
			Log.d(TAG, "Handling oharra");
			TxotxNotifications.showOharra(this, intent);
		}
		
	}

	private void handleRegistration(Intent intent) {
		try {
			String error = intent.getExtras().getString("error");
			String registration_id = intent.getExtras().getString("registration_id");
			String unregistered = intent.getExtras().getString("unregistered");
			
			googleDeviceRESTClient = new GoogleDeviceRESTClient(new TxotxConnectionData(context));
			Log.d(TAG, "Handling registration");
			if(error==null){
				if(registration_id!=null){
					GoogleDevice googleDevice = googleDeviceRESTClient.addGoogeDevice(AccountUtil.getGoogleEmail(context) , registration_id);
					//prefs.registrationId().put(googleDevice.getRegistrationId());
				}
				/*else if(unregistered != null) {
					Log.d(TAG, "registration_id: " + registration_id);
					googleDeviceRESTClient.unregisterGoogleDevice(prefs.registrationId().get());
				}*/
			}
		} catch (Exception e) {
			Log.e(TAG, "Error handling registration",e);
		}
	}
	

}
