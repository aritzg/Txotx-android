package net.sareweb.android.txotx.notification;

import net.sareweb.android.txotx.R;
import net.sareweb.android.txotx.activity.SagardoEgunDetailActivity_;
import net.sareweb.android.txotx.activity.SagardotegiDetailActivity_;
import net.sareweb.android.txotx.activity.TxotxActivity_;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

public class TxotxNotifications {
	
	private static String TAG = "TxotxNotifications";
	
	public static void showDeviceResgistration(Context context, Intent intent){
		 Builder builder = new NotificationCompat.Builder(context);
		 builder.setContentTitle("Txootx!");
		 builder.setSmallIcon(R.drawable.notification);
		 builder.setContentText("Ongi etorri Txootx!-era.");
		 Notification notification = builder.build();
		
		 /*NotificationManager mNotificationManager =
			        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		 mNotificationManager.notify(0,notification);*/
	}
	
	public static void showMezua(Context context, Intent intent){
		String testua = intent.getExtras().getString("testua");
		
		String sagardotegiIzena = intent.getExtras().getString("sagardotegiIzena");
		String sagardotegiId = intent.getExtras().getString("sagardotegiId");
		long sagardotegiIdLng = Long.parseLong(sagardotegiId);
		
		String sagardoEgunIzena = intent.getExtras().getString("sagardoEgunIzena");
		String sagardoEgunId = intent.getExtras().getString("sagardoEgunId");
		long sagardoEgunIdLng = Long.parseLong(sagardoEgunId);
		
		Log.d(TAG, "sagardotegiIzena: " + sagardotegiIzena + " sagardoEgunIzena: " + sagardoEgunIzena);
		
		String nork = intent.getExtras().getString("nork");
		
		 Builder builder = new NotificationCompat.Builder(context);
		 builder.setContentTitle(nork +"(e)k " + sagardotegiIzena + sagardoEgunIzena + "(e)n"); //sagardotegiIzena + sagardoEgunIzena Bietako bat hutsa da
		 builder.setSmallIcon(R.drawable.notification);
		 builder.setContentText(testua);
		 
		 
		 TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		 
		 if(!sagardotegiIzena.equals("")){
			 stackBuilder.addParentStack(SagardotegiDetailActivity_.class);
			 //Intent detailIntent = SagardotegiDetailActivity_.intent(context).sagardotegiId(sagardotegiIdLng).get();
			 Intent detailIntent = TxotxActivity_.intent(context).sagardotegiId(sagardotegiIdLng).get();
			 stackBuilder.addNextIntent(detailIntent);
		 }
		 else if(!sagardoEgunIzena.equals("")){
			 stackBuilder.addParentStack(SagardoEgunDetailActivity_.class);
			 //Intent detailIntent = SagardoEgunDetailActivity_.intent(context).sagardoEgunId(sagardoEgunIdLng).get();
			 Intent detailIntent = TxotxActivity_.intent(context).sagardoEgunId(sagardoEgunIdLng).get();
			 stackBuilder.addNextIntent(detailIntent);
		 }

		 PendingIntent resultPendingIntent =
			        stackBuilder.getPendingIntent(
			            0,
			            PendingIntent.FLAG_UPDATE_CURRENT
			        );
		 builder.setContentIntent(resultPendingIntent);
		 
		 Notification notification = builder.build();
		
		 NotificationManager mNotificationManager =
			        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		 mNotificationManager.notify((int)sagardotegiIdLng + (int)sagardoEgunIdLng, notification); //Bietako bat 0 da
	}
	
	public static void showOharra(Context context, Intent intent){
		String izenburua = intent.getExtras().getString("izenburua");
		String oharraId = intent.getExtras().getString("oharraId");
		long oharraIdLng = Long.parseLong(oharraId);
		
		Builder builder = new NotificationCompat.Builder(context);
		builder.setContentTitle("Txootx! Oharra!");
		builder.setSmallIcon(R.drawable.notification);
		builder.setContentText(izenburua);
		 
		//Intent detailIntent = OharraActivity_.intent(context).oharraId(oharraIdLng).get();
		Intent detailIntent = TxotxActivity_.intent(context).fragmentToBeLoaded(TxotxActivity_.OHARRA_FRAGMENT).oharraId(oharraIdLng).get();
		 
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		stackBuilder.addParentStack(TxotxActivity_.class);
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
	
	/*public static void showFollowed(Context context, Intent intent){
		String testua = intent.getExtras().getString("testua");
		
		String sagardotegiIzena = intent.getExtras().getString("sagardotegiIzena");
		String sagardotegiId = intent.getExtras().getString("sagardotegiId");
		long sagardotegiIdLng = Long.parseLong(sagardotegiId);
		
		String sagardoEgunIzena = intent.getExtras().getString("sagardoEgunIzena");
		String sagardoEgunId = intent.getExtras().getString("sagardoEgunId");
		long sagardoEgunIdLng = Long.parseLong(sagardoEgunId);
		
		String nork = intent.getExtras().getString("nork");
		
		 Builder builder = new NotificationCompat.Builder(context);
		 
		 
		 if(sagardotegiIzena!=null && !sagardotegiIzena.equals("")){
			 builder.setContentTitle(nork +"(e)k " + sagardotegiIzena + "sagardotegian.");
		 }
		 else if(sagardoEgunIzena!=null && !sagardoEgunIzena.equals("")){
			 builder.setContentTitle(nork +"(e)k " + sagardoEgunIzena + "sagardotegian.");
		 }
		 
		 
		 builder.setSmallIcon(R.drawable.notification);
		 builder.setContentText(testua);
		 
		 TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		 
		 if(!sagardotegiIzena.equals("")){
			 stackBuilder.addParentStack(SagardotegiDetailActivity_.class);
			 Intent detailIntent = TxotxActivity_.intent(context).sagardotegiId(sagardotegiIdLng).get();
			 stackBuilder.addNextIntent(detailIntent);
		 }
		 else if(!sagardoEgunIzena.equals("")){
			 stackBuilder.addParentStack(SagardoEgunDetailActivity_.class);
			 Intent detailIntent = TxotxActivity_.intent(context).sagardoEgunId(sagardoEgunIdLng).get();
			 stackBuilder.addNextIntent(detailIntent);
		 }
		 
		 
		 stackBuilder.addParentStack(SagardotegiDetailActivity_.class);
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
	}*/

}
