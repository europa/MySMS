package com.example.smstb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.os.Parcelable;

public class ItemInfo implements Serializable{
	private SMSInfo smsInfo;
	private long lastTime;

	public SMSInfo getSmsInfo() {
		return  smsInfo;
	}

	public void setSmsInfo(SMSInfo smsInfo) {
		this.smsInfo = smsInfo;
	}

	public long getLastTime() {
		return lastTime;
	}

	public void setLastTime(long lastTime) {
		this.lastTime = lastTime;
	}
	
}
