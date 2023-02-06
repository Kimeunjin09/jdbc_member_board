package edu.kh.jdbc.main.model.dao;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import edu.kh.jdbc.member.vo.Member;

import static edu.kh.jdbc.common.JDBCTemplate.*;

public class MainDAO {
	
	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	private Properties prop = null;
	// Map<String, String> 제한, XML파일을 읽고 쓰는데 특화
	
	// 기본생성자, 서비스에서 호출할때 자동실행
	// DAO가 서비스에서 만들어졌을 때 기본적으로 해야할 일 작성
	public MainDAO() {
		
		try {
			prop = new Properties();// Properties 객체 생성
			
			prop.loadFromXML(new FileInputStream("main-query.xml"));
			// main-query.xml 파일의 내용을 읽어와 Properties 객체에 저장
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/** 로그인 DAO
	 * @param conn
	 * @param memberId
	 * @param memberPw
	 * @return loginMember
	 * @throws Exception
	 */
	public Member login(Connection conn, String memberId, String memberPw) throws Exception {
		
		// 1. 결과 저장용 변수 선언
		Member loginMember = null;
		
		try {
			// 2. SQL 얻어오기 (main-query.xml 파일에 작성된 SQL)
			String sql = prop.getProperty("login");
			
			// 3. PreparedStatement 객체 생성(?플레이스 홀더 사용때문에)
			pstmt = conn.prepareStatement(sql);
			
			// 4. ? 에 알맞은 값 대입
			pstmt.setString(1, memberId);
			pstmt.setString(2, memberPw);
			
			// 5. SQL(SELECT) 수행 결과(ResultSet)반환 받기
			rs = pstmt.executeQuery();
			
			// 6. 조회 결과가 있을 경우 컬럼값을 모두 얻어와
			//    Member 객체를 생성해서 loginMember에 대입
			//    조회결과 1개 =  아이디,패스워드 같은 회원은 1명
			
			if(rs.next()) {
				
				// String memberNo = rs.getInt("MEMBER_NO") 이렇게 쓰고 밑에 memberId 변수를
				// 집어넣지않고 밑에 처럼 바로 넣어도 됨
				
				loginMember = new Member(rs.getInt("MEMBER_NO"),
										 memberId,
										 rs.getString("MEMBER_NM"),
										 rs.getString("MEMBER_GENDER"),
										 rs.getString("ENROLL_DATE"));
			}
		} finally {
			// 7. 사용한 JDBC 객체 자원 반환
			close(rs);
			close(pstmt);
		}
		
		// 8. 조회 결과 반환
		return loginMember;
	}

	/** 아이디 중복검사 DAO
	 * @param conn
	 * @param memberId
	 * @return result
	 * @throws Exception
	 */
	public int idDupCheck(Connection conn, String memberId) throws Exception {
		int result = 0;
		
		try {
			String sql = prop.getProperty("idDupCheck");
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1,  memberId);
			
			rs = pstmt.executeQuery();
			// COUNT(*) 1 / 얻어온값이 int형 같아도 하나의 resultset이라서
			// 컬럼의 값을 얻어와야 하기 때문에 rs에 조회결과 저장
			
			if(rs.next()) {
				// result = rs.getInt("COUNT(*)");
				result = rs.getInt(1); // 컬럼 순서(1번컬럼), 여러개일때는 사용x
			}
			
		} finally {
			close(rs);
			close(pstmt);
		}
		return result;
	}

	/** 회원 가입 DAO
	 * @param conn
	 * @param member
	 * @return result
	 * @throws Exception
	 */
	public int signUp(Connection conn, Member member) throws Exception {
		int result = 0;
		
		try {
			
			String sql = prop.getProperty("singUp");
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, member.getMemberId());
			pstmt.setString(2, member.getMemberPw());
			pstmt.setString(3, member.getMemberName());
			pstmt.setString(4, member.getMemberGender());
			
			result = pstmt.executeUpdate();
			
		} finally {
			close(pstmt);
		}
		return result;
	}
}
