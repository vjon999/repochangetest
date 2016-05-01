package com.prapps.rail.helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnector {

	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	static final String DB_URL = "jdbc:mysql://localhost/a7054067_apps";
	private Connection conn;
	private Statement stmt;
	
	public DBConnector() throws ClassNotFoundException, SQLException {
		init();
	}
	
	public void init() throws ClassNotFoundException, SQLException {
		Class.forName(JDBC_DRIVER);
		conn = DriverManager.getConnection(DB_URL,"root","root");
		stmt = conn.createStatement();
	}
	
	public void close() {
		try {
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public ResultSet query(String query) throws SQLException {
		return stmt.executeQuery(query);
 	}
}
