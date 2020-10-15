package test02.member;

import java.sql.*;
import java.util.Map;

public class MemberDAO implements InterMemberDAO{
	
	Connection conn;
	PreparedStatement ps;
	ResultSet rs;
	
	// 자원을 반납하는 메서드를 하나 만들자
	
	private void close() {
		try {
			if (rs != null) rs.close();
			if(ps != null) ps.close();
			if(conn != null) conn.close();
		} catch (SQLException e) {
			
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////
	// DB를 통한 회원가입 메서드
	@Override
	public int memberRegister(MemberDTO member) {
		
		int result = 0;
		
		try {
			// 1. 오라클 드라이버 로딩
			Class.forName("oracle.jdbc.driver.OracleDriver");
			// 2. 드라이버 선택
			conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:xe", "myorauser", "cclass");
			// 3. 메세지 작성
			String sql = " insert into jdbcmember(userseq, userid, passwd, name, mobile)\n"+
					"    values (userseq.nextval, ?, ?, ?, ?) ";
			// 4. 메시지 보낼준비
			
			ps = conn.prepareStatement(sql);
			
			ps.setString(1, member.getUserid());
			ps.setString(2, member.getPasswd());
			ps.setString(3, member.getName());
			ps.setString(4, member.getMobile());
			
			// 5. 실행시켜준다.
			
			result = ps.executeUpdate();
			
			
		} catch (ClassNotFoundException e) {
			System.out.println("ojdbc6.jar이 없습니다. 추가해주쇼ㅕ잉~");
		} catch (SQLIntegrityConstraintViolationException e) {  // 데이터베이스 제약조건에 의한 예외처리
			System.out.println("에러메세지 : " + e.getMessage());
			System.out.println("에러코드번호 : " + e.getErrorCode());
			System.out.println("아이디가 중복되었습니다. 새로운 아이디를 입력해주세요~~~");
		} catch (SQLException e) {
			
		} finally {
			close();
		}
		
		
		return result;
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	// DB를 통한 로그인 메서드
	@Override
	public MemberDTO login(Map<String, String> paraMap) {
		MemberDTO member = null;
		
		try {
			
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:xe", "myorauser", "cclass");
			
			String sql = "select userid\n"+
					"    , name\n"+
					"    from jdbcmember\n"+
					"    where userid = ? and passwd = ?";
			
			ps = conn.prepareStatement(sql);
			ps.setString(1, paraMap.get("userid"));
			ps.setString(2, paraMap.get("passwd"));
			
			rs = ps.executeQuery();
			
			if (rs.next()) {
				member = new MemberDTO();
				member.setUserid(rs.getString(1));
				member.setName(rs.getString("name"));
			}
			
			
		} catch (ClassNotFoundException e) {
			System.out.println("ojdbc6.jar울 확인하쇼잉~");
		} catch (SQLException e) {
			
		} finally {
			close();
		}
		
		
		return member;
	}
	

}
