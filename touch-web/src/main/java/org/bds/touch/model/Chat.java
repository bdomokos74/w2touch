package org.bds.touch.model;

import java.util.Date;

public class Chat {
	private int id;
	private String chatName;
	private String userName;
	private String otherName;
	private int ownerId;
	private Date lastModified;
	
	public Chat(int id, Date lastModified, String chatname, int ownerId, String otherName) {
		this(id, chatname, ownerId, otherName);
		this.lastModified = lastModified;
	}
	
	public Chat(int id, String chatname, int ownerId, String otherName) {
		this.id = id;
		chatName = chatname;
		this.ownerId = ownerId;
		this.otherName = otherName;
	}
	
	public String getChatName() {
		return chatName;
	}
	public void setChatName(String chatName) {
		this.chatName = chatName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getOtherName() {
		return otherName;
	}
	public void setOtherName(String otherName) {
		this.otherName = otherName;
	}
	public int getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}
	public int getId() {
		return id;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public Date getLastModified() {
		return lastModified;
	}
	
	@Override
	public String toString() {
		return "Chat["+getId()+","+getChatName()+","+getOwnerId()+"]";		
	}
}
