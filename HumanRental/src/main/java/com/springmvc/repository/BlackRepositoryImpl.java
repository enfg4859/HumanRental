package com.springmvc.repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.springmvc.domain.Black;
import com.springmvc.util.Utility;

@Repository
public class BlackRepositoryImpl implements BlackRepository {
	
	Utility util = new Utility();
	
	// JDBC
	JdbcTemplate template;
	
	@Autowired
	public void setJdbcTemplate(DataSource dataSource) {
		this.template = new JdbcTemplate(dataSource);
	}
	
	@Override
	public void registBlack(Black black) throws DataIntegrityViolationException {
		String SQL;
		
		black.setBlackId(util.createId("black"));
		SQL = "INSERT INTO blacklist VALUES(?, ?, ?)";
		template.update(SQL,  black.getBlackId(), black.getMemberId(), LocalDateTime.now(ZoneId.of("Asia/Seoul")));
	}
	
	@Override
	public void removeBlack(String blackId) {
		String SQL;
		
		SQL = "DELETE FROM blacklist WHERE blackId = ?";
		template.update(SQL, blackId);
	}
	
	@Override
	public List<Black> getBlackList(String sort,String sortTarget) {
		String SQL = "SELECT * FROM blacklist";
		
		if(!(sort.equals("none") || sort.equals("0"))) {
			SQL += util.sortSQL(sort, sortTarget);
		}
		
		try {
			return template.query(SQL, new RowMapper<Black>() {

				@Override
				public Black mapRow(ResultSet rs, int rowNum) throws SQLException {
					Black black = new Black();
					black.setBlackId(rs.getString(1));
					black.setMemberId(rs.getString(2));
					black.setRegistDate(rs.getTimestamp(3));
					return black;
				}
				
			});
		} catch (EmptyResultDataAccessException | IndexOutOfBoundsException e) {
			return null;
		}
	}
}
