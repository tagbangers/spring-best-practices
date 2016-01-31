package practice;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class MultiTenantTokenRepository extends JdbcDaoSupport {

	/** Default SQL for creating the database table to store the tokens */
	public static final String CREATE_TABLE_SQL =
			"create table persistent_logins (tenant_id varchar(64) not null, username varchar(64) not null, series varchar(64) primary key, " +
			"token varchar(64) not null, last_used timestamp not null)";
	/** The default SQL used by the <tt>getTokenBySeries</tt> query */
	public static final String DEF_TOKEN_BY_SERIES_SQL = "select tenant_id,username,series,token,last_used from persistent_logins where series = ?";
	/** The default SQL used by <tt>createNewToken</tt> */
	public static final String DEF_INSERT_TOKEN_SQL = "insert into persistent_logins (tenant_id, username, series, token, last_used) values(?,?,?,?,?)";
	/** The default SQL used by <tt>updateToken</tt> */
	public static final String DEF_UPDATE_TOKEN_SQL = "update persistent_logins set token = ?, last_used = ? where series = ?";
	/** The default SQL used by <tt>removeUserTokens</tt> */
	public static final String DEF_REMOVE_USER_TOKENS_SQL = "delete from persistent_logins where username = ? and tenant_id = ?";

	private String tokensBySeriesSql = DEF_TOKEN_BY_SERIES_SQL;
	private String insertTokenSql = DEF_INSERT_TOKEN_SQL;
	private String updateTokenSql = DEF_UPDATE_TOKEN_SQL;
	private String removeUserTokensSql = DEF_REMOVE_USER_TOKENS_SQL;
	private boolean createTableOnStartup;

	@Override
	protected void initDao() {
		if (createTableOnStartup) {
			getJdbcTemplate().execute(CREATE_TABLE_SQL);
		}
	}

	public void createNewToken(MultiTenantRememberMeToken token) {
		getJdbcTemplate().update(insertTokenSql, token.getTenantId(), token.getUsername(), token.getSeries(),
				token.getTokenValue(), token.getDate());
	}

	public void updateToken(String series, String tokenValue, Date lastUsed) {
		getJdbcTemplate().update(updateTokenSql, tokenValue, lastUsed, series);
	}

	public MultiTenantRememberMeToken getTokenForSeries(String seriesId) {
		try {
			return getJdbcTemplate().queryForObject(tokensBySeriesSql,
					new RowMapper<MultiTenantRememberMeToken>() {
						public MultiTenantRememberMeToken mapRow(ResultSet rs, int rowNum)
								throws SQLException {
							return new MultiTenantRememberMeToken(
									rs.getString(1), // tenant_id
									rs.getString(2), // username
									rs.getString(3), // series
									rs.getString(4), // token
									rs.getTimestamp(5)); // last_used
						}
					}, seriesId);
		}
		catch (EmptyResultDataAccessException zeroResults) {
			if (logger.isDebugEnabled()) {
				logger.debug("Querying token for series '" + seriesId
						+ "' returned no results.", zeroResults);
			}
		}
		catch (IncorrectResultSizeDataAccessException moreThanOne) {
			logger.error("Querying token for series '" + seriesId
					+ "' returned more than one value. Series" + " should be unique");
		}
		catch (DataAccessException e) {
			logger.error("Failed to load token for series " + seriesId, e);
		}

		return null;
	}

	public void removeUserTokens(String username, String tenantId) {
		getJdbcTemplate().update(removeUserTokensSql, username, tenantId);
	}

	public void setCreateTableOnStartup(boolean createTableOnStartup) {
		this.createTableOnStartup = createTableOnStartup;
	}
}
