package org.bds.touch.mock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bds.touch.db.PostDAO;
import org.bds.touch.model.Chat;
import org.bds.touch.model.Post;
import org.bds.touch.model.Post.Direction;
import org.springframework.beans.factory.InitializingBean;

public class MockPostDAO implements PostDAO, InitializingBean {

	public Post createPost(int chatId, Direction direction, String text) {
		Post newPost = new Post(posts.size()+1, chatId, direction, text);
		posts.add(newPost);

		addPostByChatId(newPost);

		return newPost;
	}

	public void delete(int id) {
		deleteFromPosts(id);
		deleteFromPostsById(id);
	}

	private void deleteFromPostsById(int id) {
		for (List<Post> list : postsByChatId.values()) {
			Iterator<Post> it = list.iterator();
			while (it.hasNext()) {
				Post p = it.next();
				if (p.getId() == id) {
					it.remove();
				}
			}
		}
	}

	private void deleteFromPosts(int id) {
		Iterator<Post> it = posts.iterator();
		while (it.hasNext()) {
			Post p = it.next();
			if (p.getId() == id)
				it.remove();
		}
	}

	// FIXME remove this method completely as it is not needed
	public List<Post> findAllPosts() {
		throw new RuntimeException("Not implemented: findAllPosts");
	}

	public List<Post> findAllPostsByChatId(int chatId) {
		return postsByChatId.get(chatId);
	}

	public List<Post> findAllPostsByOwnerIdAndChatName(int ownerId,	String chatName) {
		return findAllPostsByChatId(findChatId(ownerId, chatName));
	}

	private int findChatId(int ownerId, String chatName) {
		for(Chat c : chats) {
			if(c.getOwnerId() == ownerId && c.getChatName().equals(chatName))
				return c.getId();
		}
		return -1;
	}

	public Post findPostById(int id) {
		for (Post p : posts) {
			if (p.getId() == id)
				return p;
		}
		return null;
	}

	private List<Chat> chats;
	public void setChats(List<Chat> chats) {
		this.chats = chats;
	}
	
	private List<Post> posts;
	private Map<Integer, List<Post>> postsByChatId = new HashMap<Integer, List<Post>>();

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}

	public void afterPropertiesSet() throws Exception {
		for (Post p : posts) {
			addPostByChatId(p);
		}
	}

	private void addPostByChatId(Post p) {
		List<Post> postList = postsByChatId.get(p.getChatId());
		if (postList == null) {
			postList = new ArrayList<Post>();
			postsByChatId.put(p.getChatId(), postList);
		}
		postList.add(p);
	}
}
