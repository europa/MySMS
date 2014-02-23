package com.example.smstb.ui.adapter;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.PrivateCredentialPermission;

import com.europa.tool.ToolAdapter;
import com.example.smstb.R;
import com.example.smstb.R.id;
import com.example.smstb.R.layout;
import com.example.smstb.bean.Contact;
import com.example.smstb.bean.SMSInfo;
import com.example.smstb.iinterface.ListInterface;
import com.example.smstb.util.Constants;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.provider.ContactsContract.PhoneLookup;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ContactAdapter extends ToolAdapter<Contact> implements ListInterface{
	public ContactAdapter(List<Contact> list, Activity activity) {
		super(list, activity);
	}

	private static final String TAG="SMSInfoAdapter";
	private LayoutInflater mInflater;
	ViewHolder viewHolder; 

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			mInflater = activity.getLayoutInflater();
			view = mInflater.inflate(R.layout.item_contact, null);
			viewHolder = new ViewHolder();
			viewHolder.deleteContactChkbox=(CheckBox) view.findViewById(R.id.contactChkbox);
			viewHolder.nameTextView=(TextView) view.findViewById(R.id.name);
			viewHolder.phoneNumTView=(TextView) view.findViewById(R.id.phoneNum);
			view.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) view.getTag();
		}
		Contact contact=getItem(position);
		viewHolder.nameTextView.setText(contact.getName());
		viewHolder.phoneNumTView.setText(contact.getPhoneNum());
		//使只有被点击的checkbox才显示勾勾,否则会有没被点击的checkbox也会显示勾勾
		viewHolder.deleteContactChkbox.setChecked(((ListView)parent).isItemChecked(position));
		return view;
	}
	

	class ViewHolder {
		CheckBox deleteContactChkbox;
		TextView nameTextView;
		TextView phoneNumTView;
	}

	@Override
	public void deleteItemById(int position) {
		notifyDataSetChanged();
	}

	@Override
	public SMSInfo getInfoByPosition(int position) {
		return null;
	}
}
