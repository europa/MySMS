package com.example.smstb;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import android.net.Uri;
import android.os.Bundle;
import android.app.TabActivity;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

public class MainActivity extends TabActivity {
	private final static String TAG = "MainActivity";
	public static final String IS_INFO = "isInfo";
	public static final String IS_INBOX = "isInbox";

	private TabHost tabhost;
	private LayoutInflater mInflater;

	private ListView inboxListView, outboxListView;

	private List<SMSInfo> inboxInfos = new ArrayList<SMSInfo>();
	private List<SMSInfo> outboxInfos = new ArrayList<SMSInfo>();
	private List<ItemInfo> inboxItems = new ArrayList<ItemInfo>();
	private List<ItemInfo> outboxItems = new ArrayList<ItemInfo>();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getSmsContent();

		tabhost = getTabHost();
		mInflater = LayoutInflater.from(this);
		mInflater.inflate(R.layout.layout_main, tabhost.getTabContentView());
		findView();
		inboxItems = getFirstPageItems(inboxInfos);
		outboxItems = getFirstPageItems(outboxInfos);
		ContentAdapter inboxAdapter = new ContentAdapter(inboxItems, this);
		ContentAdapter outboxAdapter = new ContentAdapter(outboxItems, this);
		inboxListView.setAdapter(inboxAdapter);
		outboxListView.setAdapter(outboxAdapter);
		inboxListView.setOnItemClickListener(new ItemClickListener());
		outboxListView.setOnItemClickListener(new ItemClickListener());
		tabhost.addTab(tabhost.newTabSpec("inbox").setIndicator("收件箱").setContent(R.id.inbox));
		tabhost.addTab(tabhost.newTabSpec("outbox").setIndicator("发件箱").setContent(R.id.outbox));
		setContentView(tabhost);

		// tabhost.setOnTabChangedListener(new OnTabChangeListener() {
		//
		// @Override
		// public void onTabChanged(String tabId) {
		// // TODO Auto-generated method stub
		// if (tabId.equals("inbox")) {
		// inboxListView.setOnItemClickListener(new ItemClickListener());
		// } else {
		// outboxListView.setOnItemClickListener(new ItemClickListener());
		// }
		// }
		// });
	}

	private void findView() {
		inboxListView = (ListView) findViewById(R.id.inbox);
		outboxListView = (ListView) findViewById(R.id.outbox);
	}

	class ItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			if (tabhost.getCurrentTabTag().equals("inbox")) {
				Log.i(TAG, "position");
				intent.setClass(MainActivity.this, InfosPersonActivity.class);
				intent.putExtra("infos", inboxItems.get(position).getSmsInfos());
				startActivity(intent);
			} else {
				Log.i(TAG, "outbox:" + position);
				intent.setClass(MainActivity.this, InfosPersonActivity.class);
				intent.putExtra("infos", outboxItems.get(position).getSmsInfos());
				startActivity(intent);
			}
		}
	}

	private String getSmsContent() {
		final String SMS_URI_ALL = "content://sms/";
		final String SMS_URI_INBOX = "content://sms/inbox";
		final String SMS_URI_SEND = "content://sms/send";
		final String SMS_URI_DRFT = "content://sms/draft";
		final String SMS_URI_OUTBOX = "content://sms/outbox";
		final String SMS_URI_FAILED = "content://sms/failed";
		final String SMS_URI_QUEUED = "content://sms/queued";
		StringBuilder smsBuilder = new StringBuilder();
		try {
			Uri uri = Uri.parse(SMS_URI_ALL);
			String[] projection = { "_id", "address", "person", "body", "date", "type" };
			Cursor cursor = getContentResolver().query(uri, projection, null, null, "date desc");
			if (cursor.moveToFirst()) {
				int index_address = cursor.getColumnIndex("address");
				int index_person = cursor.getColumnIndex("person");
				int index_body = cursor.getColumnIndex("body");
				int index_date = cursor.getColumnIndex("date");
				int index_type = cursor.getColumnIndex("type");
				do {
					String address = cursor.getString(index_address);
					String person = cursor.getString(index_person);
					String body = cursor.getString(index_body);
					long date = cursor.getLong(index_date);
					int type = cursor.getInt(index_type);

					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					Date d = new Date(date);
					String dateStr = format.format(d);

					SMSInfo info = new SMSInfo();
					info.setPhoneNum(address);
					info.setTime(dateStr);
					info.setContent(body);
					String strType = "";
					if (type == 1) {
						strType = "接收";
						if (info.getPhoneNum().equals("")) {
						} else {
							inboxInfos.add(info);
						}
					} else if (type == 2) {
						strType = "发送";
						outboxInfos.add(info);
					} else {
						strType = "null";
					}
					smsBuilder.append("[ ");
					smsBuilder.append(address + ", ");
					smsBuilder.append(person + ", ");
					smsBuilder.append(body + ", ");
					smsBuilder.append(dateStr + ", ");
					smsBuilder.append(strType);
					smsBuilder.append(" ]\n\n");

				} while (cursor.moveToNext());
				if (!cursor.isClosed()) {
					cursor.close();
					cursor = null;
				}
			} else {
				smsBuilder.append("no result");
			}
			smsBuilder.append("All");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return smsBuilder.toString();
	}

	private List<ItemInfo> getFirstPageItems(List<SMSInfo> inboxInfos) {
		List<String> phonNums = new ArrayList<String>();
		List<ItemInfo> itemInfos = new ArrayList<ItemInfo>();

		for (int i = 0; i < inboxInfos.size(); i++) {
			if (!phonNums.contains(inboxInfos.get(i).getPhoneNum())) {
				phonNums.add(inboxInfos.get(i).getPhoneNum());
			}
		}
		for (int i = 0; i < phonNums.size(); i++) {
			List<SMSInfo> infos = new ArrayList<SMSInfo>();
			ItemInfo itemInfo = new ItemInfo();
			for (int j = 0; j < inboxInfos.size(); j++) {
				if (phonNums.get(i).equals(inboxInfos.get(j).getPhoneNum())) {
					infos.add(inboxInfos.get(j));
				}
			}
			itemInfo.setSmsInfos(infos);
			itemInfos.add(itemInfo);
		}
		return itemInfos;
	}

}
