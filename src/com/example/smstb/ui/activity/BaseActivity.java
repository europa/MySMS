package com.example.smstb.ui.activity;

import com.example.smstb.util.Brain;

import android.app.Activity;
import android.os.Bundle;

public class BaseActivity extends Activity {

	public String TAG = this.getClass().getSimpleName();
	public Brain brain=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		brain=Brain.newInstance();
		super.onCreate(savedInstanceState);
	}
}
