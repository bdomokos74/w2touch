package org.bds.touch.db;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ServiceLocator {
	private static UserDAO userDao;
	private static ChatDAO chatDao;
	private static PostDAO postDao;
	private static ApplicationContext appContext;
	static {
		appContext = new ClassPathXmlApplicationContext(
				"/appcontext.xml");
		userDao = (UserDAO) appContext.getBean("userDAO");
		chatDao = (ChatDAO) appContext.getBean("chatDAO");
		postDao = (PostDAO) appContext.getBean("postDAO");
	}

	private ServiceLocator() {
	}

	public static synchronized void setContext(String appContextConfig) {
		appContext = new ClassPathXmlApplicationContext(
				appContextConfig);
		userDao = (UserDAO) appContext.getBean("userDAO");
		chatDao = (ChatDAO) appContext.getBean("chatDAO");
		postDao = (PostDAO) appContext.getBean("postDAO");
	}

	public static UserDAO getUserDao() {
		return userDao;
	}

	public static ChatDAO getChatDao() {
		return chatDao;
	}

	public static PostDAO getPostDao() {
		return postDao;
	}
	
	public static ApplicationContext getAppContext() {
		return appContext;
	}
	
	public static Object getBean(String name) {
		return appContext.getBean(name);
	}
}
