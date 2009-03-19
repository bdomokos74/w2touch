package org.bds.touch.db;

import java.util.List;

import org.bds.touch.model.Post;
import org.bds.touch.model.Post.Direction;

public interface PostDAO {
	Post createPost(int chatId, Direction direction, String text);
	List<Post> findAllPosts();
	Post findPostById(int id);
	List<Post> findAllPostsByChatId(int chatId);
	void delete(int id);
	List<Post> findAllPostsByOwnerIdAndChatName(int ownerId, String chatName);

}
