package com.springmvc.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.springmvc.domain.Alarm;
import com.springmvc.domain.ReportType;
import com.springmvc.domain.Reservation;
import com.springmvc.util.Utility;

@Repository
public class AlarmRepositoryImpl implements AlarmRepository {
	
	Utility util = new Utility();
	
	// JDBC
	private JdbcTemplate template;

	@Autowired
	public void setJdbcTemplate(DataSource dataSource) {
		this.template = new JdbcTemplate(dataSource);
	}
	
	// 멘토 신청 알람
	@Override
	public void createMentoApplyAlarm(String sendMemberId, String registId) {
		Alarm alarm = new Alarm();
		alarm.setSendMemberId(sendMemberId);
		alarm.setReceiveMemberId("admin");
		alarm.setContent(sendMemberId + "님의 멘토 신청 알람입니다.");
		String SQL;
		
		try {
			SQL = "INSERT INTO alarm VALUES(?, ?, ?, ?, ?, ?)";
			template.update(SQL, util.createId("mentorApplyAlarm"),alarm.getSendMemberId(), alarm.getReceiveMemberId(), alarm.getDate(), alarm.getContent(), registId);
		} catch (EmptyResultDataAccessException | IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}
	
	// 경고 알람
	@Override
	public void createWarningAlarm(Map<String, Object> data) {
		Alarm alarm = new Alarm();
		alarm.setSendMemberId("admin");
		alarm.setReceiveMemberId((String) data.get("memberId"));
		String type = (String) data.get("type");
		
		String content = "";
		for(int i = 0; i < ReportType.TYPES.length; i++) {
			if(type.equals(ReportType.TYPES[i])) {
				content = ReportType.TYPES_KOR[i] + " 관련으로 경고가 발생했습니다. 대상 "; 
			}
		}
		
		content += (String) data.get("title") + " 게시글";
		
		alarm.setContent(content);
		
		String SQL;
		
		SQL = "INSERT INTO alarm VALUES(?, ?, ?, ?, ?)";
		template.update(SQL, util.createId("warningAlarm"), alarm.getSendMemberId(), alarm.getReceiveMemberId(), alarm.getDate(), alarm.getContent());
		
	}
	
	// 멘토 신청 결과 알람
	@Override
	public void createMentoApplyResultAlarm(String memberId, String result) {
		Alarm alarm = new Alarm();
		alarm.setSendMemberId("admin");
		alarm.setReceiveMemberId(memberId);
		alarm.setContent(memberId + "님의 멘토 신청이 " + result + " 되었습니다.");
		
		String SQL;
		
		try {
			SQL = "INSERT INTO alarm VALUES(?, ?, ?, ?, ?, ?)";
			template.update(SQL, util.createId("mentorApplyResultAlarm"), alarm.getSendMemberId(), alarm.getReceiveMemberId(), alarm.getDate(), alarm.getContent(), null);
		} catch (EmptyResultDataAccessException | IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}
	
	// 예약 신청 알람(재능 판매) 멘티 -> 멘토, 멘토 -> 멘티
	@Override
	public void createReservationApplyAlarm(Reservation reservation) {
		Alarm alarm = new Alarm();
		alarm.setSendMemberId(reservation.getApplicantMemberId());
		alarm.setReceiveMemberId(reservation.getMemberId());
		
		if(reservation.getBoardId().contains("sellingId")) {
			alarm.setContent(reservation.getApplicantMemberId() + "님이 '" + reservation.getTitle() + "' 에 멘티 신청하셨습니다.");
		} else if(reservation.getBoardId().contains("buyingId")) {
			alarm.setContent(reservation.getApplicantMemberId() + "님이 '" + reservation.getTitle() + "' 에 멘토 신청하셨습니다.");
		}
		
		alarm.setLinkId(reservation.getReservationId());
		
		String SQL = "INSERT INTO alarm VALUES(?, ?, ?, ?, ?, ?)";
		
		template.update(SQL, util.createId("ReservationApplyAlarm"), alarm.getSendMemberId(), alarm.getReceiveMemberId(), alarm.getDate(), alarm.getContent(), alarm.getLinkId());
	}
	
	// 예약 검토 알람(승인 및 거절) 
	@Override
	public void createReservationConfirmAlarm(String reservationId, String approval) {
		String SQL;
		
		SQL = "SELECT * FROM reservation WHERE reservationId = ?";
		Reservation reservation = template.queryForObject(SQL, new BeanPropertyRowMapper<Reservation>(Reservation.class), reservationId);
		
		//String targetId = reservation.getBoardId();
		
		Alarm alarm = new Alarm();
		
		alarm.setSendMemberId(reservation.getMemberId());
		alarm.setReceiveMemberId(reservation.getApplicantMemberId());
		
		if(approval.equals("yes")) {
			alarm.setContent("신청하신 재능 거래가 승인되었습니다. 관련 : " + reservation.getTitle());
		} else if(approval.equals("no")) {
			alarm.setContent("신청하신 재능 거래가 거부되었습니다. 관련 : " + reservation.getTitle());
		} else if(approval.equals("rentalyes")) {
			alarm.setContent("신청하신 재능 거래가 정상적으로 종료되었습니다. 관련 : " + reservation.getTitle());
		} else if(approval.equals("rentalno")) {
			alarm.setContent("신청하신 재능 거래가 실패처리 되었습니다. 관련 : " + reservation.getTitle());
		}
		
		alarm.setLinkId(reservationId);

		SQL = "INSERT INTO alarm VALUES(?, ?, ?, ?, ?, ?)";
		template.update(SQL, util.createId("ReservationConfirmAlarm"), alarm.getSendMemberId(), alarm.getReceiveMemberId(), alarm.getDate(), alarm.getContent(), alarm.getLinkId());
	}

	// 알람 목록
	@Override
	public List<Alarm> selectAlarm(String memberId) {
		String SQL;
		
		List<Alarm> alarmList = null;
		
		try {
			SQL = "SELECT * FROM alarm WHERE receiveMemberId = ?";
			alarmList = template.query(SQL, new RowMapper<Alarm>() {

				@Override
				public Alarm mapRow(ResultSet rs, int rowNum) throws SQLException {
					Alarm alarm = new Alarm();
					alarm.setAlarmId(rs.getString(1));
					alarm.setSendMemberId(rs.getString(2));
					alarm.setReceiveMemberId(rs.getString(3));
//					alarm.setDate(new java.sql.Timestamp(rs.getDate(3).getTime()).toLocalDateTime());
					alarm.setDate(util.outputFormatting(rs.getTimestamp(4)));
					alarm.setContent(rs.getString(5));
					alarm.setLinkId(rs.getString(6));
					return alarm;
				}
			}, memberId);
		} catch(EmptyResultDataAccessException | IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
		
		return alarmList;
	}
	
	// 알람 삭제
	@Override
	public void deleteAlarm(String memberId, String alarmId) {
		String SQL;
		
		try {
			SQL = "DELETE FROM alarm WHERE receiveMemberId = ? and alarmId = ?";
			template.update(SQL, memberId, alarmId);
		} catch(EmptyResultDataAccessException | IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}
}
