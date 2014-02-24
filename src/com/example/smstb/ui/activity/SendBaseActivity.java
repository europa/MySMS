package com.example.smstb.ui.activity;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.gsm.SmsManager;

import com.example.smstb.R;
import com.example.smstb.bean.SMSInfo;
import com.example.smstb.util.Constants;
import com.example.smstb.util.InfoUtil;

public class SendBaseActivity extends BaseActivity {
	public SMSInfo info = new SMSInfo();
	public SMSInfo draft =null;
	BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			context.unregisterReceiver(receiver);
			String action = intent.getAction();
			if (action.equals(Constants.SMS_SEND_ACTION)) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					getMe().toast(R.string.send_success);
					info.setType(Constants.SENT);
					InfoUtil.insertInfo(getMe(), info);
					break;
				default:
					getMe().toast(R.string.send_failure);
					info.setType(Constants.FAILED);
					InfoUtil.insertInfo(getMe(), info);
					break;
				}
			} else if (action.equals(Constants.SMS_DELIVERED_ACTION)) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:

					break;

				default:
					break;
				}
			}
		}
	};

	public void sendInfo(SMSInfo info){
		Intent sendIntent = new Intent(Constants.SMS_SEND_ACTION);
		Intent deliverIntent = new Intent(Constants.SMS_DELIVERED_ACTION);
		PendingIntent sendPi = PendingIntent.getBroadcast(this,Constants.CODE,sendIntent,Constants.CODE);
		PendingIntent deliverPi = PendingIntent.getBroadcast(this, Constants.CODE,
				deliverIntent, Constants.CODE);
		SmsManager manager = SmsManager.getDefault();
		IntentFilter filter=new IntentFilter();
		filter.addAction(Constants.SMS_DELIVERED_ACTION);
		filter.addAction(Constants.SMS_SEND_ACTION);
		registerReceiver(receiver, filter);
		manager.sendTextMessage(info.getAddress(), null, info.getBody(), sendPi, deliverPi);
	}
	private BaseActivity getMe() {
		return (BaseActivity) this;
	}
}
