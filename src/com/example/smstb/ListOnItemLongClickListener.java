package com.example.smstb;

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
			// TODO Auto-generated method stub
			String str[] = new String[2];
			str[0] = mContext.getResources().getString(R.string.delete_info);
			str[1]=mContext.getResources().getString(R.string.reply);
			String mMenuHead=mContext.getResources().getString(R.string.prompt);
			DialogUtil.createDialog(mContext, mMenuHead, str, new AlertDialogOperateByPosition() {
				
				@Override
				public void operate(int which) {
					// TODO Auto-generated method stub
					switch(which){
					case 0:
						toAlert(position);
						break;
					case 1:
						toReply(position);
						break;
					}
				}
			});
			return false;
	}

	private void toAlert(final int position) {
		DialogUtil.createAlertDialog(mContext, R.string.prompt, R.string.tip, new AlertDialogOperate() {
			@Override
			public void operate() {
				// TODO Auto-generated method stub
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
