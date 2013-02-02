package com.example.smstb;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class SMSInfo implements Serializable{
	private int id;
	private String phoneNum;
	private String name;
	private String time;
	private String content;
	private int type;
	private long oTime;
	private long thread_id;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
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
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getoTime() {
		return oTime;
	}
	public void setoTime(long oTime) {
		this.oTime = oTime;
	}
	public long getThread_id() {
		return thread_id;
	}
	public void setThread_id(long thread_id) {
		this.thread_id = thread_id;
	}
	
}
