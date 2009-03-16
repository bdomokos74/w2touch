package org.bds.touch.model;

public class User {
	private String name;
	private String pw;
	private int id;

	public User(int id, String name, String pw) {
		this.id = id;
		this.name = name;
		this.pw = pw;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getPw() {
		return pw;
	}
	
	public void setPw(String pw) {
		this.pw = pw;
	}
	
	@Override
	public String toString() {
		return "User["+id+","+name+"]";
	}
}
