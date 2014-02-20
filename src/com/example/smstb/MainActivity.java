package com.example.smstb;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MainActivity extends Activity{
	private final static String TAG = "MainActivity";

	private LayoutInflater mInflater;
	private ListView infosListView;
	private ContentAdapter infosAdapter;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_main);
		findView();
		InfoUtil infoUtil=InfoUtil.newInstance(this);
		infosAdapter=new ContentAdapter(this);
		infosListView.setAdapter(infosAdapter);
		infosListView.setOnItemClickListener(new ItemClickListener());
		infosListView.setOnItemLongClickListener(new ListOnItemLongClickListener(this,infosAdapter));
	}

	@Override
	protected void onResume() {
		super.onResume();
		infosAdapter.refreshData();
	}

	private void findView() {
		infosListView=(ListView) findViewById(R.id.infoList);
	}

	class ItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, InfosPersonActivity.class);
			intent.putExtra(Constants.NAME,((ItemInfos)infosAdapter.getItem(position)).getSmsInfo().getName());
			intent.putExtra(Constants.THREAD_ID,((ItemInfos)infosAdapter.getItem(position)).getSmsInfo().getThread_id());
			startActivity(intent);
		}
	}
}
