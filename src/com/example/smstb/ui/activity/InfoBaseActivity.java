package com.example.smstb.ui.activity;

import com.example.smstb.R;
import com.example.smstb.util.Brain;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public abstract class InfoBaseActivity extends BaseActivity {
	

	@Override
	public int getLayoutId() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public void setView() {
		// TODO Auto-generated method stub
		
	}

	public Menu menu;
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_main, menu);
		this.menu=menu;
		return super.onCreateOptionsMenu(menu);
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.i(TAG,"id:"+item.getItemId());
		switch (item.getItemId()) {
		case R.id.write:
        	Intent intent=new Intent();
        	intent.setClass(this,NewActivity.class);
        	startActivity(intent);
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
