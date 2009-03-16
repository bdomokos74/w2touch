package org.bds.touch.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.bds.touch.model.Chat;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

public class JdbcChatDAO extends SimpleJdbcDaoSupport implements ChatDAO {

	private static final String SELECT_ALL_CHAT = "select id, last_modified, chat_name, owner_id, party_name from chat";
	private static final String SELECT_CHAT_BY_ID = "select id, last_modified, chat_name, owner_id, party_name from chat where id = ?";
	private static final String SELECT_CHAT_BY_USERID = "select id, last_modified, chat_name, owner_id, party_name from chat where owner_id = ?";
	private static final String DELETE_CHAT_BY_ID = "delete from chat where id = ?";
	static final String CREATE_CHAT = "insert into chat (chat_name, owner_id, party_name) values (?, ?, ?)";

	static ParameterizedRowMapper<Chat> mapper = new  ParameterizedRowMapper<Chat>() {
		public Chat mapRow(ResultSet rs, int rowNum) throws SQLException {
			
			return new Chat(rs.getInt("id"), (Date)rs.getTimestamp("last_modified"), rs.getString("chat_name"), rs.getInt("owner_id"), rs.getString("party_name"));
		}
	};
	
	public Chat createChat(final String chatName, int ownerId, String otherName) {
		
		/*User user = getSimpleJdbcTemplate().queryForObject(JdbcUserDAO.SELECT_USER_BY_ID, JdbcUserDAO.mapper, ownerId);*/
		
		int updated = getSimpleJdbcTemplate().update(CREATE_CHAT, chatName,	ownerId, otherName);

		if (updated < 1)
			throw new RuntimeException("insert failed (Chat)");

		int idValue = getSimpleJdbcTemplate().queryForInt("call identity()");

		return new Chat(idValue, chatName, ownerId, otherName);
	}

	public void delete(int id) {
		int numDeleted = getSimpleJdbcTemplate().update(DELETE_CHAT_BY_ID, id);
		if (numDeleted < 1)
			throw new RuntimeException("delete failed (Chat)");
	}

	
	public List<Chat> findAllChat() {
		List<Chat> chatList = getSimpleJdbcTemplate().query(SELECT_ALL_CHAT, mapper);
		return chatList;
	}

	public List<Chat> findAllChatByUserId(int id) {
		List<Chat> chatList = getSimpleJdbcTemplate().query(SELECT_CHAT_BY_USERID, mapper, id);
		return chatList;
	}

	public Chat findChatById(int id) {
		Chat chat = getSimpleJdbcTemplate().queryForObject(SELECT_CHAT_BY_ID, mapper, id);
		return chat;
	}

}
