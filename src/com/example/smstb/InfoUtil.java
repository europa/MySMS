package com.example.smstb;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.util.Log;
import android.widget.Toast;

public class InfoUtil {
	private static final String TAG = "InfoUtil";
	public static InfoUtil infoUtil = new InfoUtil();
	private static Context mContext;
	private static List<SMSInfo> infos = new ArrayList<SMSInfo>();

	public static InfoUtil newInstance(Context context) {
		mContext = context;
		if (infoUtil == null) {
			return new InfoUtil();
		}
		return infoUtil;
	}

	/*
	 * 暂时不用这个
	 */

	public static String getSmsContent() {
		Log.i(TAG, "before get content:" + System.currentTimeMillis());
		infos.clear();
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
			String[] projection = { "_id", "thread_id", "address", "person",
					"body", "date", "type" };
			Cursor cursor = mContext.getContentResolver().query(uri,
					projection, null, null, "date desc");
			if (cursor.moveToFirst()) {
				int index_id = cursor.getColumnIndex("_id");
				int index_address = cursor.getColumnIndex("address");
				int index_thread = cursor.getColumnIndex("thread_id");
				int index_person = cursor.getColumnIndex("person");
				int index_body = cursor.getColumnIndex("body");
				int index_date = cursor.getColumnIndex("date");
				int index_type = cursor.getColumnIndex("type");
				do {
					int id = cursor.getInt(index_id);
					String address = cursor.getString(index_address);
					String person = cursor.getString(index_person);
					String body = cursor.getString(index_body);
					long date = cursor.getLong(index_date);
					int type = cursor.getInt(index_type);
					long thread = cursor.getLong(index_thread);
					Log.i(TAG, "id:" + id + ",thread:" + thread + ",ad:"
							+ address + ",person:" + person + ",body:" + body
							+ ",date:" + date + ",type:" + type);
					SimpleDateFormat format = new SimpleDateFormat(
							"yyyy-MM-dd hh:mm:ss");
					Date d = new Date(date);
					String dateStr = format.format(d);

					SMSInfo info = new SMSInfo();
					if (type == 1) {// 发送
						info.setType(0);
					} else if (type == 2) {// 接收
						info.setType(1);
					} else if (type == 3) {// 草稿
						info.setType(2);
						address = queryById(id);
						Log.i(TAG, "address:" + address);
					} else {
						// Log.i(TAG,"type:"+type);
						continue;
					}
					info.setId(id);
					info.setoTime(date);
					info.setPhoneNum(address);
					info.setTime(dateStr);
					info.setContent(body);
					info.setThread_id(thread);
					String strType = "";
					if (address == null) {
						Log.i(TAG, "==body:" + body + ",date:" + date
								+ ",type:" + type);
						continue;
					}
					if (address.equals("")) {
						Log.i(TAG, "eqbody:" + body + ",date:" + date
								+ ",type:" + type);
						continue;
					}
					infos.add(info);
				} while (cursor.moveToNext());
				if (!cursor.isClosed()) {
					cursor.close();
					cursor = null;
				}
			} else {
				smsBuilder.append("no result");
			}
			for (int i = 0; i < infos.size(); i++) {
				String name = infos.get(i).getPhoneNum();
				Uri personUri = Uri.withAppendedPath(
						ContactsContract.PhoneLookup.CONTENT_FILTER_URI, name);
				cursor = mContext.getContentResolver().query(personUri,
						new String[] { PhoneLookup.DISPLAY_NAME }, null, null,
						null);
				if (cursor.moveToFirst()) {
					int nameId = cursor
							.getColumnIndex(PhoneLookup.DISPLAY_NAME);
					String phoneName = cursor.getString(nameId);
					if (infos.get(i).getPhoneNum().equals("10086")) {
					}
					if (phoneName == null || phoneName.equals("")) {
						infos.get(i).setName(name);
					} else {
						infos.get(i).setName(phoneName);
					}
				} else {
					// 有号码，但不在联系人中的
					infos.get(i).setName(name);
				}
				cursor.close();
			}
			smsBuilder.append("All");
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.i(TAG, "after get content:" + System.currentTimeMillis());
		return smsBuilder.toString();
	}

	public static List<ItemInfo> getInfosInPerson() {
		Log.i(TAG, "before get in:" + System.currentTimeMillis());
		final String SMS_URI_ALL = "content://sms/";
		List<Long> threadIds = new ArrayList<Long>();
		List<ItemInfo> itemInfos = new ArrayList<ItemInfo>();
		try {
			Uri uri = Uri.parse(SMS_URI_ALL);
			String[] projection = { "_id", "thread_id", "address", "body",
					"date", "type" };
			Cursor cursor = mContext.getContentResolver().query(uri,
					projection, null, null, "date desc");
			if (cursor.moveToFirst()) {
				int index_id = cursor.getColumnIndex("_id");
				int index_address = cursor.getColumnIndex("address");
				int index_thread = cursor.getColumnIndex("thread_id");
				int index_body = cursor.getColumnIndex("body");
				int index_date = cursor.getColumnIndex("date");
				int index_type = cursor.getColumnIndex("type");
				do {

					long thread = cursor.getLong(index_thread);
					SMSInfo info = new SMSInfo();
					ItemInfo itemInfo = new ItemInfo();
					if (threadIds.contains(thread)) {
						itemInfo = itemInfos.get(threadIds.indexOf(thread));
						int amount = itemInfo.getAmount() + 1;
						itemInfos.get(threadIds.indexOf(thread)).setAmount(
								amount);
						continue;
					}
					int id = cursor.getInt(index_id);
					String address = cursor.getString(index_address);
					String body = cursor.getString(index_body);
					long date = cursor.getLong(index_date);
					int type = cursor.getInt(index_type);
					Log.i(TAG, "id:" + id + ",thread:" + thread + ",ad:"
							+ address + ",body:" + body + ",date:" + date
							+ ",type:" + type);
					SimpleDateFormat format = new SimpleDateFormat(
							"yyyy-MM-dd hh:mm:ss");
					Date d = new Date(date);
					String dateStr = format.format(d);
					if (type == 1) {// 发送
						info.setType(0);
					} else if (type == 2) {// 接收
						info.setType(1);
					} else if (type == 3) {// 草稿
						info.setType(2);
						address = queryById(id);
						Log.i(TAG, "address:" + address);
					} else {
						// Log.i(TAG,"type:"+type);
						continue;
					}

					threadIds.add(thread);
					info.setId(id);
					info.setoTime(date);
					info.setPhoneNum(address);
					info.setTime(dateStr);
					info.setContent(body);
					info.setThread_id(thread);
					if (address == null) {
						Log.i(TAG, "==body:" + body + ",date:" + date
								+ ",type:" + type);
						continue;
					}
					if (address.equals("")) {
						Log.i(TAG, "eqbody:" + body + ",date:" + date
								+ ",type:" + type);
						continue;
					}

					itemInfo.setSmsInfo(info);
					itemInfo.setAmount(1);
					itemInfos.add(itemInfo);
				} while (cursor.moveToNext());
				if (!cursor.isClosed()) {
					cursor.close();
					cursor = null;
				}

			} else {
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (int i = 0; i < itemInfos.size(); i++) {
			String name = itemInfos.get(i).getSmsInfo().getPhoneNum();
			Uri personUri = Uri.withAppendedPath(
					ContactsContract.PhoneLookup.CONTENT_FILTER_URI, name);
			Cursor cursor = mContext.getContentResolver()
					.query(personUri,
							new String[] { PhoneLookup.DISPLAY_NAME }, null,
							null, null);
			if (cursor.moveToFirst()) {
				int nameId = cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME);
				String phoneName = cursor.getString(nameId);
				if (phoneName == null || phoneName.equals("")) {
					itemInfos.get(i).getSmsInfo().setName(name);
				} else {
					itemInfos.get(i).getSmsInfo().setName(phoneName);
				}
			} else {
				// 有号码，但不在联系人中的
				itemInfos.get(i).getSmsInfo().setName(name);
			}
			Log.i(TAG, "name:" + itemInfos.get(i).getSmsInfo().getName());
			cursor.close();
		}
		Log.i(TAG, "after get in:" + System.currentTimeMillis());
		return itemInfos;
	}

	public static List<ItemInfo> getItemsByPerson() {
		List<ItemInfo> itemInfos = new ArrayList<ItemInfo>();
		Uri MMS_SMS_URI = Uri.parse("content://mms-sms");
		Uri MSG_QUERY_URI = Uri.withAppendedPath(MMS_SMS_URI, "conversations")
				.buildUpon().appendQueryParameter("simple", "true").build();
		String[] projection = { "_id", "recipient_ids", "snippet", "date",
				"has_attachment", "snippet_cs", "message_count" };
		Cursor cursor = mContext.getContentResolver().query(MSG_QUERY_URI,
				projection, null, null, "date desc");
		if (cursor.moveToFirst()) {
			do {
				String id = "" + cursor.getLong(cursor.getColumnIndex("_id"));
				String recipient_id = cursor.getString(cursor
						.getColumnIndex("recipient_ids"));
				String snippet = cursor.getString(cursor
						.getColumnIndex("snippet"));
				String date = ""
						+ cursor.getLong(cursor.getColumnIndex("date"));
				String has_attachment = ""
						+ cursor.getLong(cursor
								.getColumnIndex("has_attachment"));
				String snippet_cs = ""
						+ cursor.getLong(cursor.getColumnIndex("snippet_cs"));
				String message_count = ""
						+ cursor.getLong(cursor.getColumnIndex("message_count"));
				Log.i(TAG, "id:" + id + ",recipient_id:" + recipient_id
						+ ",snippet:" + snippet + ",date:" + date
						+ ",has_attachment:" + has_attachment + ",snippet_cs:"
						+ snippet_cs + ",message_count:" + message_count);
			} while (cursor.moveToNext());
		}
		return itemInfos;
	}

	public static String getInfosByPhoneNum(String phoneNum) {
		String name = "";
		Uri nameUri = Uri.withAppendedPath(
				ContactsContract.PhoneLookup.CONTENT_FILTER_URI, phoneNum);
		Cursor cursor = mContext.getContentResolver().query(nameUri,
				new String[] { PhoneLookup.DISPLAY_NAME }, null, null, null);
		if (cursor.moveToFirst()) {
			int nameId = cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME);
			name = cursor.getString(nameId);
		} else {
			name = phoneNum;
		}
		if (cursor != null) {
			cursor.close();
		}
		return name;
	}

	public static void deleteByThreadId(long thread_id) {
		String deleteUri = "content://sms/";
		mContext.getContentResolver().delete(Uri.parse(deleteUri),
				"thread_id=" + thread_id, null);
	}

	public static void deleteById(long id) {
		String deleteUri = "content://sms/";
		mContext.getContentResolver().delete(Uri.parse(deleteUri), "_id=" + id,
				null);
	}

	public static List<SMSInfo> queryByThreadId(String name, long thread_id) {
		List<SMSInfo> infos = new ArrayList<SMSInfo>();
		String deleteUri = "content://sms/";
		String[] projection = { "_id", "address", "body", "date", "type" };
		Cursor cursor = mContext.getContentResolver().query(
				Uri.parse(deleteUri), projection, "thread_id=" + thread_id,
				null, "date desc");
		while (cursor.moveToNext()) {
			SMSInfo info = new SMSInfo();

			int index_id = cursor.getColumnIndex("_id");
			int index_address = cursor.getColumnIndex("address");
			int index_body = cursor.getColumnIndex("body");
			int index_date = cursor.getColumnIndex("date");
			int index_type = cursor.getColumnIndex("type");
			int id = cursor.getInt(index_id);
			String address = cursor.getString(index_address);
			String body = cursor.getString(index_body);
			long date = cursor.getLong(index_date);
			int type = cursor.getInt(index_type);

			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy-MM-dd hh:mm:ss");
			Date d = new Date(date);
			String dateStr = format.format(d);
			if (type == 1) {// 发送
				info.setType(0);
			} else if (type == 2) {// 接收
				info.setType(1);
			} else if (type == 3) {// 草稿
				info.setType(2);
				address = queryById(id);
				Log.i(TAG, "address:" + address);
			} else {
				continue;
			}
			info.setId(id);
			info.setoTime(date);
			info.setPhoneNum(address);
			info.setTime(dateStr);
			info.setContent(body);
			info.setThread_id(thread_id);
			info.setName(name);
			infos.add(info);
		}
		return infos;
	}

	public static String queryById(long id) {
		Cursor draftCursor = mContext
				.getContentResolver()
				.query(Uri.parse("content://sms"),
						new String[] { "canonical_addresses.address "
								+ "from sms,threads,canonical_addresses "
								+ "where sms.thread_id=threads._id and threads.recipient_ids=canonical_addresses._id and sms._id ='"
								+ String.valueOf(id) + "' --" }, null, null,
						null);
		String address = "";
		while (draftCursor.moveToNext()) {
			int index_address = draftCursor.getColumnIndex("address");
			address = draftCursor.getString(index_address);
		}
		return address;
	}

	public static void saveDraft(SMSInfo info, Context context) {
		String ID = "_id";
		String THREAD_ID = "thread_id";
		String ADDRESS = "address";
		String DATE = "date";
		String READ = "read";
		String STATUS = "status";
		String TYPE = "type";
		String BODY = "body";

		ContentValues value = new ContentValues();
		// value.put(ID, mInfo.getId());
		value.put(THREAD_ID, info.getThread_id());
		value.put(ADDRESS, info.getPhoneNum());
		value.put(DATE, String.valueOf(System.currentTimeMillis()));
		value.put(READ, "1");
		value.put(STATUS, "-1");
		value.put(TYPE, "3");
		value.put(BODY, info.getContent());
		context.getContentResolver().update(Uri.parse("content://sms"), value,
				ID + "=" + info.getId(), null);
		Toast.makeText(context, R.string.save_draft, 1000).show();
	}

	public static void insertDraft(SMSInfo info, Context context) {
		Log.i(TAG, "insert");
		String ADDRESS = "address";
		String DATE = "date";
		String TYPE = "type";
		String BODY = "body";
		long time;

		ContentValues value = new ContentValues();
		value.put(ADDRESS, info.getPhoneNum());
		time = System.currentTimeMillis();
		value.put(DATE, String.valueOf(System.currentTimeMillis()));
		value.put(TYPE, "2");
		value.put(BODY, info.getContent());

		context.getContentResolver().insert(Uri.parse("content://sms"), value);
		value.put(TYPE, "3");
		context.getContentResolver().update(Uri.parse("content://sms"), value,
				"date=" + time, null);
		Toast.makeText(context, R.string.save_draft, 1000).show();
	}
}
