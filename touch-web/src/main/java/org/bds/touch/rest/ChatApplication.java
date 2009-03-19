package org.bds.touch.rest;

import org.bds.touch.db.ChatDAO;
import org.bds.touch.db.PostDAO;
import org.bds.touch.db.UserDAO;
import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.Router;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ChatApplication extends Application {
	private UserDAO userDao;
	private ChatDAO chatDao;
	private PostDAO postDao;

	public ChatApplication() {
		ApplicationContext appContext = new ClassPathXmlApplicationContext(
				"/appcontext.xml");
		userDao = (UserDAO) appContext.getBean("userDAO");
		chatDao = (ChatDAO) appContext.getBean("chatDAO");
		postDao = (PostDAO) appContext.getBean("postDAO");
	}

	/**
	 * Creates a root Restlet that will receive all incoming calls.
	 */
	@Override
	public synchronized Restlet createRoot() {
		// Create a router Restlet that routes each call to a
		// new instance of HelloWorldResource.
		Router router = new Router(getContext());

		// Defines only one route
		router.attach("/users", UserListResource.class); // GET,POST(name,pw)
		router.attach("/users/{userId}", UserResource.class); // GET
		router.attach("/users/{userId}/chats", ChatListResource.class); // GET,POST(chatName,partyName)
		router.attach("/users/{userId}/chats/{chatName}", ChatResource.class); // GET,PUT,POST(text,direction),DELETE
		router.attach("/users/{userId}/chats/{chatName}/{postId}", PostResource.class); // GET
		//router.attach("/users/{userId}/contacts", ContactListResource.class); // GET
		//router.attach("/users/{userId}/contacts/{contactName}", ContactResource.class); // GET,PUT

		return router;
	}
	
	public UserDAO getUserDao() {
		return userDao;
	}

	public ChatDAO getChatDao() {
		return chatDao;
	}
	
	public PostDAO getPostDao() {
		return postDao;
	}
}
