package net.sareweb.android.txotx.notification;

import net.sareweb.android.txotx.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;

public class TxotxNotifications {
	
	public static void showDeviceResgistration(Context context, Intent intent){
		 Builder builder = new NotificationCompat.Builder(context);
		 builder.setContentTitle("Txootx!");
		 builder.setSmallIcon(R.drawable.notification);
		 builder.setContentText("Gogoan izan iruzkinetan @izena jarri ezkero, pertsona horri oharra iritsiko zaiola. Txootx!");
		 Notification notification = builder.build();
		
		 NotificationManager mNotificationManager =
			        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		 mNotificationManager.notify(0,notification);
	}
	
	public static void showMention(Context context, Intent intent){
		String testua = intent.getExtras().getString("testua");
		String sagardotegiIzena = intent.getExtras().getString("sagardotegiIzena");
		String nork = intent.getExtras().getString("nork");
		 Builder builder = new NotificationCompat.Builder(context);
		 builder.setContentTitle("Txootx! " + nork +"-(e)k " + sagardotegiIzena + "-(e)n");
		 builder.setSmallIcon(R.drawable.notification);
		 builder.setContentText(testua);
		 Notification notification = builder.build();
		
		 NotificationManager mNotificationManager =
			        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		 mNotificationManager.notify(0,notification);
	}

}
