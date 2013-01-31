package com.example.smstb;

import java.util.ArrayList;
import java.util.List;

import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.telephony.gsm.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class NewActivity extends FragmentActivity{

	private static String TAG="InfoActivity ";
	private static final String SMS_SEND_ACTION="SMS_SEND_ACTION";
	private static final String SMS_DELIVERED_ACTION="SMS_DELIVERED_ACTION";
	private TextView personText,infoText,infoPerson;
	private SMSInfo mInfo;
	private Button sendBtn;
	private EditText replyEdit;
	String phoneNum;
	String name;
	private List<SMSInfo> mInfos=new ArrayList<SMSInfo>();
	SMSReceiver smsReceiver;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_info);
		mInfo=(SMSInfo) getIntent().getSerializableExtra(Constants.INFO);
		
		personText=(TextView) findViewById(R.id.head_center);
		infoText=(TextView) findViewById(R.id.info);
		infoPerson=(TextView) findViewById(R.id.infoPerson);
		sendBtn=(Button) findViewById(R.id.send);
		replyEdit=(EditText) findViewById(R.id.reply);
		
		phoneNum=mInfo.getPhoneNum();
		name=mInfo.getName();
		if(phoneNum.equals(name)){
			personText.setText(phoneNum);
		}else{
			personText.setText(name+" "+phoneNum);
		}
		if(mInfo.getType()==1){
			infoPerson.setText(R.string.me);
		}else{
			infoPerson.setText(name+"：");
		}
		infoText.setText(mInfo.getContent());
		
		sendBtn.setOnClickListener(new SendOnClickListener());
	}
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		smsReceiver=new SMSReceiver(this,phoneNum);
		IntentFilter sendFilter=new IntentFilter(Constants.SMS_SEND_ACTION);
		registerReceiver(smsReceiver, sendFilter);
	}

	protected void onPause(){
		super.onPause();
		unregisterReceiver(smsReceiver);
	}

	class SendOnClickListener implements OnClickListener{

	@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			sendInfo();
		}
	}
	
	private void sendInfo(){
		Log.i(TAG,replyEdit.getText().toString()+"here");
		if((replyEdit.getText().toString()).equals("")){
			Log.i(TAG,"null");
			Toast.makeText(this, R.string.not_null,1000).show();
		}else{
			Log.i(TAG,"send");
			String reply=replyEdit.getText().toString();
			
			insertInfo(phoneNum,reply);
			
			Intent itSend=new Intent(SMS_SEND_ACTION);
			Intent itDeliver=new Intent(SMS_DELIVERED_ACTION);
			PendingIntent sendPi=PendingIntent.getActivity(this,0 ,itSend, 0);
			PendingIntent deliverPi=PendingIntent.getActivity(this,0 ,itDeliver, 0);
			SmsManager manager=SmsManager.getDefault();
			manager.sendTextMessage(phoneNum, null, reply, sendPi, deliverPi);
			replyEdit.setText("");
			Toast.makeText(this, R.string.have_send,1000).show();
		}
	}
	private void insertInfo(String phoneNum,String reply){
		String ADDRESS="address";
		String DATE="date";
		String READ="read";
		String STATUS="status";
		String TYPE="type";
		String BODY="body";
		
		ContentValues value=new ContentValues();
		value.put(ADDRESS, phoneNum);
		value.put(DATE, String.valueOf(System.currentTimeMillis()));
		value.put(READ, "1");
		value.put(STATUS, "-1");
		value.put(TYPE,"2");
		value.put(BODY, reply);
		
		getContentResolver().insert(Uri.parse("content://sms"), value);
	}
}
