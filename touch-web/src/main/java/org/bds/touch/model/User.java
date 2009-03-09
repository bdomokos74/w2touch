package org.bds.touch.model;

public class User {
	private final String name;
	private final String pw;
	private final int id;

	public User(int id, String name, String pw) {
		this.id = id;
		this.name = name;
		this.pw = pw;
	}

	public String getName() {
		return name;
	}

	public String getPw() {
		return pw;
	}
	
	public int getId() {
		return id;
	}
}
