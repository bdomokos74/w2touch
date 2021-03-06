package org.bds.touch.db;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.SQLException;
import java.util.List;

import org.bds.touch.model.Chat;
import org.bds.touch.model.User;
import org.bds.touch.rest.AbstractResource;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.DataIntegrityViolationException;

public class ChatDAOTest {
	private static ChatDAO chatDao;
	private static UserDAO userDao;
	private static ApplicationContext appContext;
	private static User user;
	private static User user2;
	private String otherName= "othername";

	@BeforeClass
	public static void setUp() {
		appContext = new ClassPathXmlApplicationContext(
		"/org/bds/touch/db/testcontext.xml");
		chatDao = (ChatDAO) appContext.getBean("chatDAO");
		userDao = (UserDAO) appContext.getBean("userDAO");
		user = userDao.createUser("testuser", "234");		
		user2 = userDao.createUser("testuser2", "435");
	}
	
	@AfterClass
	public static void cleanUp() throws Exception {
		// Delete users other than admin
		List<User> allUsers = userDao.findUsers();
		for(User u : allUsers) {
			if(!u.getName().equals("admin"))
				userDao.deleteUserById(u.getId());
		}
		// Reset the autoincremented index on the user table
		((JdbcChatDAO)chatDao).getSimpleJdbcTemplate().update("alter table TOUCHUSER alter column id restart with 1");
	}

	@Before
	public void initDB() {
		
	}

	
	@After
	public void resetDB() throws SQLException {
		List<Chat> chatList = chatDao.findAllChat();
		for(Chat chat : chatList) {
			chatDao.delete(chat.getId());
		}
	}

	@Test
	public void testEncode() throws Exception {
		String result = AbstractResource.encodeUrl("test$ 1");
		assertEquals("test%24+1", result);
		assertEquals("test$ 1", AbstractResource.decodeUrl(result));	
	}
	
	@Test
	public void testFindAll_empty() throws Exception {
		List<Chat> list = chatDao.findAllChat();
		assertNotNull(list);
		assertTrue(list.size()==0);
	}
	
	@Test
	public void testCreateChat_1() throws Exception {
		Chat chat = chatDao.createChat("chatname", user.getId(), otherName );
		assertEquals("othername", chat.getOtherName());
		assertEquals("chatname", chat.getChatName());
	}
	@Test
	public void testFindChatByUserId_1() throws Exception {
		Chat chat = chatDao.createChat("chatname1", user.getId(), otherName );
		Chat chat2 = chatDao.createChat("chatname2", user2.getId(), otherName );
		List<Chat> chatList = chatDao.findAllChatByUserId(user2.getId());
		assertEquals(1, chatList.size());
		assertEquals("chatname2", chatList.get(0).getChatName());
	}
	@Test
	public void testFindChatByUserName_1() throws Exception {
		Chat chat = chatDao.createChat("chatname1", user.getId(), otherName );
		Chat chat2 = chatDao.createChat("chatname2", user2.getId(), otherName );
		List<Chat> chatList = chatDao.findAllChatByUserName(user2.getName());
		assertEquals(1, chatList.size());
		assertEquals("chatname2", chatList.get(0).getChatName());
	}
	@Test
	public void testFindChatByName_1() throws Exception {
		Chat chat = chatDao.createChat("chatname1", user.getId(), otherName );
		Chat chat2 = chatDao.createChat("chatname2", user2.getId(), otherName );
		Chat readFromDb = chatDao.findChatByName(user2.getName(), "chatname2");
		assertNotNull(readFromDb);
		assertEquals("chatname2", readFromDb.getChatName());
	}
	@Test
	public void testDeleteChat() throws Exception {
		Chat chat = chatDao.createChat("chatname", user.getId(), otherName );
		chatDao.delete(chat.getId());
	}
	
	@Test
	public void testCreateChat_owner_chatname_constraint() throws Exception {
		Chat chat1 = chatDao.createChat("chatname", user.getId(), otherName );
		
		try {
			Chat chat2 = chatDao.createChat("chatname", user.getId(), "othername2" );
			fail("Exception expected, unique constraint is violated");
		} catch (DataIntegrityViolationException e) {
		}
	}
	
	
	@Test
	public void testCreateChat_specialchar() throws Exception {
		@SuppressWarnings("unused")
		Chat chat1 = chatDao.createChat("chatname$ %", user.getId(), otherName );
		Chat readFromDb = chatDao.findChatByName(user.getName(), "chatname$ %");
		assertNotNull(readFromDb);
		assertEquals("chatname$ %", readFromDb.getChatName());
	}
}
