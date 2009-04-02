package org.bds.touch.mock;

import java.util.ArrayList;
import java.util.List;

import org.bds.touch.db.ChatDAO;
import org.bds.touch.db.ServiceLocator;
import org.bds.touch.model.Chat;
import org.bds.touch.model.User;

public class MockChatDAO implements ChatDAO {

	public Chat createChat(String chatName, int ownerId, String otherName) {
		Chat chat = new Chat(chats.size()+1, chatName, ownerId, otherName);
		chats.add(chat);
		return chat;
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
		User user = ServiceLocator.getUserDao().findUserByName(ownerName);
		if(user==null) {
			return null;
		}
			
		for(Chat c : chats) {
			if(c.getChatName().equals(chatName) && c.getOwnerId()==user.getId())
				return c;
		}
		return null;
	}

	public List<Chat> findAllChatByUserName(String userName) {
		User user = ServiceLocator.getUserDao().findUserByName(userName);
		List<Chat> list = new ArrayList<Chat>();
		for(Chat c : chats) {
			if(c.getOwnerId() == user.getId())
				list.add(c);
		}
		return list;
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
