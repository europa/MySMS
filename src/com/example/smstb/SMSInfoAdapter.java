package com.example.smstb;

import java.util.ArrayList;
import java.util.List;

import com.example.smstb.ContentAdapter.ViewHolder;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SMSInfoAdapter extends BaseAdapter {

	List<SMSInfo> mInfos = new ArrayList<SMSInfo>();
	private LayoutInflater mInflater;
	private Context mContext;

	public SMSInfoAdapter(List<SMSInfo> infos, Context context) {
		mInfos = infos;
		mContext = context;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return mInfos.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mInfos.get(position);
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
			view = mInflater.inflate(R.layout.layout_item, null);

			ViewHolder viewHolder = new ViewHolder();
			viewHolder.phoneNum = (TextView) view.findViewById(R.id.phoneNumber);
			viewHolder.amount = (TextView) view.findViewById(R.id.amount);
			viewHolder.time = (TextView) view.findViewById(R.id.time);
			viewHolder.content = (TextView) view.findViewById(R.id.content);
			view.setTag(viewHolder);
		}
		ViewHolder viewHolder = (ViewHolder) view.getTag();

		viewHolder.phoneNum.setText(InfoUtils.getContactByPhone(mContext,mInfos
				.get(position).getPhoneNum()));
		viewHolder.amount.setText("");
		viewHolder.time.setText(mInfos.get(position).getTime());
		viewHolder.content.setText(mInfos.get(position).getContent());
		return view;
	}

	class ViewHolder {
		TextView phoneNum;
		TextView amount;
		TextView time;
		TextView content;
	}
}
