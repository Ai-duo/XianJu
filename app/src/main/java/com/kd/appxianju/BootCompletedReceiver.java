
package com.kd.appxianju;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class BootCompletedReceiver extends BroadcastReceiver
{
	@Override
	public void onReceive(Context context, Intent intent)
	{	 try {
		Thread.sleep(4000);
	} catch (InterruptedException e) {
		e.printStackTrace();
	}
      Intent activity = new Intent(context, MainActivity.class);
      activity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      context.startActivity(activity);
	}
}