package com.example.smstb.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.gsm.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.smstb.R;
import com.example.smstb.bean.Contact;
import com.example.smstb.bean.SMSInfo;
import com.example.smstb.util.Constants;
import com.example.smstb.util.InfoUtil;

public class NewActivity extends BaseActivity {
	private EditText contactEditText;
	private EditText replyEditText;
	private Button sendBtn, contactBtn;
	private String contact;
	private String reply;
	private SMSInfo info = new SMSInfo();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_info);

		findView();
		if (brain.getContacts().size() == 0) {
			brain.setContacts(InfoUtil.getContacts());
		}
//		autoCompleteTextView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//				String auto=autoCompleteTextView.getText().toString().trim();
//				if(!auto.endsWith(",")){
//					auto+=",";
//				}
//				auto+=brain.getContacts().get(arg2).getName()+",";
//				autoCompleteTextView.setText(auto);
//			}
//			
//		});
		contactEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String str=s.toString();
				//count==0,delete,or add
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		sendBtn.setOnClickListener(new BtnOnClickListener());
		contactBtn.setOnClickListener(new BtnOnClickListener());
	}

	private void findView() {
		contactEditText = (EditText) findViewById(R.id.contact);
		replyEditText = (EditText) findViewById(R.id.reply);
		sendBtn = (Button) findViewById(R.id.send);
		contactBtn = (Button) findViewById(R.id.contactBtn);
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		Log.i(TAG, "arg0:" + arg0 + ",arg1:" + arg1);
		switch (arg1) {
		case RESULT_OK:
			String name = "";
			List<Contact> selectedContacts = brain.getSelectedContacts();
			for (int i = 0; i < selectedContacts.size(); i++) {
				name += selectedContacts.get(i).getName();
				name += ",";
			}
			contactEditText.setText(name);
			contactEditText.setSelection(name.length());
			break;
		default:
			break;
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (contactEditText.getText().equals("")
				|| replyEditText.getText().toString().trim().equals("")) {
			return;
		}
		info.setBody(contactEditText.getText().toString());
		info.setAddress(replyEditText.getText().toString().trim());
		InfoUtil.insertDraft(info, this);
	}

	class BtnOnClickListener implements OnClickListener {

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

	private void sendInfo() {
		reply = replyEditText.getText().toString();
		contact = contactEditText.getText().toString();
		if (contact.trim().equals("")) {
			Toast.makeText(this, R.string.phonenum_not_null, 1000).show();
		} else if (reply.equals("")) {
			Toast.makeText(this, R.string.not_null, 1000).show();
		} else {
			String[] phoneNums = contact.split(",");
			for (int i = 0; i < phoneNums.length; i++) {
				contact = phoneNums[i];
				insertInfo(contact, reply);
				Intent itSend = new Intent(Constants.SMS_SEND_ACTION);

				itSend.putExtra(Constants.NAME, contact);
				Intent itDeliver = new Intent(Constants.SMS_DELIVERED_ACTION);
				PendingIntent sendPi = PendingIntent.getActivity(this, 0,
						itSend, 0);
				PendingIntent deliverPi = PendingIntent.getActivity(this, 0,
						itDeliver, 0);
				SmsManager manager = SmsManager.getDefault();
				manager.sendTextMessage(contact, null, reply, sendPi, deliverPi);
				replyEditText.setText("");
				Toast.makeText(this, R.string.have_send, 1000).show();
			}

		}
	}

	private void toContact() {
		Intent intent = new Intent();
		intent.setClass(this, ContactActivity.class);
		startActivityForResult(intent, 0);
	}

	private void insertInfo(String contact, String reply) {
		String ADDRESS = "address";
		String DATE = "date";
		String READ = "read";
		String STATUS = "status";
		String TYPE = "type";
		String BODY = "body";

		ContentValues value = new ContentValues();
		value.put(ADDRESS, contact);
		value.put(DATE, String.valueOf(System.currentTimeMillis()));
		value.put(READ, "1");
		value.put(STATUS, "-1");
		value.put(TYPE, "2");
		value.put(BODY, reply);

		getContentResolver().insert(Uri.parse("content://sms"), value);
	}

	private List<String> getAutoStrList() {
		List<String> autoStrList = new ArrayList<String>();
		for (Contact contact : brain.getContacts()) {
			if(contact.getName().equals(contact.getPhoneNum())){
				autoStrList.add(contact.getName());
			}else{
				autoStrList.add(contact.getName()+" "+contact.getPhoneNum());
			}
		}
		return autoStrList;
	}
}
