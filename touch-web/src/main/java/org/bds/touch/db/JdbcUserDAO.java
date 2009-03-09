package org.bds.touch.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.bds.touch.model.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class JdbcUserDAO extends JdbcDaoSupport implements UserDAO {

	private static final String SELECT_USERS = "select id, name, pw from USER";
	private static final String SELECT_USER_BY_NAME = "select id, name, pw from USER where name = ?";
	private static final String SELECT_USER_BY_ID = "select id, name, pw from USER where id = ?";
	protected static final String CREATE_USER = "insert into USER (name, pw) values (?, ?)";

	private final class UserRowMapper implements RowMapper {
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new User(rs.getInt("id"), rs.getString("name"), rs.getString("pw"));
		}
	}


	/* (non-Javadoc)
	 * @see org.bds.touch.db.UserDAO#findUsers()
	 */
	@SuppressWarnings("unchecked")
	public List<User> findUsers() {
		List<User> result = (List<User>)getJdbcTemplate().query(SELECT_USERS, new UserRowMapper());
		return result;
	}

	/* (non-Javadoc)
	 * @see org.bds.touch.db.UserDAO#findUserByName(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public User findUserByName(String name) {
		List<User> userList = (List<User>)getJdbcTemplate().query(SELECT_USER_BY_NAME, new Object[] {name}, new UserRowMapper());
		return userList.get(0);		
	}

	/* (non-Javadoc)
	 * @see org.bds.touch.db.UserDAO#findUserById(int)
	 */
	@SuppressWarnings("unchecked")
	public User findUserById(int id) {
		List<User> userList = (List<User>)getJdbcTemplate().query(SELECT_USER_BY_ID, new Object[] {id}, new UserRowMapper());
		return userList.get(0);
	}

	/* (non-Javadoc)
	 * @see org.bds.touch.db.UserDAO#createUser(java.lang.String, java.lang.String)
	 */
	public User createUser(final String name, final String password) {
		int updated = getJdbcTemplate().update( new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection conn)
					throws SQLException {
				PreparedStatement ps = conn.prepareStatement(CREATE_USER);
				ps.setString(1, name);
				ps.setString(2, password);
				return ps;
			}
		});
		if(updated<=0)
			throw new RuntimeException("insert failed (USER)");
		
		int idValue = (Integer)getJdbcTemplate().query("call identity()", new ResultSetExtractor() {
			public Object extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if(rs.next())
					return rs.getInt(1);
				else
					throw new RuntimeException("call identity() failed (USER)");
			}
		});
		return new User(idValue, name, password);
	}

	
}
