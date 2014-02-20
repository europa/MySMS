package com.example.smstb;

import java.io.Serializable;

public class ItemInfo implements Serializable{
	private SMSInfo smsInfo;
	private int amount;
	
	public SMSInfo getSmsInfo() {
		return smsInfo;
	}

	public void setSmsInfo(SMSInfo smsInfo) {
		this.smsInfo = smsInfo;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
}
