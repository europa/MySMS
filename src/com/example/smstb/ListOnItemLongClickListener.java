package com.example.smstb;

import com.example.smstb.iinterface.AlertDialogOperate;
import com.example.smstb.iinterface.AlertDialogOperateByPosition;
import com.example.smstb.iinterface.ListInterface;
import com.example.smstb.ui.activity.InfoActivity;
import com.example.smstb.ui.activity.MainActivity;
import com.example.smstb.util.Constants;
import com.example.smstb.util.DialogUtil;

import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;

public class ListOnItemLongClickListener implements OnItemLongClickListener{ 
	
	private final static String TAG = "ListOnItemLongClickListener";
	private Context mContext;
	private ListInterface listInterface;
	
	public ListOnItemLongClickListener(Context context,ListInterface listInterface){
		mContext=context;
		this.listInterface=listInterface;
	}	
		@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
			String str[] = new String[2];
			if(mContext instanceof MainActivity){
				str[0] = mContext.getResources().getString(R.string.delete_infos);
			}else{
				str[0] = mContext.getResources().getString(R.string.delete_info);
			}
				str[1]=mContext.getResources().getString(R.string.reply);
			String mMenuHead=mContext.getResources().getString(R.string.prompt);
			DialogUtil.createDialog(mContext, mMenuHead, str, new AlertDialogOperateByPosition() {
				
				@Override
				public void operate(int which) {
					switch(which){
					case 0:
						if(mContext instanceof MainActivity){
							toAlert(position,R.string.tip);
						}else{
							toAlert(position,R.string.delete_info);
						}
						break;
					case 1:
						toReply(position);
						break;
					}
				}
			});
			return false;
	}

	private void toAlert(final int position,final int str) {
		DialogUtil.createAlertDialog(mContext, R.string.prompt, str, new AlertDialogOperate() {
			@Override
			public void operate() {
				listInterface.deleteItemById(position);
			}
		});
	}
	
	private void toReply(int position){
		Intent intent=new Intent();
		intent.putExtra(Constants.INFO, listInterface.getInfoByPosition(position));
		intent.setClass(mContext, InfoActivity.class);
		mContext.startActivity(intent);
	}
}
