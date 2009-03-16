package org.bds.touch.db;

import static junit.framework.Assert.assertEquals;

import java.sql.SQLException;
import java.util.List;

import org.bds.touch.model.Chat;
import org.bds.touch.model.Post;
import org.bds.touch.model.User;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class PostDAOTest {
	private static ApplicationContext appContext;
	private static ChatDAO chatDao;
	private static UserDAO userDao;
	private static PostDAO postDao;
	private static User user;
	private static Chat chat;
	private static Chat otherchat;

	@BeforeClass
	public static void setUp() {
		appContext = new ClassPathXmlApplicationContext(
		"/org/bds/touch/db/testcontext.xml");
		userDao = (UserDAO) appContext.getBean("userDAO");
		chatDao = (ChatDAO) appContext.getBean("chatDAO");
		postDao = (PostDAO) appContext.getBean("postDAO");
		user = userDao.createUser("testuser", "234");	
		chat = chatDao.createChat("mychat", user.getId(), "testname");
		otherchat = chatDao.createChat("myotherchat", user.getId(), "testothername");
	}
	
	@AfterClass
	public static void cleanUp() throws Exception {
		List<Chat> allChats = chatDao.findAllChat();
		for (Chat chat : allChats) {
			chatDao.delete(chat.getId());
		}
		// Delete users other than admin
		List<User> allUsers = userDao.findUsers();
		for(User u : allUsers) {
			if(!u.getName().equals("admin"))
				userDao.deleteUserById(u.getId());
		}
		// Reset the autoincremented index on the user table
		((JdbcChatDAO)chatDao).getSimpleJdbcTemplate().update("alter table USER alter column id restart with 1");
	}

	@Before
	public void initDB() {
		
	}

	@After
	public void resetDB() throws SQLException {
		List<Post> postList = postDao.findAllPosts();
		for(Post post : postList) {
			postDao.delete(post.getId());
		}
	}
	
	@Test
	public void testCreatePost_1() throws Exception {
		Post post = postDao.createPost( chat.getId(), Post.Direction.OUT, "hello there");
		assertEquals("hello there", post.getText());
	}
	@Test
	public void testDeletePost() throws Exception {
		Post post = postDao.createPost( chat.getId(), Post.Direction.OUT, "hello there");
		assertEquals("hello there", post.getText());
		postDao.delete(post.getId());
	}
	
	@Test 
	public void testFindPostsForChat() throws Exception {		
		postDao.createPost( chat.getId(), Post.Direction.OUT, "hello there");
		postDao.createPost( otherchat.getId(), Post.Direction.IN, "hello there again");
		List<Post> postList = postDao.findAllPostsByChatId(chat.getId());
		assertEquals(1, postList.size());
		assertEquals("hello there", postList.get(0).getText());
	}
	
}
