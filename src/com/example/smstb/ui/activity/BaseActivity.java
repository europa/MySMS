package com.example.smstb.ui.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.smstb.util.Brain;
import com.example.smstb.util.InfoUtil;

public abstract class BaseActivity extends Activity {

	public String TAG = this.getClass().getSimpleName();
	public Brain brain = null;
	ActionBar actionBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		brain = Brain.newInstance();
		InfoUtil.newInstance(this);
		super.onCreate(savedInstanceState);
		getActionBar(getLayoutId());
		setView();
	}

	public void toast(String toast) {
		Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
	}

	public void toast(int toast) {
		Toast.makeText(this, getString(toast), Toast.LENGTH_SHORT).show();
	}
	
	
	public void getActionBar(int id){
		setContentView(id);
		actionBar=getActionBar();
	}
	
	public abstract int getLayoutId();
	public abstract void setView();
}
