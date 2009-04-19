package org.bds.touch.db;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.bds.touch.model.Chat;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

public class JdbcChatDAO extends SimpleJdbcDaoSupport implements ChatDAO {

	private static final String FIND_ALL_CHAT = "select id, last_modified, chat_name, owner_id, party_name from CHAT";
	private static final String FIND_CHAT_BY_NAME = "select id, last_modified, chat_name, owner_id, party_name from CHAT, TOUCHUSER where owner_id = TOUCHUSER.id and TOUCHUSER.name= ? and chat_name = ?";
	private static final String FIND_CHAT_BY_ID = "select id, last_modified, chat_name, owner_id, party_name from CHAT where id = ?";
	private static final String FINDALL_CHAT_BY_USERID = "select id, last_modified, chat_name, owner_id, party_name from CHAT where owner_id = ?";
	private static final String FINDALL_CHAT_BY_USERNAME = "select id, last_modified, chat_name, owner_id, party_name from CHAT, TOUCHUSER where owner_id = TOUCHUSER.id and TOUCHUSER.name = ?";
	private static final String DELETE_CHAT_BY_ID = "delete from CHAT where id = ?";
	static final String CREATE_CHAT = "insert into CHAT (chat_name, owner_id, party_name) values (?, ?, ?)";

	static ParameterizedRowMapper<Chat> mapper = new  ParameterizedRowMapper<Chat>() {
		public Chat mapRow(ResultSet rs, int rowNum) throws SQLException {
			
			return new Chat(rs.getInt("id"), (Date)rs.getTimestamp("last_modified"), rs.getString("chat_name"), rs.getInt("owner_id"), rs.getString("party_name"));
		}
	};
	
	public Chat createChat(final String chatName, int ownerId, String otherName) {
		
		/*User user = getSimpleJdbcTemplate().queryForObject(JdbcUserDAO.SELECT_USER_BY_ID, JdbcUserDAO.mapper, ownerId);*/
		
		int updated = getSimpleJdbcTemplate().update(CREATE_CHAT, chatName, ownerId, otherName);

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
		List<Chat> chatList = getSimpleJdbcTemplate().query(FIND_ALL_CHAT, mapper);
		return chatList;
	}

	public List<Chat> findAllChatByUserId(int id) {
		List<Chat> chatList = getSimpleJdbcTemplate().query(FINDALL_CHAT_BY_USERID, mapper, id);
		return chatList;
	}
	public List<Chat> findAllChatByUserName(String userName) {
		List<Chat> chatList = getSimpleJdbcTemplate().query(FINDALL_CHAT_BY_USERNAME, mapper, userName);
		return chatList;
	}

	public Chat findChatById(int id) {
		Chat chat = getSimpleJdbcTemplate().queryForObject(FIND_CHAT_BY_ID, mapper, id);
		return chat;
	}

	public Chat findChatByName(String userName, String chatName) {
		List<Chat> chats = getSimpleJdbcTemplate().query(FIND_CHAT_BY_NAME, mapper, userName, chatName);
		if(chats==null||chats.size()==0)
			return null;
		else
			return chats.get(0);
	}

}
