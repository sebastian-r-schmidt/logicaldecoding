/*
Copyright (c) 2016 Sebastian Schmidt

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
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
	
	public ChangeSetFetcher(JdbcTemplate template) {
		this.template = template;
	}

	public ChangeSetFetcher() {
	}
	
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
		String sql = "SELECT * from pg_logical_slot_get_changes(?, NULL, ?, 'include-timestamp', 'on')";
		List<ChangeSetDAO> changes = template.query(sql, new Object[]{slotname, maxRows}, changeSetRowMapper);
		return changes;
	}
	
	public long peek(String slotname) {
		//TODO this might be a VERY long running transaction in case of big updates (>10 Minutes), 
		//is this feasible? Are there better solutions? Is this really necessary?
		String sql = "SELECT count(*) from pg_logical_slot_peek_changes(?, NULL, NULL)";
		Number number = template.queryForObject(sql,  new Object[]{slotname}, Long.class);
		return (number != null ? number.longValue() : 0);
		
	}
	
}
