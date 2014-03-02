package com.example.smstb.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smstb.R;
import com.example.smstb.bean.SMSInfo;
import com.example.smstb.ui.adapter.SMSInfoAdapter;
import com.example.smstb.util.Constants;
import com.example.smstb.util.InfoUtil;

public class InfosPersonActivity extends SendBaseActivity {
	public ListView infoList;
	SMSInfoAdapter smsInfoAdapter;
	EditText replyEditText;
	Boolean autoChanged = false;
	public Boolean autoLink = true;
	public ActionMode actionMode;
	MultiChoiceModeListener multiChoiceModeListener = new MultiChoiceModeListener() {

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false;
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
			autoLink = true;
			actionMode = null;
			// need refresh the listview because sometimes the
			// onActionItemClicked is not invoked
			smsInfoAdapter.notifyDataSetChanged();
			Log.i(TAG, "destroy");
		}

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			actionMode = mode;
			autoChanged = false;
			getMenuInflater().inflate(R.menu.context, menu);
			Log.i(TAG,"onCreate");
			return true;
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			switch (item.getItemId()) {
			case R.id.item_all:
				autoChanged = true;
				updateAll(item);
				break;
			case R.id.item_delete:
				autoChanged = true;
				delete();
				mode.finish();
				break;
			default:
				break;
			}
			return false;
		}

		@Override
		public void onItemCheckedStateChanged(ActionMode mode, int position,
				long id, boolean checked) {
			// autoChanged come from the MenuItem of all.In the case,can't
			// refresh one item by one item.
			Log.i(TAG, "position:" + position + ",checked:" + checked);
			if (autoChanged) {
				smsInfoAdapter.notifyDataSetChanged();
			} else {
				smsInfoAdapter.checkItem(
						infoList.getChildAt(position
								- infoList.getFirstVisiblePosition()), checked);
			}
		}
	};

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
		String reply = replyEditText.getText().toString();
		if ((draft == null && !reply.trim().equals(""))
				|| (draft != null && (!reply.trim().equals("")))) {
			String id = brain.getCurrentConversation().getRecipient_ids()
					.split(" ")[0];
			info.setAddress(InfoUtil.getPhoneNum(Integer.parseInt(id)));
			info.setType(Constants.DRAFT);
			info.setBody(reply);
			InfoUtil.insertInfo(this, info);
		} else if (draft != null && reply.trim().equals("")) {
			InfoUtil.deleteSMSInfoById(draft.getId());
		}
		super.onPause();
	}

	private void handleInfo() {
		String reply = replyEditText.getText().toString();
		if (reply.equals("")) {
			Toast.makeText(this, R.string.not_null, 1000).show();
		} else {
			for (String id : brain.getCurrentConversation().getRecipient_ids()
					.split(" ")) {
				info.setAddress(InfoUtil.getPhoneNum(Integer.parseInt(id)));
				info.setBody(reply);
				sendInfo(info);
				replyEditText.setText("");
			}

		}
	}

	private SMSInfo getDraft() {
		SMSInfo draft = null;
		for (SMSInfo info : brain.getInfos()) {
			if (info.getType() == Constants.DRAFT) {
				draft = info;
				break;
			}
		}
		if (draft != null) {
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
		draft = getDraft();
		if (draft != null) {
			replyEditText.setText(draft.getBody());
		}
		smsInfoAdapter = new SMSInfoAdapter(brain.getInfos(), this);
		infoList.setAdapter(smsInfoAdapter);
		infoList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		infoList.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				Log.i(TAG, "onItemlongClick");
				return true;
			}

		});
		infoList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Log.i(TAG, "onItem arg2:" + arg2 + "");
			}
		});
		infoList.setMultiChoiceModeListener(multiChoiceModeListener);
		actionBar.setTitle(brain.getCurrentName());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_infos, menu);
		smsInfoAdapter.notifyDataSetChanged();
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent();
		switch (item.getItemId()) {
		case R.id.dial:
			intent.setAction(Intent.ACTION_DIAL);
			intent.setData(Uri.parse("tel:"
					+ brain.getContactByName(brain.getCurrentName())));
			startActivity(intent);
			break;
		case R.id.write:
			intent.setClass(this, NewActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
		return true;
	}
	

	/**
	 * check all the items or uncheck all the items
	 */
	private void updateAll(MenuItem item) {
		Boolean checked = true;
		if (infoList.getCheckedItemCount() == smsInfoAdapter.getCount()) {
			checked = false;
			item.setIcon(android.R.drawable.btn_star_big_on);
		} else {
			item.setIcon(android.R.drawable.btn_star_big_off);
			checked = true;
		}

		for (int i = 0; i < smsInfoAdapter.getCount(); i++) {
			if (checked != infoList.isItemChecked(i)) {
				infoList.setItemChecked(i, checked);
			}
		}
	}

	private void delete() {
		SparseBooleanArray array = infoList.getCheckedItemPositions();
		List<SMSInfo> deletedSMSInfos=new ArrayList<SMSInfo>();
		for (int i = 0; i < array.size(); i++) {
			Log.i(TAG, ":" + array.keyAt(i));
			SMSInfo smsInfo=smsInfoAdapter.getItem(array.keyAt(i));
			InfoUtil.deleteSMSInfoById(smsInfo.getId());
			deletedSMSInfos.add(smsInfo);
		}
		smsInfoAdapter.list.removeAll(deletedSMSInfos);
	}
	
	public void startActionMode(){
		startActionMode(multiChoiceModeListener);
	}
}
