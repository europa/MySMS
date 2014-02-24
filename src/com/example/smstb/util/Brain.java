package com.example.smstb.util;

import java.util.ArrayList;
import java.util.List;

import com.example.smstb.bean.Contact;
import com.example.smstb.bean.Conversation;
import com.example.smstb.bean.SMSInfo;

public class Brain {
	private static Brain brain = null;

	private Brain() {
	}

	public static Brain newInstance() {
		if (brain == null) {
			brain = new Brain();
		}
		return brain;
	}

	private Conversation currentConversation;
	private List<Contact> contacts=new ArrayList<Contact>();
	private List<Contact> selectedContacts=new ArrayList<Contact>();
	private List<SMSInfo> infos=new ArrayList<SMSInfo>();

	public Conversation getCurrentConversation() {
		return currentConversation;
	}

	public void setCurrentConversation(Conversation currentConversation) {
		this.currentConversation = currentConversation;
	}

	/**
	 * 
	 * @return current conversation's display name
	 */
	public String getCurrentName() {
		String name = "";
		if (currentConversation != null) {
			name = InfoUtil.convertIdSToName(currentConversation
					.getRecipient_ids());
		}
		return name;
	}

	public long getCurrentThreadId() {
		long thread_id = -1;
		if (currentConversation != null) {
			thread_id = currentConversation.getId();
		}
		return thread_id;
	}

	public List<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}

	public List<String> getPinyinList(){
		List<String> pinyinList=new ArrayList<String>();
		for(Contact contact:contacts){
			String startLetter=contact.getPinYin().substring(0,1);
			if(!pinyinList.contains(startLetter)){
				pinyinList.add(startLetter);
			}
		}
		return pinyinList;
	}
	
	public List<Contact> getSelectedContacts() {
		return selectedContacts;
	}

	public void setSelectedContacts(List<Contact> selectedContacts) {
		this.selectedContacts = selectedContacts;
	}
	
	/**
	 * 
	 * @param name:contactEdit's contact item
	 * @return the name's phoneNum
	 */
	public String getContactByName(String name){
		for(Contact contact:contacts){
			if(contact.getName().equals(name)){
				return contact.getPhoneNum();
			}
		}
		return name;
	}
	public List<SMSInfo> getInfos() {
		return infos;
	}

	public void setInfos(List<SMSInfo> infos) {
		this.infos = infos;
	}
	
}
