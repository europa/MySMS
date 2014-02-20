package com.example.smstb;

import java.util.ArrayList;
import java.util.List;

import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.telephony.gsm.SmsManager;
import android.util.Log;
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
	private Button sendBtn,contactBtn;
	private String contact;
	private String reply;
	private SMSInfo info=new SMSInfo();
	private List<List<String>> contacts=new ArrayList<List<String>>();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_info);
		
		findView();
		sendBtn.setOnClickListener(new BtnOnClickListener());
		contactBtn.setOnClickListener(new BtnOnClickListener());
	}
	private void findView(){
		autoCompleteTextView=(AutoCompleteTextView) findViewById(R.id.contact);
		replyEditText=(EditText) findViewById(R.id.reply);
		sendBtn=(Button) findViewById(R.id.send);
		contactBtn=(Button) findViewById(R.id.contactBtn);
	}
	
	
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		Log.i(TAG,"arg0:"+arg0+",arg1:"+arg1);
		switch (arg1) {
		case RESULT_OK:
			String phoneNums="";
			contacts=(List<List<String>>) arg2.getSerializableExtra(Constants.CONTACTS);
			Log.i(TAG,"size:"+contacts.size());
			for(int i=0;i<contacts.size();i++){
				if(i==contacts.size()-1){
					phoneNums+=contacts.get(i).get(1);
				}else{
					phoneNums+=contacts.get(i).get(1)+",";
				}
			}
			autoCompleteTextView.setText(phoneNums);
			break;
		default:
			break;
		}
	}
	@Override
	protected void onPause() {
		super.onPause();
		if(autoCompleteTextView.getText().equals("")||replyEditText.getText().toString().trim().equals("")){
			return;
		}
		info.setContent(autoCompleteTextView.getText().toString());
		info.setPhoneNum(replyEditText.getText().toString().trim());
		InfoUtil.insertDraft(info, this);
	}


	class BtnOnClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.send:
				sendInfo();
				break;
			case R.id.contactBtn:
				toContact();
				break;
			default:
				break;
			}
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
			String[] phoneNums=contact.split(",");
			for(int i=0;i<phoneNums.length;i++){
				contact=phoneNums[i];
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
	}
	
	
	private void toContact(){
		Intent intent=new Intent();
		intent.setClass(this, ContactActivity.class);
		startActivityForResult(intent, 0);
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
