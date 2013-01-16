package com.example.smstb;

import java.util.ArrayList;
import java.util.List;

import android.app.TabActivity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

public class ItemActivity extends TabActivity {

	private TabHost tabhost;
	private List<SMSInfo> mInfos = new ArrayList<SMSInfo>();
	private SMSInfo info;
	private LayoutInflater mInflater;

	private ListView inboxListView, outboxListView;
	private TextView infoTextView;
	private Boolean mIsInfo, mIsInbox;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		mIsInfo = intent.getBooleanExtra(MainActivity.IS_INFO, true);
		mIsInbox = intent.getBooleanExtra(MainActivity.IS_INBOX, true);
		if (!mIsInfo) {
			if (mIsInbox) {
				mInfos = (List<SMSInfo>) intent.getSerializableExtra("infos");
			} else {
				mInfos = (List<SMSInfo>) intent.getSerializableExtra("infos");
			}
		} else {
			info = (SMSInfo) intent.getSerializableExtra("info");
		}

		tabhost = getTabHost();
		mInflater = LayoutInflater.from(this);
		mInflater.inflate(R.layout.layout_main, tabhost.getTabContentView());
		findView();
		if (mIsInfo) {
			infoTextView.setText(info.getContent());

			String phoneNum = info.getPhoneNum();
			if (!getContactByPhone(phoneNum).equals(phoneNum)) {
				phoneNum = getContactByPhone(phoneNum) + "\n" + phoneNum;
			}
			tabhost.addTab(tabhost.newTabSpec("tab").setIndicator(phoneNum).setContent(R.id.inbox));
		} else {
			SMSInfoAdapter adapter = new SMSInfoAdapter(mInfos, this);
			inboxListView.setAdapter(adapter);
			inboxListView.setOnItemClickListener(new InfoOnItemClickListener());
			outboxListView.setVisibility(View.GONE);
			String phoneNum = mInfos.get(0).getPhoneNum();
			if (getContactByPhone(phoneNum).equals(phoneNum)) {
				tabhost.addTab(tabhost.newTabSpec("tab").setIndicator(mInfos.get(0).getPhoneNum())
						.setContent(R.id.inbox));
			} else {
				phoneNum = getContactByPhone(phoneNum) + "\n" + phoneNum;
				tabhost.addTab(tabhost.newTabSpec("tab").setIndicator(phoneNum)
						.setContent(R.id.inbox));
			}
		}

		setContentView(tabhost);
	}

	private void findView() {
		if (mIsInfo) {
			infoTextView = (TextView) findViewById(R.id.info);
		} else {
			inboxListView = (ListView) findViewById(R.id.inbox);
			outboxListView = (ListView) findViewById(R.id.outbox);
		}
	}

	class InfoOnItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// TODO Auto-generated method stub
			Intent infoIntent = new Intent();
			infoIntent.setClass(ItemActivity.this, ItemActivity.class);
			infoIntent.putExtra("info", mInfos.get(position));
			infoIntent.putExtra(MainActivity.IS_INFO, true);
			startActivity(infoIntent);
		}
	}

	public String getContactByPhone(String phoneNum) {
		String name = phoneNum;
		Uri personUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
				phoneNum);
		Cursor cursor = getContentResolver().query(personUri,
				new String[] { PhoneLookup.DISPLAY_NAME }, null, null, null);
		if (cursor.moveToFirst()) {
			int nameId = cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME);
			name = cursor.getString(nameId);
		}
		return name;
	}
}
