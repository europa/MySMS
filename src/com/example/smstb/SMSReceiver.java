package com.example.smstb;

import java.io.Serializable;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.sax.StartElementListener;
import android.telephony.gsm.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class SMSReceiver extends BroadcastReceiver {
	private static final String TAG="SMSReceiver";
	private Context mContext;
	private String mPhoneNum;
	public SMSReceiver(Context context,String phoneNum){
		mContext=context;
		mPhoneNum=phoneNum;
	}
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if(intent.getAction()==Constants.SMS_SEND_ACTION){
			switch(getResultCode()){
			case Activity.RESULT_OK:
				Toast.makeText(mContext,R.string.send_success,Toast.LENGTH_LONG);
				break;
			case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
				Toast.makeText(mContext,R.string.send_failure,Toast.LENGTH_LONG);
				break;
			}
			Intent toIntent=new Intent(context,InfosPersonActivity.class);
//			toIntent.putExtra("infos", (Serializable)InfoUtil.newInstance(mContext).getInfosByPerson(mPhoneNum));
			context.startActivity(toIntent);
			Log.i(TAG,"here");
		}else if(intent.getAction()==Constants.SMS_DELIVERED_ACTION){
			switch(getResultCode()){
			case Activity.RESULT_OK:
				Toast.makeText(mContext,R.string.send_success,Toast.LENGTH_LONG);
				break;
			case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
				Toast.makeText(mContext,R.string.send_failure,Toast.LENGTH_LONG);
				break;
			}
		}
	}

}
