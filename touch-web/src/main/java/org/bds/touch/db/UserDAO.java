package org.bds.touch.db;

import java.util.List;

import org.bds.touch.model.User;

public interface UserDAO {

	List<User> findUsers();

	User findUserByName(String name);

	User findUserById(int id);

	User createUser(final String name, final String password);

}