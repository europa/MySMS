package com.example.smstb.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smstb.R;
import com.example.smstb.bean.SMSInfo;
import com.example.smstb.ui.adapter.SMSInfoAdapter;
import com.example.smstb.util.Constants;
import com.example.smstb.util.InfoUtil;

public class InfosPersonActivity extends SendBaseActivity {


	private ListView infoList;
	SMSInfoAdapter smsInfoAdapter;
	EditText replyEditText;

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sendImgBtn:
			handleInfo();
			break;

		default:
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		
	}
	@Override
	protected void onPause() {
		String reply=replyEditText.getText().toString();
		if((draft==null&&!reply.trim().equals(""))||(draft!=null&&(!reply.trim().equals("")))){
			String id=brain.getCurrentConversation().getRecipient_ids().split(" ")[0];
			info.setAddress(InfoUtil.getPhoneNum(Integer.parseInt(id)));
			info.setType(Constants.DRAFT);
			info.setBody(reply);
			InfoUtil.insertInfo(this, info);
		}else if(draft!=null&&reply.trim().equals("")){
			InfoUtil.deleteById(draft.getId());
		}
		super.onPause();
	}
	class InfoOnItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent intent = new Intent();
			intent.setClass(InfosPersonActivity.this, InfoActivity.class);
			intent.putExtra(Constants.INFO,
					(SMSInfo) smsInfoAdapter.getItem(position));
			startActivity(intent);
		}
	}

	private void handleInfo() {
		String reply = replyEditText.getText().toString();
		if (reply.equals("")) {
			Toast.makeText(this, R.string.not_null, 1000).show();
		} else {
			for (String id:brain.getCurrentConversation().getRecipient_ids().split(" ")) {
				info.setAddress(InfoUtil.getPhoneNum(Integer.parseInt(id)));
				info.setBody(reply);
				sendInfo(info);
				replyEditText.setText("");
			}

		}
	}
	
	private SMSInfo getDraft(){
		SMSInfo draft=null;
		for(SMSInfo info:brain.getInfos()){
			if(info.getType()==Constants.DRAFT){
				draft=info;
				break;
			}
		}
		if(draft!=null){
			brain.getInfos().remove(draft);
		}
		return draft;
	}

	@Override
	public int getLayoutId() {
		return R.layout.layout_infos;
	}

	@Override
	public void setView() {
		infoList = (ListView) findViewById(R.id.infos);
		replyEditText = (EditText) findViewById(R.id.replyEdit);

		brain.setInfos(InfoUtil.queryByThreadId(brain.getCurrentThreadId()));
		draft=getDraft();
		if(draft!=null){
			replyEditText.setText(draft.getBody());
		}
		smsInfoAdapter = new SMSInfoAdapter(brain.getInfos(), this);
		infoList.setAdapter(smsInfoAdapter);
		actionBar.setTitle(brain.getCurrentName());
	}
	
	
}
