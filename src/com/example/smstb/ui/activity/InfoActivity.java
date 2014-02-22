package com.example.smstb.ui.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.smstb.R;
import com.example.smstb.SMSReceiver;
import com.example.smstb.R.id;
import com.example.smstb.R.layout;
import com.example.smstb.R.string;
import com.example.smstb.bean.SMSInfo;
import com.example.smstb.iinterface.DismissProgessInterface;
import com.example.smstb.util.Constants;
import com.example.smstb.util.InfoUtil;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class InfoActivity extends FragmentActivity implements DismissProgessInterface{

	private static String TAG="InfoActivity ";
	private TextView personText,infoText,infoPerson,sendInfo;
	private SMSInfo mInfo;
	private Button sendBtn;
	private ProgressBar sendProgressBar;
	private EditText replyEdit;
	private LinearLayout progressLayout;
	String phoneNum;
	String name,reply;
	private List<SMSInfo> mInfos=new ArrayList<SMSInfo>();
	private SMSReceiver receiver;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_info);
		mInfo=(SMSInfo) getIntent().getSerializableExtra(Constants.INFO);
		
		personText=(TextView) findViewById(R.id.head_center);
		infoText=(TextView) findViewById(R.id.info);
		infoPerson=(TextView) findViewById(R.id.infoPerson);
		sendBtn=(Button) findViewById(R.id.send);
		replyEdit=(EditText) findViewById(R.id.reply);
		sendInfo=(TextView) findViewById(R.id.sendInfo);
		sendProgressBar=(ProgressBar) findViewById(R.id.sendProgress);
		progressLayout=(LinearLayout) findViewById(R.id.progressLayout);
		
		phoneNum=mInfo.getPhoneNum();
		name=mInfo.getName();
		if(phoneNum.equals(name)){
			personText.setText(phoneNum);
		}else{
			personText.setText(name+" "+phoneNum);
		}
		if(mInfo.getType()==1){
			infoPerson.setText(R.string.me);
			infoText.setText(mInfo.getContent());
		}else if(mInfo.getType()==2){
			infoText.setText("");
			replyEdit.setText(mInfo.getContent());
		}else{
			infoPerson.setText(name+"：");
			infoText.setText(mInfo.getContent());
		}
		
		sendBtn.setOnClickListener(new SendOnClickListener());
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		receiver=new SMSReceiver(this);
		registerReceiver(receiver, new IntentFilter(Constants.SMS_SEND_ACTION));
	}

	protected void onPause(){
		super.onPause();
		if(replyEdit.getText()!=null&&replyEdit.getText().equals("")){
			mInfo.setContent(replyEdit.getText().toString());
			InfoUtil.saveDraft(mInfo,this);
		}else{
			if(mInfo.getType()==2){
				InfoUtil.deleteById(mInfo.getId());
				Toast.makeText(this, R.string.delete_draft, 1000).show();
			}
		}
		unregisterReceiver(receiver);
	}

	class SendOnClickListener implements OnClickListener{

	@Override
		public void onClick(View v) {
			sendInfo();
		}
	}
	
	private void sendInfo(){
		if((replyEdit.getText().toString()).equals("")){
			Log.i(TAG,"null");
			Toast.makeText(this, R.string.not_null,1000).show();
		}else{
			Log.i(TAG,"send");
			reply=replyEdit.getText().toString();
			
			sendProgressBar.setVisibility(View.VISIBLE);
			insertInfo(phoneNum,reply);
			
			Intent itSend=new Intent(Constants.SMS_SEND_ACTION);
			sendBroadcast(itSend);
			Intent itDeliver=new Intent(Constants.SMS_DELIVERED_ACTION);
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
		InfoUtil.getSmsContent();
	}
	
	public void dismissProgress(){
		Log.i(TAG,"diss");
		sendProgressBar.setVisibility(View.GONE);
		sendInfo.setText(reply);
		progressLayout.setVisibility(View.VISIBLE);
	}
}
