package org.bds.touch.db;

import java.util.List;

import org.bds.touch.model.Chat;

public interface ChatDAO {

	Chat createChat(String string, int ownerId, String otherName);
	void delete(int id);
	List<Chat> findAllChat();
	List<Chat> findAllChatByUserId(int id);
	Chat findChatById(int id);

}
