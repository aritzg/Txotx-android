package net.sareweb.android.txotx.notification;

import net.sareweb.android.txotx.R;
import net.sareweb.android.txotx.activity.OharraActivity_;
import net.sareweb.android.txotx.activity.SagardotegiDetailActivity_;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.app.TaskStackBuilder;

public class TxotxNotifications {
	
	public static void showDeviceResgistration(Context context, Intent intent){
		 Builder builder = new NotificationCompat.Builder(context);
		 builder.setContentTitle("Txootx! aipuak");
		 builder.setSmallIcon(R.drawable.notification);
		 builder.setContentText("@izena jarriz aipuak egin daitezke!");
		 Notification notification = builder.build();
		
		 NotificationManager mNotificationManager =
			        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		 mNotificationManager.notify(0,notification);
	}
	
	public static void showMention(Context context, Intent intent){
		String testua = intent.getExtras().getString("testua");
		String sagardotegiIzena = intent.getExtras().getString("sagardotegiIzena");
		String sagardotegiId = intent.getExtras().getString("sagardotegiId");
		long sagardotegiIdLng = Long.parseLong(sagardotegiId);
		
		String nork = intent.getExtras().getString("nork");
		
		 Builder builder = new NotificationCompat.Builder(context);
		 builder.setContentTitle(nork +"(e)k " + sagardotegiIzena + "(e)n");
		 builder.setSmallIcon(R.drawable.notification);
		 builder.setContentText(testua);
		 
		 Intent detailIntent = SagardotegiDetailActivity_.intent(context).sagardotegiId(sagardotegiIdLng).get();
		 
		 TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		 stackBuilder.addParentStack(SagardotegiDetailActivity_.class);
		 stackBuilder.addNextIntent(detailIntent);
		 PendingIntent resultPendingIntent =
			        stackBuilder.getPendingIntent(
			            0,
			            PendingIntent.FLAG_UPDATE_CURRENT
			        );
		 builder.setContentIntent(resultPendingIntent);
		 
		 Notification notification = builder.build();
		
		 NotificationManager mNotificationManager =
			        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		 mNotificationManager.notify((int)sagardotegiIdLng, notification);
	}
	
	public static void showOharra(Context context, Intent intent){
		String izenburua = intent.getExtras().getString("izenburua");
		String oharraId = intent.getExtras().getString("oharraId");
		long oharraIdLng = Long.parseLong(oharraId);
		
		Builder builder = new NotificationCompat.Builder(context);
		builder.setContentTitle("Txootx! Oharra!");
		builder.setSmallIcon(R.drawable.notification);
		builder.setContentText(izenburua);
		 
		Intent detailIntent = OharraActivity_.intent(context).oharraId(oharraIdLng).get();
		 
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		stackBuilder.addParentStack(OharraActivity_.class);
		stackBuilder.addNextIntent(detailIntent);
		PendingIntent resultPendingIntent =
			        stackBuilder.getPendingIntent(
			            0,
			            PendingIntent.FLAG_UPDATE_CURRENT
			        );
		 builder.setContentIntent(resultPendingIntent);
		 
		 Notification notification = builder.build();
		
		 NotificationManager mNotificationManager =
			        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		 mNotificationManager.notify((int)oharraIdLng, notification);
	}

}
