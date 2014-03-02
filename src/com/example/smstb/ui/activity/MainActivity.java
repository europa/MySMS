package com.example.smstb.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
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
import android.widget.ListView;

import com.example.smstb.R;
import com.example.smstb.bean.Conversation;
import com.example.smstb.ui.adapter.ContentAdapter;
import com.example.smstb.util.InfoUtil;

public class MainActivity extends InfoBaseActivity {
	public ListView infosListView;
	private ContentAdapter infosAdapter;
	public ActionMode actionMode;
	public Boolean autoChanged = false;
	MultiChoiceModeListener multiChoiceModeListener = new MultiChoiceModeListener() {

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false;
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
			actionMode = null;
		}

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			actionMode = mode;
			autoChanged = false;
			getMenuInflater().inflate(R.menu.context, menu);
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
				autoChanged=true;
				delete();
				infosAdapter.notifyDataSetChanged();
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
			if (autoChanged) {
				infosAdapter.notifyDataSetChanged();
			} else {
				infosAdapter
						.checkItem(
								infosListView.getChildAt(position-infosListView
										.getFirstVisiblePosition()),
								checked);
			}
		}
	};

	@Override
	protected void onResume() {
		infosAdapter.list = InfoUtil.getItemsByPerson();
		infosAdapter.notifyDataSetChanged();
		super.onResume();
	}

	class ItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent intent = new Intent();
			if (infosAdapter.getItem(position).getCount() == 0) {
				intent.setClass(MainActivity.this, NewActivity.class);
			} else {
				intent.setClass(MainActivity.this, InfosPersonActivity.class);
				brain.setCurrentConversation(infosAdapter.getItem(position));
			}
			startActivity(intent);
		}
	}

	@Override
	public int getLayoutId() {
		return R.layout.layout_main;
	}

	@Override
	public void setView() {
		infosListView = (ListView) findViewById(R.id.infoList);
		infosAdapter = new ContentAdapter(InfoUtil.getItemsByPerson(), this);
		infosListView.setAdapter(infosAdapter);
		infosListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		infosListView.setOnItemClickListener(new ItemClickListener());
		infosListView.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {
				if (actionMode != null) {
					return false;
				}
//				startActionMode(multiChoiceModeListener);
				return false;
			}
		});
		infosListView.setMultiChoiceModeListener(multiChoiceModeListener);
	}

	/**
	 * check all the items or uncheck all the items
	 */
	private void updateAll(MenuItem item) {
		Boolean checked = true;
		if (infosListView.getCheckedItemCount() == infosAdapter.getCount()) {
			checked = false;
			item.setIcon(android.R.drawable.btn_star_big_on);
		} else {
			item.setIcon(android.R.drawable.btn_star_big_off);
			checked = true;
		}

		for (int i = 0; i < infosAdapter.getCount(); i++) {
			if (checked != infosListView.isItemChecked(i)) {
				infosListView.setItemChecked(i, checked);
			}
		}
	}

	private void delete() {
		SparseBooleanArray array = infosListView.getCheckedItemPositions();
		List<Conversation> deletedConversations=new ArrayList<Conversation>();
		for (int i = 0; i < array.size(); i++) {
			Log.i(TAG, ":" + array.keyAt(i));
			Conversation conversation=infosAdapter.getItem(array.keyAt(i));
			InfoUtil.deleteByThreadId(conversation.getId());
			deletedConversations.add(conversation);
		}
		infosAdapter.list.removeAll(deletedConversations);
	}
}
