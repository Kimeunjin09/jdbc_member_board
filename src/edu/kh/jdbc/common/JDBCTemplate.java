package edu.kh.jdbc.common;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class JDBCTemplate {
	
	/* DB 연결(Connection 생성), 자동커밋 off
	 * 트랜젝션 제어, JDBC 객체 자원 반환(close)
	 * 
	 * 이러한 JDBC에서 반복 사용되는 코드를 모아둔 클래스
	 * 
	 * 모든 필드, 메서드가 static
	 * -> 어디서든지 클래스명.필드명 / 클래스명.메서드명으로 호출 가능 (별도 객체 생성 X)
	 * static 메모리에 있는 애들은 어디서든 접근가능
	 * */
	
	// 안에서 실행하고 끝(딱히 반환할게 없으니까) void
	
	private static Connection conn = null;
	
	
	/** DB 연결 정보를 담고있는 Connection 객체 생성 및 반환 메서드
	 * @return conn
	 */
	public static Connection getConnection() {
		try {
			
			// 현재 커넥션 객체가 없을 경우 -> 새 커넥션 객체를 생성
			if(conn == null || conn.isClosed()) {
				
				Properties prop = new Properties();
				// Map<String, String> 형태의 객체, XML 입출력 특화
				
				// driver.xml 파일 읽어오기
				// 재활용 안하고, 코드길이 늘어나는데 굳이 이렇게 쓸필요 X
				// FileInputStream fos = new FileInputStream();
				// prop.loadFromXML(fos); 와 밑에 한줄이 같음
				
				prop.loadFromXML(new FileInputStream("driver.xml"));
				// -> XML 파일에 작성된 내용이 Properties 객체에 모두 저장됨
				
				// XML에서 읽어온 값을 모두 String 변수에 저장
				String driver = prop.getProperty("driver");
				String url = prop.getProperty("url");
				String user = prop.getProperty("user");
				String password = prop.getProperty("password");
				
				// 커넥션 생성
				Class.forName(driver); // Oracle JDBC Driver 객체 메모리에 로드
				
				// DriverManager를 이용해 Connection 객체 생성
				conn = DriverManager.getConnection(url, user, password);
				
				// 개발자가 직접 트랜잭션을 제어할 수 있도록
				// 자동 커밋 비활성화
				conn.setAutoCommit(false);
			}
		} catch(Exception e) {
			System.out.println("[Connection 생성 중 예외 발생]");
			e.printStackTrace();
		}
		return conn;
	}
	
	
	/** Connection 객체 자원 반환 메서드
	 * @param conn
	 */
	public static void close(Connection conn) {
		
		try {
			// 전달받은 conn
			// 참조하는 Connection 객체가 있고
			// 그 Connection 객체가 close 상태가 아니라면 닫아라
			// !conn.isClosed() = 열려있다면
			if(conn != null && !conn.isClosed()) conn.close();
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	/** Statement(부모), PreparedStatement(자식) 객체 자원 반환 메서드
	 * 자바의 다형성(상속) 특징 때문에 pstmt는 따로 안만들어도 됨
	 * (다형성, 동적바인딩)
	 * 정적바인딩 : 컴파일 시 / 동적바인딩 : 런타임
	 * @param stmt
	 */
	public static void close(Statement stmt) { // 오버로딩 때문에 가능, 매개변수가 다르니까
		
		try {
			if(stmt != null && !stmt.isClosed()) stmt.close();
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	/** ResultSet 객체 자원 반환 메서드
	 * @param rs
	 */
	public static void close(ResultSet rs) {
		
		try {
			if(rs != null && !rs.isClosed()) rs.close();
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	/** 트랜잭션 Commit 메서드
	 * @param conn
	 */
	public static void commit(Connection conn) { // Connection 안에 있는 애들 커밋하니까
		try { 
			// 연결되어있는 Connection 객체일 경우에만 Commit
			if(conn != null && !conn.isClosed()) conn.commit();
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	/** 트랜잭션 Rollback 메서드
	 * @param conn
	 */
	public static void rollback(Connection conn) { 
		try { 
			// 연결되어있는 Connection 객체일 경우에만 rollback
			if(conn != null && !conn.isClosed()) conn.rollback();
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
}
