package com.example.smstb;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.util.Log;

public class InfoUtil {
	private static final String TAG="InfoUtil";
	public static InfoUtil infoUtil=new InfoUtil();
	private static Context mContext;
	private static List<SMSInfo> infos=new ArrayList<SMSInfo>();
	
	public static InfoUtil newInstance(Context context){
		mContext=context;
		if(infoUtil==null){
			return new InfoUtil();
		}
		return infoUtil;
	}
	public static String getSmsContent() {
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
			String[] projection = { "_id", "address", "person", "body", "date", "type" };
			Cursor cursor = mContext.getContentResolver().query(uri, projection, null, null, "date desc");
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
					Log.i(TAG,"ad:"+address+",person:"+person+",body:"+body+",date:"+date+",type:"+type);
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					Date d = new Date(date);
					String dateStr = format.format(d);
					if(address==null){
						Log.i(TAG,"==body:"+body+",date:"+date+",type:"+type);
						continue;
					}
					if(address.equals("")){
						Log.i(TAG,"eqbody:"+body+",date:"+date+",type:"+type);
						continue;
					}
					SMSInfo info = new SMSInfo();
					info.setoTime(date);
					info.setPhoneNum(address);
					info.setTime(dateStr);
					info.setContent(body);
					String strType = "";
					if (type == 1) {
						strType = "接收";
						if (info.getPhoneNum().equals("")) {	
							continue;
						} else {
							info.setType(0);
						}
					} else if (type == 2) {
						strType = "发送";
						info.setType(1);
					} else {
						strType = "null";
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
			for(int i=0;i<infos.size();i++){
				String name = infos.get(i).getPhoneNum();
				Uri personUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
						name);
				cursor = mContext.getContentResolver().query(personUri,
						new String[] { PhoneLookup.DISPLAY_NAME }, null, null, null);
				if (cursor.moveToFirst()) {
					int nameId = cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME);
					String phoneName=cursor.getString(nameId);
					Log.i(TAG,"name:"+name+",phoneName:"+phoneName);
					if(phoneName==null||phoneName.equals("")){
						infos.get(i).setName(name);
					}else{
						infos.get(i).setName(phoneName);
					}
				}else{
					//有号码，但不在联系人中的
					infos.get(i).setName(name);
				}
				cursor.close();
			}
			smsBuilder.append("All");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return smsBuilder.toString();
	}

	public static List<ItemInfos> getInfosInPerson() {
		List<String> names = new ArrayList<String>();
		List<ItemInfos> itemInfos = new ArrayList<ItemInfos>();

		for (int i = 0; i < infos.size(); i++) {
			if (!names.contains(infos.get(i).getName())) {
				names.add(infos.get(i).getName());
			}
		}
		for (int i = 0; i < names.size(); i++) {
			List<SMSInfo> infoList = new ArrayList<SMSInfo>();
			ItemInfos itemInfo = new ItemInfos();
			for (int j = 0; j < infos.size(); j++) {
				if (names.get(i).equals(infos.get(j).getName())) {
					infoList.add(infos.get(j));
				}
			}
			itemInfo.setSmsInfos(infoList);
			itemInfo.setLastTime(infoList.get(0).getoTime());
			itemInfos.add(itemInfo);
		}
		return itemInfos;
	}
	public static List<SMSInfo> getInfosByName(String name){
		List<SMSInfo> infosByName=new ArrayList<SMSInfo>();
		Log.i(TAG,"infos:"+infos.size());
		for(int i=0;i<infos.size();i++){
			if(name.equals(infos.get(i).getName())){
				infosByName.add(infos.get(i));
			}
		}
		return infosByName;
	}
	public static String getInfosByPhoneNum(String phoneNum){
		String name="";
		Uri nameUri=Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, phoneNum);
		Cursor cursor=mContext.getContentResolver().query(nameUri,
				new String[] { PhoneLookup.DISPLAY_NAME }, null, null, null);
		if(cursor.moveToFirst()){
			int nameId=cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME);
			name=cursor.getString(nameId);
		}else{
			name=phoneNum;
		}
		if(cursor!=null){
			cursor.close();
		}
		return name;
	}
}
