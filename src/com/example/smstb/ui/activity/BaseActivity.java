package com.example.smstb.ui.activity;

import com.example.smstb.util.Brain;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class BaseActivity extends Activity {

	public String TAG = this.getClass().getSimpleName();
	public Brain brain=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		brain=Brain.newInstance();
		super.onCreate(savedInstanceState);
	}
	
	public void toast(String toast){
		Toast.makeText(this, toast,Toast.LENGTH_SHORT).show();
	}
	public void toast(int toast){
		Toast.makeText(this,getString(toast),Toast.LENGTH_SHORT).show();
	}
}
