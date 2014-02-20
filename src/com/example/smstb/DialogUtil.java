package com.example.smstb;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;

public class DialogUtil {
	private static final String TAG="DialogUtil";
	private static DialogUtil dialogUtil=new DialogUtil();
	public DialogUtil getInstance(){
		if(dialogUtil==null){
			dialogUtil=new DialogUtil();
		}
		return dialogUtil;
	}
	
	public static void createAlertDialog(Context ctx,int head,int tip,final AlertDialogOperate callback){
		new AlertDialog.Builder(ctx)
			.setTitle(head)
			.setMessage(tip)
			.setPositiveButton(R.string.certain,new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Log.i(TAG,"w:"+which);
					callback.operate();
					dialog.dismiss();
				}
			})
			.setNegativeButton(R.string.cancel,new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			}).create().show();
	}
	
	public static void createDialog(Context context,String head,String[] str,final AlertDialogOperateByPosition callback){
		new AlertDialog.Builder(context)
			.setTitle(head)
			.setItems(str, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					callback.operate(which);
					Log.i(TAG,"Dw:"+which);
				}
			})
			.create().show();
	}
}
