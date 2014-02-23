package com.example.smstb.ui.activity;

import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.gsm.SmsManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smstb.R;
import com.example.smstb.bean.SMSInfo;
import com.example.smstb.ui.adapter.SMSInfoAdapter;
import com.example.smstb.util.Constants;
import com.example.smstb.util.InfoUtil;

public class InfosPersonActivity extends BaseActivity {
	private TextView personText;
	private ListView infoList;
	SMSInfoAdapter smsInfoAdapter;
	private ImageButton contactImgBtn, sendImgBtn;
	EditText replyEditText;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_infos);

		personText = (TextView) findViewById(R.id.head_center);
		infoList = (ListView) findViewById(R.id.infos);
		contactImgBtn = (ImageButton) findViewById(R.id.contactImgBtn);
		sendImgBtn = (ImageButton) findViewById(R.id.sendImgBtn);
		replyEditText = (EditText) findViewById(R.id.replyEdit);

		smsInfoAdapter = new SMSInfoAdapter(InfoUtil.queryByThreadId(brain
				.getCurrentThreadId()), this);
		infoList.setAdapter(smsInfoAdapter);
		personText.setText(brain.getCurrentName());
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.contactImgBtn:

			break;
		case R.id.sendImgBtn:
			sendInfo();
			break;

		default:
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	class InfoOnItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent intent = new Intent();
			intent.setClass(InfosPersonActivity.this, InfoActivity.class);
			intent.putExtra(Constants.INFO,
					(SMSInfo) smsInfoAdapter.getItem(position));
			startActivity(intent);
		}
	}

	private void sendInfo() {
		String reply = replyEditText.getText().toString();
		if (reply.equals("")) {
			Toast.makeText(this, R.string.not_null, 1000).show();
		} else {
			for (String id:brain.getCurrentConversation().getRecipient_ids().split(" ")) {
				String phoneNum=InfoUtil.getPhoneNum(Integer.parseInt(id));
				insertInfo(phoneNum, reply);
				Intent itSend = new Intent(Constants.SMS_SEND_ACTION);

				itSend.putExtra(Constants.NAME,phoneNum );
				Intent itDeliver = new Intent(Constants.SMS_DELIVERED_ACTION);
				PendingIntent sendPi = PendingIntent.getActivity(this, 0,
						itSend, 0);
				PendingIntent deliverPi = PendingIntent.getActivity(this, 0,
						itDeliver, 0);
				SmsManager manager = SmsManager.getDefault();
				manager.sendTextMessage(phoneNum, null, reply, sendPi, deliverPi);
				replyEditText.setText("");
				Toast.makeText(this, R.string.have_send, 1000).show();
			}

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
