package com.example.smstb.bean;

import java.io.Serializable;

import android.graphics.Bitmap;

public class Contact implements Serializable {

	private String name;
	private String pinYin;
	private String phoneNum;

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

	public String getPinYin() {
		return pinYin;
	}

	public void setPinYin(String pinYin) {
		this.pinYin = pinYin;
	}

}
