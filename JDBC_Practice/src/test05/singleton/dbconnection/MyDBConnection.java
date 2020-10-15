package test05.singleton.dbconnection;

import java.sql.*;

public class MyDBConnection {
	private static Connection conn = null;
	
	static {
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:xe", "myorauser", "cclass");
			conn.setAutoCommit(false);
			
			
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println("DB class를 찾지 못했삼!");
		}
	}
	
	private MyDBConnection() {}  // 생성하고자 하는 클래스의 기본 생성자를 private으로 만들어 외부에서 접근을 허용하지 않게 만든다.
	
	public static Connection getConn() {
		return conn;
	}
	
	public static void closeConnection() {
		
			try {
				if(conn != null) conn.close();
			} catch (SQLException e) { }
			
	}
}
