package com.example.smstb.ui.activity;


import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.smstb.R;
import com.example.smstb.ui.adapter.ContentAdapter;
import com.example.smstb.util.Constants;
import com.example.smstb.util.InfoUtil;

public class MainActivity extends InfoBaseActivity{
	private ListView infosListView;
	private ContentAdapter infosAdapter;

	@Override
	protected void onResume() {
		infosAdapter.list=InfoUtil.getItemsByPerson();
		infosAdapter.notifyDataSetChanged();
		super.onResume();
	}

	class ItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, InfosPersonActivity.class);
			brain.setCurrentConversation(infosAdapter.getItem(position));
			startActivity(intent);
		}
	}

	@Override
	public int getLayoutId() {
		return R.layout.layout_main;
	}

	@Override
	public void setView() {
		infosListView=(ListView) findViewById(R.id.infoList);
		infosAdapter=new ContentAdapter(InfoUtil.getItemsByPerson(),this);
		infosListView.setAdapter(infosAdapter);
		infosListView.setOnItemClickListener(new ItemClickListener());
	}
}
