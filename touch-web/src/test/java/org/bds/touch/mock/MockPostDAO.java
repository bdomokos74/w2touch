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
		throw new RuntimeException(
				"Not implemented: findAllPostsByOwnerIdAndChatName");
	}

	public Post findPostById(int id) {
		throw new RuntimeException("Not implemented: findPostById");
	}

}
