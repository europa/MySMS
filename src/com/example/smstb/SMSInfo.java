package com.example.smstb;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class SMSInfo implements Serializable{
	private String phoneNum;
	private String time;
	private String content;
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
