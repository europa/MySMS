package com.example.smstb;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.example.smstb.bean.SMSInfo;
import com.example.smstb.ui.activity.InfosPersonActivity;
import com.example.smstb.util.Constants;

public class SMSTestReceiver extends BroadcastReceiver{
	private static final String TAG="SMSTestReceiver";
	public void onReceive(Context context, Intent intent) {
		Log.i(TAG,"con:"+context.toString());
		switch(getResultCode()){
		case Activity.RESULT_OK:
			break;
		}
		Bundle bundle=intent.getExtras();
		Log.i(TAG,"deli");
		NotificationManager nm=(NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
		Notification nn=new Notification();
		nn.flags|=Notification.FLAG_AUTO_CANCEL;
		Object[] pdus=(Object[]) bundle.get("pdus");
		if(pdus!=null&&pdus.length!=0){
			SmsMessage[] messages=new SmsMessage[pdus.length];
			for(int i=0;i<messages.length;i++){
				byte[] pdu=(byte[]) pdus[i];
				messages[i]=SmsMessage.createFromPdu(pdu);
			}	
			for(SmsMessage message:messages){
				Log.i(TAG,TAG);
				Intent sendIntent=new Intent(context,InfosPersonActivity.class);
				SMSInfo info=new SMSInfo();
				
				String phoneNum=message.getDisplayOriginatingAddress();
				String content=message.getMessageBody();
				long date=message.getTimestampMillis();
				info.setBody(content);
				info.setAddress(phoneNum);
				info.setDate(date);
				
				sendIntent.putExtra(Constants.INFO, info);
				
				PendingIntent pendIntent=PendingIntent.getActivity(context, 0, sendIntent, 0);
				nn.icon=R.drawable.ic_launcher;
				nn.tickerText=content;
				nn.defaults=Notification.DEFAULT_SOUND;
				nn.setLatestEventInfo(context, nn.tickerText, nn.tickerText, pendIntent);
				nm.notify(0, nn);
			}
		}
	
	} 
}
