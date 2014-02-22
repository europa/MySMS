package com.example.smstb.ui.activity;

import com.example.smstb.ListOnItemLongClickListener;
import com.example.smstb.R;
import com.example.smstb.R.id;
import com.example.smstb.R.layout;
import com.example.smstb.adapter.SMSInfoAdapter;
import com.example.smstb.bean.SMSInfo;
import com.example.smstb.util.Constants;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class InfosPersonActivity extends FragmentActivity{

	private TextView personText;
	private ListView infoList;
	SMSInfoAdapter smsInfoAdapter;
	
	String name="";
	long thread_id;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_infos);
		name=(String) getIntent().getSerializableExtra(Constants.NAME);
		thread_id=getIntent().getLongExtra(Constants.THREAD_ID, 0);
		
		personText=(TextView) findViewById(R.id.head_center);
		infoList=(ListView) findViewById(R.id.infos);
		
		
		smsInfoAdapter=new SMSInfoAdapter(name,thread_id,this);
		infoList.setAdapter(smsInfoAdapter);
		
		personText.setText(name);
		infoList.setOnItemClickListener(new InfoOnItemClickListener());
		
		ListOnItemLongClickListener listOnItemLongClickListener=new ListOnItemLongClickListener(this, smsInfoAdapter);
		infoList.setOnItemLongClickListener(listOnItemLongClickListener);
	}
	
	
	@Override
	protected void onResume() {
		smsInfoAdapter.refreshData();
		super.onResume();
	}


	class InfoOnItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent intent=new Intent();
			intent.setClass(InfosPersonActivity.this, InfoActivity.class);
			intent.putExtra(Constants.INFO,(SMSInfo)smsInfoAdapter.getItem(position));
			startActivity(intent);
		}
	}
	
}
