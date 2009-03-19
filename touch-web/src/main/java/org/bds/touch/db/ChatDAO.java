package org.bds.touch.db;

import java.util.List;

import org.bds.touch.model.Chat;

public interface ChatDAO {

	Chat createChat(String chatName, int ownerId, String otherName);
	void delete(int id);
	List<Chat> findAllChat();
	List<Chat> findAllChatByUserId(int id);
	Chat findChatById(int id);
	Chat findChatByName(int ownerId, String chatName);

}
