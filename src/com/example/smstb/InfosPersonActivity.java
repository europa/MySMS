package com.example.smstb;

import java.util.ArrayList;
import java.util.List;

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
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_infos);
		name=(String) getIntent().getSerializableExtra(Constants.NAME);
		
		personText=(TextView) findViewById(R.id.personText);
		infoList=(ListView) findViewById(R.id.infos);
		
		
		smsInfoAdapter=new SMSInfoAdapter(name,this);
		infoList.setAdapter(smsInfoAdapter);
		
		personText.setText(name);
		infoList.setOnItemClickListener(new InfoOnItemClickListener());
	}
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		smsInfoAdapter.refreshData();
		super.onResume();
	}


	class InfoOnItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// TODO Auto-generated method stub
			Intent intent=new Intent();
			intent.setClass(InfosPersonActivity.this, InfoActivity.class);
//			intent.putExtra(Constants.INFO,mInfos.get(position));
			startActivity(intent);
		}
	}
}
