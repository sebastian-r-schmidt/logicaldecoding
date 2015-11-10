package de.swm.nis;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ChangeSetFetcher {
	
	@Autowired
	private JdbcTemplate template;
	
	public List<ChangeSet> fetch(String slotname, int maxRows) {
		
		RowMapper<ChangeSet> changeSetRowMapper = new RowMapper<ChangeSet>() {

			@Override
			public ChangeSet mapRow(ResultSet rs, int rowNum) throws SQLException {
				ChangeSet changeset = new ChangeSet();
				changeset.setData(rs.getString("data"));
				changeset.setLocation(rs.getString("location"));
				changeset.setTransactionId(rs.getLong("xid"));
				return changeset;
			}
		};
		
		// 1. Parameter: replication Slot name
		//2. Parameter: upto_n_changes
		String sql = "SELECT * from pg_logical_slot_get_changes(?, NULL, ?)";
		List<ChangeSet> changes = template.query(sql, new Object[]{slotname, maxRows}, changeSetRowMapper);
		return changes;
	}
	
	public long peek(String slotname) {
		String sql = "SELECT count(*) from pg_logical_slot_peek_changes(?, NULL, NULL)";
		Number number = template.queryForObject(sql,  new Object[]{slotname}, Long.class);
		return (number != null ? number.longValue() : 0);
		
	}
	
}
