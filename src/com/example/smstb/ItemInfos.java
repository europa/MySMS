package com.example.smstb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.os.Parcelable;

public class ItemInfos implements Serializable{
	private List<SMSInfo> smsInfos;
	private long lastTime;

	public ArrayList<SMSInfo> getSmsInfos() {
		return (ArrayList<SMSInfo>) smsInfos;
	}

	public void setSmsInfos(List<SMSInfo> smsInfos) {
		this.smsInfos = smsInfos;
	}

	public long getLastTime() {
		return lastTime;
	}

	public void setLastTime(long lastTime) {
		this.lastTime = lastTime;
	}
}
