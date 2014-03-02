package com.example.smstb.ui.adapter;

import java.util.List;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.europa.tool.ToolAdapter;
import com.example.smstb.R;
import com.example.smstb.bean.SMSInfo;
import com.example.smstb.iinterface.ListInterface;
import com.example.smstb.ui.activity.InfosPersonActivity;
import com.example.smstb.util.InfoUtil;
import com.example.smstb.util.TimeUtil;

public class SMSInfoAdapter extends ToolAdapter<SMSInfo> implements
		ListInterface {

	private static final String TAG = "SMSInfoAdapter";
	private LayoutInflater mInflater;
	ViewHolder viewHolder;
	InfosPersonActivity infosPersonActivity;
	ListView infoListView;

	public SMSInfoAdapter(List<SMSInfo> list, Activity activity) {
		super(list, activity);
		infosPersonActivity=(InfosPersonActivity) activity;
		infoListView=infosPersonActivity.infoList;
	}

	@Override
	public int getItemViewType(int position) {
		long viewType=getItem(position).getType();
		if(viewType==2){
			return 1;
		}else{
			return 0;
		}
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
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
			viewHolder.selectedChk=(CheckBox) view.findViewById(R.id.selectedChk);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.contentTextView.setText(info.getBody());
		viewHolder.contentTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(infosPersonActivity.actionMode!=null){
					Boolean checked=infoListView.isItemChecked(position);
					infoListView.setItemChecked(position,!checked);
				}
				Log.i(TAG,"position:"+position);
			}
		});
		viewHolder.contentTextView.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				Log.i(TAG,"onlongclick position:"+position);
				if(infosPersonActivity.actionMode==null){
					infoListView.setItemChecked(position, true);
				}
				return false;
			}
		});
		
		if(infosPersonActivity.actionMode==null){
			viewHolder.selectedChk.setVisibility(View.GONE);
		}else{
			viewHolder.selectedChk.setVisibility(View.VISIBLE);
			viewHolder.selectedChk.setChecked(infoListView.isItemChecked(position));
		}
		return view;
	}

	class ViewHolder {
		TextView contentTextView;
		CheckBox selectedChk;
	}

	@Override
	public void deleteItemById(int position) {
		long id = list.get(position).getId();
		InfoUtil.deleteSMSInfoById(id);
		list.remove(position);
		notifyDataSetChanged();
	}

	@Override
	public SMSInfo getInfoByPosition(int position) {
		return list.get(position);
	}

	public void checkItem(View view,Boolean checked){
		((ViewHolder)view.getTag()).selectedChk.setChecked(checked);
	}
}
