package com.example.smstb.bean;

import java.io.Serializable;

import android.graphics.Bitmap;

public class Contact implements Serializable{
	private String name;
	private String phoneNum;
	private Bitmap portrait;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	public Bitmap getPortrait() {
		return portrait;
	}
	public void setPortrait(Bitmap portrait) {
		this.portrait = portrait;
	}
}
