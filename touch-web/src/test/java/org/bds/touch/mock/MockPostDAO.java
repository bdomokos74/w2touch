package org.bds.touch.mock;

import java.util.List;

import org.bds.touch.db.PostDAO;
import org.bds.touch.model.Post;
import org.bds.touch.model.Post.Direction;

public class MockPostDAO implements PostDAO {

	public Post createPost(int chatId, Direction direction, String text) {
		throw new RuntimeException("Not implemented: createPost");
	}

	public void delete(int id) {
		throw new RuntimeException("Not implemented: delete");
	}

	public List<Post> findAllPosts() {
		throw new RuntimeException("Not implemented: findAllPosts");
	}

	public List<Post> findAllPostsByChatId(int chatId) {
		throw new RuntimeException("Not implemented: findAllPostsByChatId");
	}

	public List<Post> findAllPostsByOwnerIdAndChatName(int ownerId,
			String chatName) {
		if(ownerId!=2)
			return null;
		return posts;
	}

	public Post findPostById(int id) {
		for(Post p : posts) {
			if(p.getId() == id)
				return p;
		}
		return null;
	}

	private String userName;
	private String chatName;
	private List<Post> posts;
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public void setChatName(String chatName) {
		this.chatName = chatName;
	}
	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}
}
