package com.verizon.dbc.iengine.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;


public class EventDao {

	private static final String SQL_CREATE_EVENT_ID = "select SDWAN_EVENT_ID_SEQ.NEXTVAL FROM DUAL";
	
	private static final String SQL_CREATE_FILE_ID = "select SDWAN_FILE_ID_SEQ.NEXTVAL FROM DUAL";
	
	private static final String SQL_INSERT_FILE_DETAIL = "insert into SDWAN_FILE_DETAIL(FILE_ID, FILE_NAME, STATUS) values(?, ? ,'CREATED')";

	private static final String SQL_INSERT_EVENT_DATA = "insert into SDWAN_EVENT(EVENT_ID, FILE_ID, STATE, TYPE, ROUTER_DNS_ENTITY_NAME) values(?, ? ,'CREATED', ?, ?)";

	private static final String SQL_INSERT_EVENT_DETAILS = "insert into SDWAN_EVENT_DETAIL(EVENT_ID, NAME, VALUE) values(?, ?, ?) ";
	
	private static final String SQL_UPDATE_FILE_STATUS = "UPDATE SDWAN_FILE_DETAIL SET STATUS=?, DESCRIPTION=? WHERE FILE_ID=?";
	
	public int insertFileDetail(Connection conn, String fileName) {

		PreparedStatement stmt = null;
		ResultSet rs = null;
		int fileID = 0;

		try {

			stmt = conn.prepareStatement(SQL_CREATE_FILE_ID);
			rs = stmt.executeQuery();

			if (rs.next())
				fileID = rs.getInt(1);

			stmt.close();
			rs.close();

			stmt = conn.prepareStatement(SQL_INSERT_FILE_DETAIL);

			stmt.setInt(1, fileID);
			stmt.setString(2, fileName);


			stmt.executeQuery();
			
			conn.commit();

		} catch (Exception e) {
			System.out.print("Error in File detail" + e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return fileID;
	}

	public int insertEvent(Connection conn, int fileID, String type, String dnsName) {

		PreparedStatement stmt = null;
		ResultSet rs = null;
		int eventID = 0;

		try {

			stmt = conn.prepareStatement(SQL_CREATE_EVENT_ID);
			rs = stmt.executeQuery();

			if (rs.next())
				eventID = rs.getInt(1);

			stmt.close();
			rs.close();

			stmt = conn.prepareStatement(SQL_INSERT_EVENT_DATA);

			stmt.setInt(1, eventID);
			stmt.setInt(2, fileID);
			stmt.setString(3, type);
			stmt.setString(4, dnsName);

			stmt.executeQuery();
			
			conn.commit();

		} catch (Exception e) {
			System.out.print("Error in inserting event" + e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return eventID;
	}

	public void insertEventData(Connection conn, int eventID,
			Map<String, String> keyValueMap) {

		PreparedStatement stmt = null;
		//System.out.println("Inserting Event data");
		try {
			stmt = conn.prepareStatement(SQL_INSERT_EVENT_DETAILS);
			for (Map.Entry<String, String> keyValueEntry : keyValueMap
					.entrySet()) {
				stmt.setInt(1, eventID);
				stmt.setString(2, keyValueEntry.getKey());
				stmt.setString(3, keyValueEntry.getValue());
				stmt.addBatch();
			}

			stmt.executeBatch();

		} catch (SQLException e) {
			System.out.println("Error in insertEventData" + e);
		} finally {
			if (stmt != null)
				try {
					stmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	
	public void updateFileStatus(Connection conn, String status, String comment, int fileID) {
		PreparedStatement stmt = null;
		try {

			stmt = conn.prepareStatement(SQL_UPDATE_FILE_STATUS);
			
			stmt.setString(1, status);
			if(comment == null)
				stmt.setString(2, "");
			else
				stmt.setString(2, comment.substring(0, 1024));
			stmt.setInt(3, fileID);

			stmt.executeUpdate();

		} catch (Exception e) {
			System.out.print("Error in updating file status" + e);
		} finally {
			try {				
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
