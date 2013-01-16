package com.example.smstb;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

public class InfosPersonActivity extends Activity{

	private TextView personText;
	private ListView infoList;
	
	private List<SMSInfo> mInfos=new ArrayList<SMSInfo>();
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_infos);
		
		personText=(TextView) findViewById(R.id.personText);
		infoList=(ListView) findViewById(R.id.infos);
		
		mInfos=(List<SMSInfo>) getIntent().getSerializableExtra("infos");
		SMSInfoAdapter smsInfoAdapter=new SMSInfoAdapter(mInfos, this);
		infoList.setAdapter(smsInfoAdapter);
		
		String phoneNum=mInfos.get(0).getPhoneNum();
		String contatcName=InfoUtils.getContactByPhone(this, phoneNum);
		if(contatcName.equals(phoneNum)){
			personText.setText(phoneNum);
		}else{
			personText.setText(contatcName+" "+phoneNum);
		}
	}
}
