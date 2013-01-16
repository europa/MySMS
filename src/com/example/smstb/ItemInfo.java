package com.example.smstb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.os.Parcelable;

public class ItemInfo implements Serializable{
	private List<SMSInfo> smsInfos;

	public ArrayList<SMSInfo> getSmsInfos() {
		return (ArrayList<SMSInfo>) smsInfos;
	}

	public void setSmsInfos(List<SMSInfo> smsInfos) {
		this.smsInfos = smsInfos;
	}
}
