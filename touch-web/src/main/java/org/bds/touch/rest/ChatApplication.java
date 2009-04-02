package org.bds.touch.rest;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.Router;

public class ChatApplication extends Application {

	public ChatApplication() {
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
		router.attach("/users/{userName}", UserResource.class); // GET
		router.attach("/users/{userName}/chats", ChatListResource.class); // GET,POST(chatName,partyName)
		router.attach("/users/{userName}/chats/{chatName}", ChatResource.class); // GET,PUT,POST(text,direction),DELETE
		router.attach("/users/{userName}/chats/{chatName}/{postId}", PostResource.class); // GET
		//router.attach("/users/{userId}/contacts", ContactListResource.class); // GET
		//router.attach("/users/{userId}/contacts/{contactName}", ContactResource.class); // GET,PUT

		return router;
	}
}
