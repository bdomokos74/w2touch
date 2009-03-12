package org.bds.touch.rest;

import org.bds.touch.db.UserDAO;
import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.Router;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ChatApplication extends Application {
	private UserDAO userDao;

	public ChatApplication() {
		ApplicationContext appContext = new ClassPathXmlApplicationContext(
				"/appcontext.xml");
		userDao = (UserDAO) appContext.getBean("userDAO");
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
		router.attach("/users", UserListResource.class);
		router.attach("/users/{userId}", UserResource.class);

		return router;
	}
	
	public UserDAO getUserDao() {
		return userDao;
	}
}
