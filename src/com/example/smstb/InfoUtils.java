package com.example.smstb;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;

public class InfoUtils {
	public static String getContactByPhone(Context ctx,String phoneNum) {
		String name = phoneNum;
		Uri personUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
				phoneNum);
		Cursor cursor = ctx.getContentResolver().query(personUri,
				new String[] { PhoneLookup.DISPLAY_NAME }, null, null, null);
		if (cursor.moveToFirst()) {
			int nameId = cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME);
			name = cursor.getString(nameId);
		}
		return name;
	}
}
