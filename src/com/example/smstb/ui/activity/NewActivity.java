package com.example.smstb.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.smstb.R;
import com.example.smstb.bean.Contact;
import com.example.smstb.util.Constants;
import com.example.smstb.util.InfoUtil;

public class NewActivity extends SendBaseActivity {
	private EditText contactEditText;
	private EditText replyEditText;
	private Button sendBtn;
	private String reply;

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		switch (arg1) {
		case RESULT_OK:
			String name = "";
			List<Contact> selectedContacts = brain.getSelectedContacts();
			for (int i = 0; i < selectedContacts.size(); i++) {
				name += selectedContacts.get(i).getName();
				name += ",";
			}
			contactEditText.setText(name);
			contactEditText.setSelection(name.length());
			break;
		default:
			break;
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (replyEditText.getText().toString().trim().equals("")) {
			return;
		}
		info.setBody(replyEditText.getText().toString().trim());
		info.setAddress(contactEditText.getText().toString().trim());
		info.setType(Constants.DRAFT);
		InfoUtil.insertInfo(this,info);
	}

	class BtnOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.send:
				handleInfo();
				break;
			default:
				break;
			}
		}
	}

	private void handleInfo() {
		reply = replyEditText.getText().toString();
		String contactStr = contactEditText.getText().toString();
		if (contactStr.trim().equals("")) {
			Toast.makeText(this, R.string.phonenum_not_null, 1000).show();
		} else if (reply.equals("")) {
			Toast.makeText(this, R.string.not_null, 1000).show();
		} else {
			String[] names = contactStr.split(",");
			for (int i = 0; i < names.length; i++) {
				if(names[i].trim().equals("")){
					continue;
				}
				info.setAddress(brain.getContactByName(names[i]));
				info.setBody(reply);
				sendInfo(info);
				replyEditText.setText("");
			}

		}
	}

	private void toContact() {
		Intent intent = new Intent();
		intent.setClass(this, ContactActivity.class);
		startActivityForResult(intent, 0);
	}

	private List<String> getAutoStrList() {
		List<String> autoStrList = new ArrayList<String>();
		for (Contact contact : brain.getContacts()) {
			if(contact.getName().equals(contact.getPhoneNum())){
				autoStrList.add(contact.getName());
			}else{
				autoStrList.add(contact.getName()+" "+contact.getPhoneNum());
			}
		}
		return autoStrList;
	}

	@Override
	public int getLayoutId() {
		return R.layout.new_info;
	}

	@Override
	public void setView() {
		contactEditText = (EditText) findViewById(R.id.contact);
		replyEditText = (EditText) findViewById(R.id.reply);
		sendBtn = (Button) findViewById(R.id.send);
		actionBar.setTitle("新建短信");
		if (brain.getContacts().size() == 0) {
			brain.setContacts(InfoUtil.getContacts());
		}
		contactEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String str=s.toString();
				//count==0,delete,or add
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		sendBtn.setOnClickListener(new BtnOnClickListener());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.findItem(R.id.write).setIcon(R.drawable.contact);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		toContact();
		return true;
	}

}
