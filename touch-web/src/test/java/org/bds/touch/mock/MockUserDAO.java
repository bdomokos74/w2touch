package org.bds.touch.mock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bds.touch.db.UserDAO;
import org.bds.touch.model.User;
import org.springframework.beans.factory.InitializingBean;

public class MockUserDAO implements UserDAO, InitializingBean {

	private Map<Integer,User> usersById;
	private Map<String,User> usersByName = new HashMap<String, User>();

	public User createUser(String name, String password) {
		throw new RuntimeException("Not implemented: createUser");
	}

	public void deleteUserById(int id) {
		throw new RuntimeException("Not implemented: deleteUserById");
	}

	public User findUserById(int id) {
		return usersById.get(id);
	}

	public User findUserByName(String name) {
		return usersByName.get(name);
	}

	public List<User> findUsers() {
		return new ArrayList<User>(usersById.values());
	}

	/*
	 * Mock methods
	 */
	public void setUsers(Map<Integer, User> users) {
		this.usersById = users;
	}

	public void afterPropertiesSet() throws Exception {
		for(User u : usersById.values()) {
			usersByName.put(u.getName(), u);
		}
	}

}
