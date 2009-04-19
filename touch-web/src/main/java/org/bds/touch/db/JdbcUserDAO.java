package org.bds.touch.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.bds.touch.model.User;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

public class JdbcUserDAO extends SimpleJdbcDaoSupport implements UserDAO {

	static final String DELETE_USER = "delete from TOUCHUSER where id = ?";
	static final String SELECT_USERS = "select id, name, pw from TOUCHUSER";
	static final String SELECT_USER_BY_NAME = "select id, name, pw from TOUCHUSER where name = ?";
	static final String SELECT_USER_BY_ID = "select id, name, pw from TOUCHUSER where id = ?";
	static final String CREATE_USER = "insert into TOUCHUSER (name, pw) values (?, ?)";

	static ParameterizedRowMapper<User> mapper = new  ParameterizedRowMapper<User>() {
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new User(rs.getInt("id"), rs.getString("name"), rs.getString("pw"));
		}
	};

	/* (non-Javadoc)
	 * @see org.bds.touch.db.UserDAO#findUsers()
	 */
	public List<User> findUsers() {
		List<User> result = (List<User>)getSimpleJdbcTemplate().query(SELECT_USERS, mapper);
		return result;
	}

	/* (non-Javadoc)
	 * @see org.bds.touch.db.UserDAO#findUserByName(java.lang.String)
	 */
	public User findUserByName(String name) {
		return getSimpleJdbcTemplate().queryForObject(SELECT_USER_BY_NAME, mapper, name);
	}

	/* (non-Javadoc)
	 * @see org.bds.touch.db.UserDAO#findUserById(int)
	 */
	public User findUserById(int id) {
		return getSimpleJdbcTemplate().queryForObject(SELECT_USER_BY_ID, mapper, id);
	}

	/* (non-Javadoc)
	 * @see org.bds.touch.db.UserDAO#createUser(java.lang.String, java.lang.String)
	 */
	public User createUser(final String name, final String password) {
		int updated = getSimpleJdbcTemplate().update(CREATE_USER, name, password);
		if(updated<=0)
			throw new RuntimeException("insert failed (USER)");
		getJdbcTemplate().update("commit");
		int idValue = getSimpleJdbcTemplate().queryForInt("call identity()");
		return new User(idValue, name, password);
	}

	public void deleteUserById(int id) {
		int updated = getSimpleJdbcTemplate().update(DELETE_USER, id);
		if(updated <=0)
			throw new RuntimeException("can't delete user, id="+id);
		
	}

	
}
