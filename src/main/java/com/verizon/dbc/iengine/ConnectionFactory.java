package com.verizon.dbc.iengine;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.verizon.dbc.iengine.config.ConnectionProperties;


public class ConnectionFactory {
	// static reference to itself
	private static ConnectionFactory instance = null;
	private static Connection conn = null;
	
		// private constructor
	private ConnectionFactory(ConnectionProperties connProperty) {
		this.createConnection(connProperty);
	}

	public static ConnectionFactory getInstance(ConnectionProperties connProperty) {
		if (instance == null)
			instance = new ConnectionFactory(connProperty);
		return instance;
	}

	private Connection createConnection(ConnectionProperties connProperty) {
				
		String URL = connProperty.getUrl();
		String USER = connProperty.getUserName();
		String PASSWORD = connProperty.getPassword();
		String DRIVER_CLASS = connProperty.getDriver();
		try {
			Class.forName(DRIVER_CLASS);
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			System.out.println("ERROR: Unable to Connect to Database.");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return conn;
	}

	public static Connection getConnection(ConnectionProperties connProperty) throws SQLException {
		if(conn != null && !conn.isClosed())
			return conn;
		else{
			conn = instance.createConnection(connProperty);
			return conn;
		}
			
	}
}