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
import android.widget.AdapterView.OnItemSelectedListener;

public abstract class InfoBaseActivity extends BaseActivity {
	
	public Menu menu;
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_main, menu);
		return true;
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent=new Intent();
		intent.setClass(this,NewActivity.class);
		startActivity(intent);
		return true;
	}
}
