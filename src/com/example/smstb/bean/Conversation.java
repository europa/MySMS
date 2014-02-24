package com.example.smstb.bean;

public class Conversation {
	long id;// the key of threads table
	long date;// time
	String date_str;// date str
	long count;// the message's amount
	String recipient_ids;// the receivers' ids
	String names;// the people's names
	String snippet;// the latest message's content
	long snippet_cs;// the snippet's code style;mms:UTF-8 is 106，sms:0
	long read;// 0:unread;1:read看
	long error;//

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public String getDate_str() {
		return date_str;
	}

	public void setDate_str(String date_str) {
		this.date_str = date_str;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public String getRecipient_ids() {
		return recipient_ids;
	}

	public void setRecipient_ids(String recipient_ids) {
		this.recipient_ids = recipient_ids;
	}

	public String getNames() {
		return names;
	}

	public void setNames(String names) {
		this.names = names;
	}

	public String getSnippet() {
		return snippet;
	}

	public void setSnippet(String snippet) {
		this.snippet = snippet;
	}

	public long getSnippet_cs() {
		return snippet_cs;
	}

	public void setSnippet_cs(long snippet_cs) {
		this.snippet_cs = snippet_cs;
	}

	public long getRead() {
		return read;
	}

	public void setRead(long read) {
		this.read = read;
	}

	public long getError() {
		return error;
	}

	public void setError(long error) {
		this.error = error;
	}
}
