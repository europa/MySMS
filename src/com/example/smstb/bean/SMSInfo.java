package com.example.smstb.bean;

import java.io.Serializable;

public class SMSInfo implements Serializable {

	private long id;// the key of sms
	private long thread_id;// the sms's therea_id
	private String address;// the receiver's phoneNum
	private long person;// the id of contacts,or null.
	long date;// the date in ms
	long read;// 0:unread;1:read.
	private long type;// ALL=0;INBOX=1;SENT=2;DRAFT=3;OUTBOX=4;FAILED=5;QUEUED=6;
	private String body;
	private String service_center;
	private long locked;// whether be locked by user:0,unlocked;1,locked

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getThread_id() {
		return thread_id;
	}

	public void setThread_id(long thread_id) {
		this.thread_id = thread_id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public long getPerson() {
		return person;
	}

	public void setPerson(long person) {
		this.person = person;
	}

	public long getRead() {
		return read;
	}

	public void setRead(long read) {
		this.read = read;
	}

	public long getType() {
		return type;
	}

	public void setType(long type) {
		this.type = type;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getService_center() {
		return service_center;
	}

	public void setService_center(String service_center) {
		this.service_center = service_center;
	}

	public long getLocked() {
		return locked;
	}

	public void setLocked(long locked) {
		this.locked = locked;
	}

}
