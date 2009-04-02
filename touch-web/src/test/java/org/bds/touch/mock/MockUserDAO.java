package org.bds.touch.mock;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bds.touch.db.UserDAO;
import org.bds.touch.model.User;
import org.springframework.beans.factory.InitializingBean;

public class MockUserDAO implements UserDAO, InitializingBean {

	private List<User> users;
	private Map<String,User> usersByName = new HashMap<String, User>();

	public User createUser(String name, String password) {
		User u = new User(users.size()+1, name, password);
		users.add(u);
		usersByName.put(u.getName(), u);
		return u;
	}

	public void deleteUserById(int id) {
		throw new RuntimeException("Not implemented: deleteUserById");
	}

	public User findUserById(int id) {
		return users.get(id);
	}

	public User findUserByName(String name) {
		return usersByName.get(name);
	}

	public List<User> findUsers() {
		return users;
	}

	/*
	 * Mock methods
	 */
	public void setUsers(List<User> users) {
		this.users = users;
	}

	public void afterPropertiesSet() throws Exception {
		for(User u : users) {
			usersByName.put(u.getName(), u);
		}
	}

}
