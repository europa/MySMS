package com.example.smstb.ui.adapter;

import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.europa.tool.ToolAdapter;
import com.europa.tool.ViewUtil;
import com.example.smstb.R;
import com.example.smstb.bean.Conversation;
import com.example.smstb.bean.SMSInfo;
import com.example.smstb.iinterface.ListInterface;
import com.example.smstb.ui.activity.MainActivity;
import com.example.smstb.util.Brain;
import com.example.smstb.util.InfoUtil;

public class ContentAdapter extends ToolAdapter<Conversation>{

	LayoutInflater inflater;
	public ViewHolder holder;
	MainActivity mainActivity;
	ListView infosListView;
	public ContentAdapter(List<Conversation> list, Activity activity) {
		super(list, activity);
		this.mainActivity=(MainActivity)activity;
		infosListView=mainActivity.infosListView;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		View view=arg1;
		if(view==null){
			holder=new ViewHolder();
			inflater=activity.getLayoutInflater();
			view=inflater.inflate(R.layout.item_conversation,null);
			holder.phoneNumberText=ViewUtil.findText(view,R.id.phoneNumber);
			holder.amountText=ViewUtil.findText(view,R.id.amount);
			holder.contentText=ViewUtil.findText(view,R.id.content);
			holder.timeText=ViewUtil.findText(view,R.id.time);
			holder.selectedChk=ViewUtil.findChkBox(view,R.id.selectedChk);
			view.setTag(holder);
		}else{
			holder=(ViewHolder) view.getTag();
		}
		Conversation conversation=getItem(arg0);
		holder.phoneNumberText.setText(InfoUtil.convertIdSToName(conversation.getRecipient_ids()));
		if(conversation.getCount()==0){
			holder.amountText.setText("草稿");
			holder.amountText.setTextColor(Color.RED);
		}else{
			holder.amountText.setText("("+conversation.getCount()+")");
			holder.amountText.setTextColor(Color.BLACK);
		}
		holder.contentText.setText(conversation.getSnippet());
		holder.timeText.setText(conversation.getDate_str());
		if(mainActivity.actionMode!=null){
			holder.selectedChk.setVisibility(View.VISIBLE);
			holder.selectedChk.setChecked(infosListView.isItemChecked(arg0));
		}else{
			holder.selectedChk.setVisibility(View.GONE);
		}
		return view;
	}
	
	public class ViewHolder{
		TextView phoneNumberText;
		TextView amountText;
		TextView timeText;
		TextView contentText;
		public CheckBox selectedChk;
	}
	
	public String convertToName(String phone){
		String name="";
		String[] ids=phone.split(" ");
		for(int i=0;i<ids.length;i++){
			name+=InfoUtil.getNameById((Integer.parseInt(ids[i])));
			if(i!=ids.length-1){
				name+=",";
			}
		}
		return name;
	}
	
	public void checkItem(View view,Boolean checked){
		((ViewHolder)view.getTag()).selectedChk.setChecked(checked);
	}
}
