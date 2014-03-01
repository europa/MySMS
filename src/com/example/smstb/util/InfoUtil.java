package com.example.smstb.util;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.PhoneLookup;
import android.util.Log;
import android.widget.Toast;

import com.example.smstb.R;
import com.example.smstb.bean.Contact;
import com.example.smstb.bean.Conversation;
import com.example.smstb.bean.SMSInfo;

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

	public static List<Conversation> getItemsByPerson() {
		List<Conversation> conversations = new ArrayList<Conversation>();
		Uri MMS_SMS_URI = Uri.parse("content://mms-sms");
		Uri MSG_QUERY_URI = Uri.withAppendedPath(MMS_SMS_URI, "conversations")
				.buildUpon().appendQueryParameter("simple", "true").build();
		String[] projection = { "_id", "recipient_ids", "snippet", "date",
				"snippet_cs", "message_count", "read" };
		Cursor cursor = mContext.getContentResolver().query(MSG_QUERY_URI,
				projection, null, null, "date desc");
		if (cursor.moveToFirst()) {
			do {
				Conversation conversation = new Conversation();
				conversation
						.setId(cursor.getLong(cursor.getColumnIndex("_id")));
				conversation.setRecipient_ids(cursor.getString(cursor
						.getColumnIndex("recipient_ids")));
				conversation.setSnippet(cursor.getString(cursor
						.getColumnIndex("snippet")));
				conversation.setDate(cursor.getLong(cursor
						.getColumnIndex("date")));
				conversation.setDate_str(TimeUtil.longToStr(conversation
						.getDate()));
				conversation.setSnippet_cs(cursor.getLong(cursor
						.getColumnIndex("snippet_cs")));
				conversation.setCount(cursor.getLong(cursor
						.getColumnIndex("message_count")));
				conversation.setRead(cursor.getLong(cursor
						.getColumnIndex("read")));
				conversations.add(conversation);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return conversations;
	}

	public static String getNameById(int id) {
		return getNameByPhoneNum(getPhoneNum(id));
	}

	public static String getPhoneNum(int id) {
		String name = "";
		Uri MMS_ADDR_URI = Uri.parse("content://sms/threadID/*");
		Cursor cursor = mContext.getContentResolver().query(MMS_ADDR_URI,
				new String[] { "address" }, "_id=" + id, null, null);
		if (cursor.moveToFirst()) {
			name = cursor.getString(cursor.getColumnIndex("address"));
		}
		cursor.close();
		return name;

	}

	public static String getNameByPhoneNum(String phoneNum) {
		String name = "";
		Log.i(TAG, "getNameByPhoneNum:" + phoneNum);
		if (!phoneNum.trim().equals("")) {
			Uri nameUri = Uri.withAppendedPath(
					ContactsContract.PhoneLookup.CONTENT_FILTER_URI, phoneNum);
			Cursor cursor = mContext.getContentResolver()
					.query(nameUri, new String[] { PhoneLookup.DISPLAY_NAME },
							null, null, null);
			if (cursor.moveToFirst()) {
				int nameId = cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME);
				name = cursor.getString(nameId);
			} else {
				name = phoneNum;
			}
			if (cursor != null) {
				cursor.close();
			}
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

	public static List<SMSInfo> queryByThreadId(long thread_id) {
		List<SMSInfo> infos = new ArrayList<SMSInfo>();
		String smsUri = "content://sms/";
		String[] projection = { "_id", "address", "person", "date", "read",
				"type", "body", "service_center", "locked" };
		Cursor cursor = mContext.getContentResolver().query(Uri.parse(smsUri),
				projection, "thread_id=" + thread_id, null, "date desc");
		while (cursor.moveToNext()) {
			SMSInfo info = new SMSInfo();
			info.setId(cursor.getLong(0));
			info.setAddress(cursor.getString(1));
			info.setPerson(cursor.getLong(2));
			info.setDate(cursor.getLong(3));
			info.setRead(cursor.getLong(4));
			info.setType(cursor.getLong(5));
			info.setBody(cursor.getString(6));
			info.setService_center(cursor.getString(7));
			info.setLocked(cursor.getLong(8));
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
		value.put(THREAD_ID, info.getThread_id());
		value.put(ADDRESS, info.getAddress());
		value.put(DATE, String.valueOf(System.currentTimeMillis()));
		value.put(READ, "1");
		value.put(STATUS, "-1");
		value.put(TYPE, "3");
		value.put(BODY, info.getBody());
		context.getContentResolver().update(Uri.parse("content://sms"), value,
				ID + "=" + info.getId(), null);
		Toast.makeText(context, R.string.save_draft, 1000).show();
	}

	/**
	 * 
	 * @param context
	 * @param info
	 *            :the info to insert whether draft or info or failed
	 */
	public static void insertInfo(Context context, SMSInfo info) {
		String ADDRESS = "address";
		String DATE = "date";
		String READ = "read";
		String STATUS = "status";
		String TYPE = "type";
		String BODY = "body";

		ContentValues value = new ContentValues();
		value.put(ADDRESS, info.getAddress());
		value.put(DATE, String.valueOf(System.currentTimeMillis()));
		value.put(READ, "1");
		value.put(STATUS, "-1");
		value.put(TYPE, info.getType());
		value.put(BODY, info.getBody());
		context.getContentResolver().insert(Uri.parse("content://sms"), value);
	}

	/**
	 * 
	 * @param phone
	 * @return display name str
	 */

	public static String convertIdSToName(String phone) {
		String name = "";
		String[] ids = phone.split(" ");
		for (int i = 0; i < ids.length; i++) {
			name += InfoUtil.getNameById((Integer.parseInt(ids[i])));
			if (i != ids.length - 1) {
				name += ",";
			}
		}
		return name;
	}

	public static List<Contact> getContacts() {
		List<Contact> contacts = new ArrayList<Contact>();
		String[] PHONE_PROJECTION = new String[] { Phone.DISPLAY_NAME,
				Phone.NUMBER };
		String sortOrder = " sort_key_alt asc";
		Cursor phoneCursor = mContext.getContentResolver().query(
				Phone.CONTENT_URI, PHONE_PROJECTION, null, null, sortOrder);
		if (phoneCursor != null) {
			while (phoneCursor.moveToNext()) {
				Contact contact = new Contact();
				contact.setName(phoneCursor.getString(0));
				contact.setPhoneNum(phoneCursor.getString(1));
				contact.setPinYin(PinYin.getPinYin(contact.getName()));
				contacts.add(contact);
			}
			phoneCursor.close();
		}
		return contacts;
	}
}
