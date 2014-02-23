package com.example.smstb.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.smstb.R;
import com.example.smstb.ui.adapter.ContentAdapter;
import com.example.smstb.util.Constants;
import com.example.smstb.util.InfoUtil;

public class MainActivity extends BaseActivity{
	private ListView infosListView;
	private ContentAdapter infosAdapter;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_main);
		findView();
		InfoUtil infoUtil=InfoUtil.newInstance(this);
		infosAdapter=new ContentAdapter(InfoUtil.getItemsByPerson(),this);
		infosListView.setAdapter(infosAdapter);
		infosListView.setOnItemClickListener(new ItemClickListener());
	}

	@Override
	protected void onResume() {
		infosAdapter.list=InfoUtil.getItemsByPerson();
		infosAdapter.notifyDataSetChanged();
		super.onResume();
	}

	private void findView() {
		infosListView=(ListView) findViewById(R.id.infoList);
	}

	class ItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, InfosPersonActivity.class);
			brain.setCurrentConversation(infosAdapter.getItem(position));
			intent.putExtra(Constants.NAME,infosAdapter.convertToName(infosAdapter.getItem(position).getRecipient_ids()));
			intent.putExtra(Constants.THREAD_ID,infosAdapter.getItem(position).getId());
			startActivity(intent);
		}
	}
}
