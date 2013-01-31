/**
 * 
 */
package com.example.smstb;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
            	Intent intent=new Intent();
            	intent.setClass(getContext(),NewActivity.class);
                ((Activity)getContext()).finish();
            }
        });
    }
}
