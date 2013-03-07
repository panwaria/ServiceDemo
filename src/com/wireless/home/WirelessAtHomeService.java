package com.wireless.home;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

public class WirelessAtHomeService extends Service 
{
  private final IBinder mBinder = new MyBinder();
  private ArrayList<String> mMACIDList = new ArrayList<String>();
  MediaPlayer player;

  public void onCreate()
  {
	  // Set the Player
      player = MediaPlayer.create(this, R.raw.sample);
      player.setLooping(false);
      
      // Display notification in the notification bar
      showNotification();
      
      Toast.makeText(this, "Service Created", Toast.LENGTH_SHORT).show();
  }
  
  public void onDestroy()
  {
	  // Remove notification from the Notification bar.
	  nm.cancel(R.string.hello_world);
	  
	  // Stop the player
      player.stop();
      
      Toast.makeText(this, "Service Destroyed", Toast.LENGTH_SHORT).show();
  }
  
  @Override
  public int onStartCommand(Intent intent, int flags, int startId) 
  {
	  if(intent == null)
		  return Service.START_STICKY;
		  
	  player.start();
	  
	  // NOTE: You can add your API here.
	  Random random = new Random();
	  
	  if (random.nextBoolean()) 
		  mMACIDList.add("01:23:45:67:89:ab");
	  
	  if (random.nextBoolean()) 
		  mMACIDList.add("ab:cd:ef:ab:cd:ef");
	  
	  if (random.nextBoolean()) 
		  mMACIDList.add("98:76:54:32:10:fe");
	  
	  if (random.nextBoolean()) 
		  mMACIDList.add("ab:ab:ab:12:12:12");
	  
	  if (mMACIDList.size() >= 20) 
		  mMACIDList.remove(0);

	  return Service.START_STICKY;
  	}

  @Override
  public IBinder onBind(Intent arg0) 
  {
	  return mBinder;
  }

  public class MyBinder extends Binder 
  {
	  WirelessAtHomeService getService() 
	  {
		  return WirelessAtHomeService.this;
	  }
  }

  public List<String> getMACIDList() 
  {
	  return mMACIDList;
  }
  
  private NotificationManager nm;
  private void showNotification() 
  {
      nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
      
      // In this sample, we'll use the same text for the ticker and the expanded notification
      CharSequence text = "Tap to go to application & stop the service.";
      
      // Set the icon, scrolling text and timestamp
      Notification notification = new Notification(R.drawable.icon_40x40, text, System.currentTimeMillis());
       
      // The PendingIntent to launch our activity if the user selects this notification
      Intent intent = new Intent(this, MyListActivity.class);
      
      PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, Notification.FLAG_ONGOING_EVENT);
      
      notification.flags = notification.flags | Notification.FLAG_ONGOING_EVENT;
      // Set the info for the views that show in the notification panel.
      notification.setLatestEventInfo(getApplicationContext(), "Wireless@Home", text, contentIntent);
      
      // Send the notification.
      // We use a layout id because it is a unique number. We use it later to cancel.
      nm.notify(R.string.hello_world, notification);
  }

} 