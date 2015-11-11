package de.swm.nis.logicaldecoding.dataaccess;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * ChangeSetFetcher is able to fetch data from PostgreSQL Replication Slots via SQL.
 * @author Schmidt.Sebastian2
 *
 */
@Repository
public class ChangeSetFetcher {
	
	@Autowired
	private JdbcTemplate template;
	
	public List<ChangeSetDAO> fetch(String slotname, int maxRows) {
		
		RowMapper<ChangeSetDAO> changeSetRowMapper = new RowMapper<ChangeSetDAO>() {

			@Override
			public ChangeSetDAO mapRow(ResultSet rs, int rowNum) throws SQLException {
				ChangeSetDAO changeset = new ChangeSetDAO();
				changeset.setData(rs.getString("data"));
				changeset.setLocation(rs.getString("location"));
				changeset.setTransactionId(rs.getLong("xid"));
				return changeset;
			}
		};
		
		//Parameter 1: replication Slot name
		//Parameter 2: upto_n_changes
		String sql = "SELECT * from pg_logical_slot_get_changes(?, NULL, ?)";
		List<ChangeSetDAO> changes = template.query(sql, new Object[]{slotname, maxRows}, changeSetRowMapper);
		return changes;
	}
	
	public long peek(String slotname) {
		String sql = "SELECT count(*) from pg_logical_slot_peek_changes(?, NULL, NULL)";
		Number number = template.queryForObject(sql,  new Object[]{slotname}, Long.class);
		return (number != null ? number.longValue() : 0);
		
	}
	
}
