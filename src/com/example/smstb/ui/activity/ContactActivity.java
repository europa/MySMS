package com.example.smstb.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.example.smstb.R;
import com.example.smstb.bean.Contact;
import com.example.smstb.ui.adapter.ContactAdapter;
import com.example.smstb.ui.adapter.PinyinAdapter;
import com.example.smstb.util.InfoUtil;

public class ContactActivity extends BaseActivity {

	private ListView pinyinListView;
	private ListView contactListView;
	private ContactAdapter contactAdapter;
	private PinyinAdapter pinyinAdapter;
	private List<List<String>> contacts = new ArrayList<List<String>>();
	private List<Contact> allContacts = new ArrayList<Contact>();
	private List<Boolean> selectedContact = new ArrayList<Boolean>();
	private TextView sureTextView;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_contact);

		findView();
		if (brain.getContacts().size() == 0) {
			brain.setContacts(InfoUtil.getContacts());
		}
		contactAdapter = new ContactAdapter(brain.getContacts(), this);
		pinyinAdapter = new PinyinAdapter(brain.getPinyinList(), this);
		contactListView.setItemsCanFocus(true);
		contactListView.setAdapter(contactAdapter);
		pinyinListView.setAdapter(pinyinAdapter);

		sureTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getSelectedContacts();
				Intent intent = new Intent();
				setResult(RESULT_OK);
				ContactActivity.this.finish();
			}
		});
		contactListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				contactListView.setItemChecked(position,
						contactListView.isItemChecked(position));
			}

		});
		pinyinListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String pinyin = pinyinAdapter.getItem(arg2);
				for (Contact contact : contactAdapter.list) {
					if (contact.getPinYin().startsWith(pinyin)) {
						contactListView.setSelectionFromTop(
								contactAdapter.list.indexOf(contact), 10);
						break;
					}
				}
			}

		});
	}

	private void findView() {
		contactListView = (ListView) findViewById(R.id.contact);
		pinyinListView = (ListView) findViewById(R.id.pinyinListView);
		sureTextView = (TextView) findViewById(R.id.sure);
	}

	@Override
	protected void onResume() {
		List<Contact> selectedContacts=brain.getSelectedContacts();
		for(Contact contact:selectedContacts){
			contactListView.setItemChecked(contactAdapter.list.indexOf(contact),true);
		}
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void getSelectedContacts() {
		// 只要被check过，就会在items里，items里并不都是true
		SparseBooleanArray items = contactListView.getCheckedItemPositions();
		List<Contact> selectedContacts=new ArrayList<Contact>();
		for (int i = 0; i < items.size(); i++) {
			if (items.valueAt(i)) {
				selectedContacts.add(contactAdapter.list.get(items.keyAt(i)));
			}
		}
		brain.setSelectedContacts(selectedContacts);
	}

	class SendOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
		}
	}
}
