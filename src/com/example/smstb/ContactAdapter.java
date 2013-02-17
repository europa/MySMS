package com.example.smstb;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.PrivateCredentialPermission;

import com.example.smstb.ContentAdapter.ViewHolder;

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
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ContactAdapter extends BaseAdapter implements ListInterface{
	private static final String TAG="SMSInfoAdapter";

	private List<Contact> mContacts=new ArrayList<Contact>();
	private LayoutInflater mInflater;
	private Context mContext;
	
	public ContactAdapter(Context context) {
		mContext=context;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return mContacts.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mContacts.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (mInflater == null) {
			mInflater = LayoutInflater.from(mContext);
		}
		View view = convertView;
		if (view == null) {
			view = mInflater.inflate(R.layout.contact_item, null);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.deleteContactChkbox=(CheckBox) view.findViewById(R.id.contact_delete_chkbox);
			viewHolder.nameTextView=(TextView) view.findViewById(R.id.name);
			viewHolder.phoneNumTView=(TextView) view.findViewById(R.id.phoneNum);
			viewHolder.portrait=(ImageView) view.findViewById(R.id.portrait);
			view.setTag(viewHolder);
		}
		ViewHolder viewHolder = (ViewHolder) view.getTag();
		viewHolder.nameTextView.setText(mContacts.get(position).getName());
		viewHolder.phoneNumTView.setText(mContacts.get(position).getPhoneNum());
		viewHolder.portrait.setImageBitmap(mContacts.get(position).getPortrait());
		//使只有被点击的checkbox才显示勾勾,否则会有没被点击的checkbox也会显示勾勾
		viewHolder.deleteContactChkbox.setChecked(((ListView)parent).isItemChecked(position));
		return view;
	}

	class ViewHolder {
		CheckBox deleteContactChkbox;
		TextView nameTextView;
		TextView phoneNumTView;
		ImageView portrait;
	}
	
	public List<Contact> refreshData(){
		String[] PHONE_PROJECTION=new String[]{Phone.DISPLAY_NAME,Phone.NUMBER,
				Photo.PHOTO_ID,Phone.CONTACT_ID};
		String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
		ContentResolver resolver=mContext.getContentResolver();
		Cursor phoneCursor=resolver.query(Phone.CONTENT_URI, PHONE_PROJECTION, null, null,sortOrder);
		if(phoneCursor!=null){
			while(phoneCursor.moveToNext()){
				Contact contact=new Contact();
				String name,phoneNum;
				int portraitId,contactId;
				Bitmap portrait;
				phoneNum=phoneCursor.getString(Constants.PHONE_NUMBER_INDEX);
				if(phoneNum.equals("")){
					continue;
				}
				name=phoneCursor.getString(Constants.PHONE_DISPLAY_NAME_INDEX);
				portraitId=phoneCursor.getInt(Constants.PHONE_PHOTO_ID_INDEX);
				contactId=phoneCursor.getInt(Constants.PHONE_CONTACT_ID_INDEX);
				if(portraitId>0){
					Uri uri=ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
					InputStream inputStream=ContactsContract.Contacts
							.openContactPhotoInputStream(resolver, uri);
					portrait=BitmapFactory.decodeStream(inputStream);
				}else{
					portrait=BitmapFactory.decodeResource(mContext.getResources(), android.R.drawable.ic_menu_view);
				}
				contact.setName(name);
				contact.setPhoneNum(phoneNum);
				contact.setPortrait(portrait);
				
				mContacts.add(contact);
			}
			phoneCursor.close();
		}
		return mContacts;
	}

	@Override
	public void deleteItemById(int position) {
		// TODO Auto-generated method stub
		notifyDataSetChanged();
	}

	@Override
	public SMSInfo getInfoByPosition(int position) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
