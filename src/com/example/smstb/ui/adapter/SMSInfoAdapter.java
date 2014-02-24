package com.example.smstb.ui.adapter;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.europa.tool.ToolAdapter;
import com.example.smstb.R;
import com.example.smstb.bean.SMSInfo;
import com.example.smstb.iinterface.ListInterface;
import com.example.smstb.util.InfoUtil;
import com.example.smstb.util.TimeUtil;

public class SMSInfoAdapter extends ToolAdapter<SMSInfo> implements
		ListInterface {

	private static final String TAG = "SMSInfoAdapter";
	private LayoutInflater mInflater;
	ViewHolder viewHolder;

	public SMSInfoAdapter(List<SMSInfo> list, Activity activity) {
		super(list, activity);
	}

	@Override
	public int getItemViewType(int position) {
		return (int) getItem(position).getType();
	}

	@Override
	public int getViewTypeCount() {
//		return list.size();
		int size=list.size();
		return size<1?1:size;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		SMSInfo info = getItem(position);
		long type = info.getType();
		if (view == null) {
			mInflater = activity.getLayoutInflater();
			viewHolder = new ViewHolder();
			if (type == 2) {
				view = mInflater.inflate(R.layout.item_right_info, null);
			} else {
				view = mInflater.inflate(R.layout.item_left_info, null);
			}
			viewHolder.contentTextView = (TextView) view
					.findViewById(R.id.contentText);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.contentTextView.setText(info.getBody()+type);
		return view;
	}

	class ViewHolder {
		TextView contentTextView;
	}

	@Override
	public void deleteItemById(int position) {
		long id = list.get(position).getId();
		InfoUtil.deleteById(id);
		list.remove(position);
		notifyDataSetChanged();
	}

	@Override
	public SMSInfo getInfoByPosition(int position) {
		return list.get(position);
	}

}
