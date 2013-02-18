package com.example.smstb;

import java.util.ArrayList;
import java.util.List;

import com.example.smstb.ContentAdapter.ViewHolder;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SMSInfoAdapter extends BaseAdapter implements ListInterface{
	private static final String TAG="SMSInfoAdapter";

	List<SMSInfo> mInfos = new ArrayList<SMSInfo>();
	private LayoutInflater mInflater;
	private Context mContext;
	private long thread_id;
	private String name;
	
	public SMSInfoAdapter(String name,long thread_id,Context context) {
		this.thread_id=thread_id;
		mContext=context;
		this.name=name;
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
			viewHolder.draftTextView=(TextView) view.findViewById(R.id.draft);
			view.setTag(viewHolder);
		}
		ViewHolder viewHolder = (ViewHolder) view.getTag();
		viewHolder.draftTextView.setVisibility(View.INVISIBLE);
		if(mInfos.get(position).getType()==1){
			viewHolder.phoneNum.setText("我");
		}else if(mInfos.get(position).getType()==2){
			viewHolder.phoneNum.setText("我");
			viewHolder.draftTextView.setVisibility(View.VISIBLE);
		}else{
			viewHolder.phoneNum.setText(mInfos.get(position).getName());
		}
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
		TextView draftTextView;
	}
	
	public void refreshData(){
		mInfos=InfoUtil.queryByThreadId(name, thread_id);
		if(mInfos==null){
			Log.i(TAG,"null");
			return;
		}
		Log.i(TAG,TAG+mInfos.size());
		notifyDataSetChanged();
	}

	@Override
	public void deleteItemById(int position) {
		// TODO Auto-generated method stub
		int id=mInfos.get(position).getId();
		InfoUtil.deleteById(id);
//		mInfos=InfoUtil.getInfosByName(mName);
		notifyDataSetChanged();
	}

	@Override
	public SMSInfo getInfoByPosition(int position) {
		// TODO Auto-generated method stub
		return mInfos.get(position);
	}
	
}
