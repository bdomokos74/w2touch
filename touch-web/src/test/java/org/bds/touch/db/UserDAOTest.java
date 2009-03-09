package org.bds.touch.db;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.commons.dbcp.BasicDataSource;
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
		Connection conn = null;
		Statement statement = null;
		BasicDataSource dataSource = null;
		try {
			dataSource = (BasicDataSource)appContext.getBean("dataSource");
			conn = dataSource.getConnection();
			statement = conn.createStatement();
			statement.executeUpdate("delete from USER where name != 'admin'");
			// reset the autoincremented index
			statement.executeUpdate("alter table USER alter column id restart with 1");
		}finally {
			statement.close();
			conn.close();
			dataSource.close();
		}
		
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
	}
}
