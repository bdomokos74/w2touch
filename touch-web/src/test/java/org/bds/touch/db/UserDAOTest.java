package org.bds.touch.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.SQLException;
import java.util.List;

import org.bds.touch.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class UserDAOTest {
	private UserDAO userDao;
	private ApplicationContext appContext;

	@Before
	public void initDB() {
		appContext = new ClassPathXmlApplicationContext(
				"/org/bds/touch/db/testcontext.xml");
		userDao = (UserDAO) appContext.getBean("userDAO");
	}

	@After
	public void resetDB() throws SQLException {
		List<User> users = userDao.findUsers();
		for (User user : users) {
			if(!user.getName().equals("admin"))
				userDao.deleteUserById(user.getId());
		}
		((JdbcUserDAO)userDao).getSimpleJdbcTemplate().update("alter table TOUCHUSER alter column id restart with 1");
	}

	@Test
	public void testFindtUsers() throws SQLException {
		List<User> users = userDao.findUsers();
		assertEquals(1, users.size());
		User user = users.get(0);
		assertEquals("admin", user.getName());
		assertEquals(0, user.getId());
	}
	
	@Test
	public void testFindUserByName() throws Exception {
		User user = userDao.findUserByName("admin");
		assertNotNull(user);
		assertEquals("admin", user.getName());
	}
	
	@Test
	public void testFindUserById() throws Exception {
		User user = userDao.findUserById(0);
		assertNotNull(user);
		assertEquals("admin", user.getName());
	}
	
	@Test
	public void testCreateUser() throws Exception {
		User user = userDao.createUser("balint", "ppppwwww");
		User user2 = userDao.createUser("balint2", "ppppwwww");
		assertNotNull(user);
		assertNotNull(user2);
		assertEquals(2, user2.getId());
		assertEquals("balint2", userDao.findUserById(2).getName());
	}
}
