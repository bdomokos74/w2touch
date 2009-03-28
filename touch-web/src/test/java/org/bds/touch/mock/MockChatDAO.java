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
		for(Chat c : chats) {
			if(c.getId()==id)
				return c;
		}
		return null;
	}

	public Chat findChatByName(String ownerName, String chatName) {
		if(!userName.equals(ownerName)) {
			return null;
		}
			
		for(Chat c : chats) {
			if(c.getChatName().equals(chatName))
				return c;
		}
		return null;
	}

	public List<Chat> findAllChatByUserName(String userName) {
		if (this.userName.equals(userName))
			return chats;
		else
			return null;
	}

	private String userName;

	public void setUserName(String userName) {
		this.userName = userName;
	}

	private List<Chat> chats;

	public void setChats(List<Chat> chats) {
		this.chats = chats;
	}

}
