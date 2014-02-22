/**
 * 
 */
package com.example.smstb.ui.view;

import com.example.smstb.ui.activity.NewActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

/**
 * 
 */
public class NewButton extends Button {

    public NewButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public NewButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NewButton(Context context) {
        super(context);
        init();
    }

    private void init() {
        setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
            	Activity activity=(Activity)getContext();
            	Intent intent=new Intent();
            	intent.setClass(activity,NewActivity.class);
            	activity.startActivity(intent);
            }
        });
    }
}
