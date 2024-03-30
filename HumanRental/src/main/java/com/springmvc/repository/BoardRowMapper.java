package com.springmvc.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.springmvc.domain.Board;
import com.springmvc.util.Utility;

public class BoardRowMapper implements RowMapper<Board> {

	Utility util = new Utility(); 	
	
	public Board mapRow(ResultSet rs, int rowNum) throws SQLException {
		Board board = new Board();
		board.setBoardId(rs.getString(1));
		board.setMemberId(rs.getString(2));
		board.setName(rs.getString(3));
		board.setTitle(rs.getString(4));
		board.setContent(rs.getString(5));
		board.setRegist_day(util.outputFormatting(rs.getTimestamp(6)));
		board.setHit(rs.getInt(7));
		return board;
	}
}
	