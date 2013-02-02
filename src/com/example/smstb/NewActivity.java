package com.example.smstb;

import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.telephony.gsm.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewActivity extends FragmentActivity{
	
	private static final String TAG="NewActivity";
	private AutoCompleteTextView autoCompleteTextView;
	private EditText replyEditText;
	private Button sendBtn;
	private String contact;
	private String reply;
	private SMSInfo info=new SMSInfo();

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_info);
		
		findView();
		sendBtn.setOnClickListener(new SendOnClickListener());
	}
	private void findView(){
		autoCompleteTextView=(AutoCompleteTextView) findViewById(R.id.contact);
		replyEditText=(EditText) findViewById(R.id.reply);
		sendBtn=(Button) findViewById(R.id.send);
	}
	
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(autoCompleteTextView.getText().equals("")&&replyEditText.getText().toString().trim().equals("")){
			return;
		}
		info.setContent(autoCompleteTextView.getText().toString());
		info.setPhoneNum(replyEditText.getText().toString().trim());
		InfoUtil.insertDraft(info, this);
	}


	class SendOnClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			sendInfo();
		}
	}
	
	private void sendInfo(){
		reply=replyEditText.getText().toString();
		contact=autoCompleteTextView.getText().toString();
		if(contact.trim().equals("")){
			Toast.makeText(this, R.string.phonenum_not_null,1000).show();
		}else if(reply.equals("")){
			Toast.makeText(this, R.string.not_null,1000).show();
		}else{
			insertInfo(contact,reply);
			
			Intent itSend=new Intent(Constants.SMS_SEND_ACTION);
			
			itSend.putExtra(Constants.NAME,contact);
			Intent itDeliver=new Intent(Constants.SMS_DELIVERED_ACTION);
			PendingIntent sendPi=PendingIntent.getActivity(this,0 ,itSend, 0);
			PendingIntent deliverPi=PendingIntent.getActivity(this,0 ,itDeliver, 0);
			SmsManager manager=SmsManager.getDefault();
			manager.sendTextMessage(contact, null, reply, sendPi, deliverPi);
			replyEditText.setText("");
			Toast.makeText(this, R.string.have_send,1000).show();
		}
	}
	
	private void insertInfo(String contact,String reply){
		String ADDRESS="address";
		String DATE="date";
		String READ="read";
		String STATUS="status";
		String TYPE="type";
		String BODY="body";
		
		ContentValues value=new ContentValues();
		value.put(ADDRESS, contact);
		value.put(DATE, String.valueOf(System.currentTimeMillis()));
		value.put(READ, "1");
		value.put(STATUS, "-1");
		value.put(TYPE,"2");
		value.put(BODY, reply);
		
		getContentResolver().insert(Uri.parse("content://sms"), value);
	}
}
