package org.bds.touch.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bds.touch.db.UserDAO;
import org.bds.touch.model.User;

public class MockUserDAO implements UserDAO {

	private Map<Integer,User> usersById;
	private Map<String,User> usersByName;

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
		throw new RuntimeException("Not implemented: findUserByName");
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

}
