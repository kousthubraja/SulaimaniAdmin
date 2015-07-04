package com.example.sutestadmin;

public class OrderItem {
	String userName;
	String userMsg;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserMsg() {
		return userMsg;
	}
	public void setUserMsg(String userMsg) {
		this.userMsg = userMsg;
	}
	public OrderItem(String userName, String userMsg) {
		super();
		this.userName = userName;
		this.userMsg = userMsg;
	}
	
}
