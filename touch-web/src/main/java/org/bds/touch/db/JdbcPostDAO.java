package org.bds.touch.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.bds.touch.model.Post;
import org.bds.touch.model.Post.Direction;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

public class JdbcPostDAO extends SimpleJdbcDaoSupport implements PostDAO {

	private static final String UPDATE_LAST_MODIFIED = "update chat set last_modified = CURRENT_TIMESTAMP where id = ?";
	
	private static ParameterizedRowMapper<Post> mapper = new ParameterizedRowMapper<Post>() {
		public Post mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new Post(rs.getInt("id"), rs.getInt("chat_id"), Direction
					.getDirection(rs.getInt("direction")), rs
					.getString("post_text"));
		}
	};

	public Post createPost(int chatId, Direction direction, String text) {
		int updated = getSimpleJdbcTemplate()
				.update(
						"insert into post (chat_id, direction, post_text) values (?, ?, ?)",
						chatId, direction.getId(), text);
		if (updated < 1)
			throw new RuntimeException("failed to create post");
		int postId = getSimpleJdbcTemplate().queryForInt("call identity()");
		
		// update the last modified date of the chat
		getSimpleJdbcTemplate().update(UPDATE_LAST_MODIFIED, chatId);
		return new Post(postId, chatId, direction, text);
	}

	public void delete(int id) {
		int updated = getSimpleJdbcTemplate().update(
				"delete from post where id = ?", id);
		if (updated < 1)
			throw new RuntimeException("failed to delete post id=" + id);
	}

	public Post findPostById(int id) {
		return getSimpleJdbcTemplate().queryForObject("select id, chat_id, direction, post_text from post where id = ?", mapper, id);
	}
	
	public List<Post> findAllPosts() {
		return getSimpleJdbcTemplate().query(
				"select id, chat_id, direction, post_text from post order by created", mapper);
	}

	public List<Post> findAllPostsByChatId(int chatId) {
		return getSimpleJdbcTemplate().query(
				"select id, chat_id, direction, post_text from post where chat_id = ?", mapper, chatId);
	}

}
