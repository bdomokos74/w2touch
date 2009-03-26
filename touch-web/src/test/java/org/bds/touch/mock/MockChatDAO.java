package org.bds.touch.mock;

import java.util.List;

import org.bds.touch.db.ChatDAO;
import org.bds.touch.model.Chat;

public class MockChatDAO implements ChatDAO {

	public Chat createChat(String chatName, int ownerId, String otherName) {
		throw new RuntimeException("Not implemented: createChat");
	}

	public void delete(int id) {
		throw new RuntimeException("Not implemented: delete");
	}

	public List<Chat> findAllChat() {
		throw new RuntimeException("Not implemented: findAllChat");
	}

	public List<Chat> findAllChatByUserId(int id) {
		throw new RuntimeException("Not implemented: findAllChatByUserId");
	}

	public Chat findChatById(int id) {
		throw new RuntimeException("Not implemented: findChatById");
	}

	public Chat findChatByName(int ownerId, String chatName) {
		throw new RuntimeException("Not implemented: findChatByName");
	}

}
