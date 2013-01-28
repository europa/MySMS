package com.example.smstb;

import java.util.ArrayList;
import java.util.List;

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

public class ContentAdapter extends BaseAdapter {

	private List<ItemInfos> mData = new ArrayList<ItemInfos>();
	private LayoutInflater mInflater;
	private Context mContext;

	public ContentAdapter( Context context) {
		mContext = context;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mData.get(position);
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

		viewHolder.phoneNum.setText(mData.get(position).getSmsInfos().get(0).getName());
		// viewHolder.phoneNum.setText(mData.get(position).getSmsInfos().get(0).getPhoneNum());
		viewHolder.amount.setText("(" + mData.get(position).getSmsInfos().size() + ")");
		viewHolder.time.setText(mData.get(position).getSmsInfos().get(0).getTime());
		viewHolder.content.setText(mData.get(position).getSmsInfos().get(0).getContent());
		return view;
	}

	class ViewHolder {
		TextView phoneNum;
		TextView amount;
		TextView time;
		TextView content;
	}
	
	public void refreshData(){
		mData=InfoUtil.getInfosInPerson();
		notifyDataSetChanged();
	}
}
