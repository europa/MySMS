package com.example.smstb.ui.adapter;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.europa.tool.ToolAdapter;
import com.example.smstb.R;
import com.example.smstb.bean.SMSInfo;
import com.example.smstb.iinterface.ListInterface;

public class PinyinAdapter extends ToolAdapter<String> implements
		ListInterface {

	private static final String TAG = "SMSInfoAdapter";
	private LayoutInflater mInflater;
	ViewHolder viewHolder;

	public PinyinAdapter(List<String> list, Activity activity) {
		super(list, activity);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			mInflater = activity.getLayoutInflater();
			view = mInflater.inflate(R.layout.item_pinyin, null);
			viewHolder = new ViewHolder();
			viewHolder.pinyinTextView = (TextView) view
					.findViewById(R.id.pinyinText);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.pinyinTextView.setText(getItem(position));
		return view;
	}

	class ViewHolder {
		TextView pinyinTextView;
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
