package com.example.smstb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.CursorJoiner.Result;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class ContactActivity extends FragmentActivity{
	
	private static final String TAG="ContactActivity";
	private ListView contactListView;
	private ContactAdapter contactAdapter;
	private List<List<String>> contacts=new ArrayList<List<String>>();
	private List<Contact> allContacts=new ArrayList<Contact>();
	private TextView sureTextView;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_contact);
		
		
		findView();
		contactAdapter=new ContactAdapter(this);
		allContacts=contactAdapter.refreshData();
		contactListView.setAdapter(contactAdapter);
		
		sureTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getSelectedContacts();
				Intent intent=new Intent();
				intent.setClass(ContactActivity.this, NewActivity.class);
				intent.putExtra(Constants.CONTACTS, (Serializable)contacts);
				if(getParent()==null){
					setResult(Activity.RESULT_OK, intent);
				}else{
					getParent().setResult(Activity.RESULT_OK, intent);
				}
				ContactActivity.this.finish();
			}
		});
		contactListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
//				Intent intent=new Intent();
//				intent.setClass(ContactActivity.this, NewActivity.class);
//				List<String> contactList=new ArrayList<String>();
//				contactList.add(allContacts.get(position).getName());
//				contactList.add(allContacts.get(position).getPhoneNum());
//				intent.putExtra(Constants.CONTACTS, (Serializable)contactList);
//				if(getParent()==null){
//					setResult(1, intent);
//				}else{
//					getParent().setResult(RESULT_OK, intent);
//				}
//				ContactActivity.this.finish();
			}
			
		});
	}
	private void findView(){
		contactListView=(ListView) findViewById(R.id.contact);
		sureTextView=(TextView) findViewById(R.id.sure);
	}
	
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK){
			getSelectedContacts();
			Intent intent=new Intent();
			intent.setClass(this, NewActivity.class);
			intent.putExtra(Constants.CONTACTS, (Serializable)contacts);
			setResult(RESULT_OK, intent);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void  getSelectedContacts(){
//		只要被check过，就会在items里，items里并不都是true
		SparseBooleanArray items = contactListView.getCheckedItemPositions();
		Log.i(TAG,"size:"+items.size());
		for(int i=0;i<items.size();i++){
			Log.i(TAG,i+"value:"+items.get(i));
			Log.i(TAG,i+"key:"+items.keyAt(i));
			Log.i(TAG,i+"values:"+items.valueAt(i));
			if(items.valueAt(i)){
				List<String> contact=new ArrayList<String>();
				contact.add(allContacts.get(items.keyAt(i)).getName());
				contact.add(allContacts.get(items.keyAt(i)).getPhoneNum());
				contacts.add(contact);
			}
		}
		Log.i(TAG,"con:"+contacts.size());
	}


	class SendOnClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
		}
	}
}
