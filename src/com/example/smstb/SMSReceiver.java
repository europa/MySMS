package com.example.smstb;

import java.text.SimpleDateFormat;
import java.util.Date;

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

public class SMSReceiver extends BroadcastReceiver{
	private static final String TAG="SMSReceiver";
	private DismissProgessInterface dismissProgessInterface;
	
	public SMSReceiver(DismissProgessInterface dismissProgessInterface){
		this.dismissProgessInterface=dismissProgessInterface;
	}
	public void onReceive(Context context, Intent intent) {
		switch(getResultCode()){
		case Activity.RESULT_OK:
			dismissProgessInterface.dismissProgress();
			break;
		}
	} 
}
