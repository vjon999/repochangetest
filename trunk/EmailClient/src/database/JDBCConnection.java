package database;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCConnection {
	
	static final String filename = "D:\\My Projects\\SCJP\\src\\scjpnew\\questionBank.mdb";
	String conString = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=";
    
	Connection conn;
	String url;
	String userName=null;
	String password=null;

	public JDBCConnection(String url) {
		this.url=url;
		conString+= url.trim() + ";DriverID=22;READONLY=true}";
	}
	public Connection createConnection() {		
		try {
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			 conn= DriverManager.getConnection(conString,"","");
		} catch (ClassNotFoundException e) {
			System.out.println("Class cast exception ");
			e.printStackTrace();
			return null;
		}
		catch(SQLException e2) {
			System.out.println("SQLException in createconnection method");
			e2.printStackTrace();
			return null;
		}
		System.out.println("Connection created successfully....");
		return conn;
	}
	public Connection createConnection(String userName,String password) {
		try {
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			 conn= DriverManager.getConnection(conString,userName,password);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		catch(SQLException e2) {
			e2.printStackTrace();
			return null;
		}
		return conn;
	}
	public void closeConnection() {
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
