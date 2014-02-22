package com.example.smstb.iinterface;

import com.example.smstb.bean.SMSInfo;

public interface ListInterface {
	public void deleteItemById(int id);
	public SMSInfo getInfoByPosition(int position);
}
