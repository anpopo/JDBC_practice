package test06.board;

import java.sql.*;
import java.util.*;

import test05.singleton.dbconnection.MyDBConnection;

public class MemberDAO implements InterMemberDAO {
	
	Connection conn;
	PreparedStatement ps;
	ResultSet rs;
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 자원반납 메서드
	private void close() {
		try {
			if (ps != null) ps.close();
			if (rs != null) rs.close();
		} catch (SQLException e) {}
	}
	
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// 회원 가입 하기
	@Override
	public int memberRegister(MemberDTO member, Scanner sc) {
		
		int result = 0;
		
		try {
			conn = MyDBConnection.getConn();
			
			String sql = "insert into jdbc_member(userseq, userid, passwd, name, mobile)\n"+
					"    values(userseq.nextval, ?, ?, ?, ?)";
			
			ps = conn.prepareStatement(sql);
			ps.setString(1, member.getUserid());
			ps.setString(2, member.getPasswd());
			ps.setString(3, member.getName());
			ps.setString(4, member.getMobile());
			
			result = ps.executeUpdate();
			String yn = null;
			if (result == 1) {
				do {
					System.out.print("회원가입을 정말 할꺼삼!?[Y/N] : ");
					yn = sc.nextLine();
					
					if("y".equalsIgnoreCase(yn)) {
						conn.commit();
					} else if ("n".equalsIgnoreCase(yn)) {
						System.out.println(">>>> 회원가입을 취소했삼!!\n");
						conn.rollback();
						result = 0;
					} else {
						System.out.println("y 혹은 n 을 입력해주삼!\n");
					}
					
				} while (!("y".equalsIgnoreCase(yn) || "n".equalsIgnoreCase(yn)));
			}
		
		} catch (SQLIntegrityConstraintViolationException e) {
			System.out.println("아이디 중복쓰\n");
		} catch (SQLException e) {
			
		} finally {
			close();
		}
		
		
		return result;
	}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// 로그인 하기
	
	@Override
	public MemberDTO login(Map<String, String> paraMap) {
		
		MemberDTO member = null;
		
		try {
			conn = MyDBConnection.getConn();
			
			String sql = "select userseq\n"+
					"    , userid\n"+
					"    , passwd\n"+
					"    , name\n"+
					"    , mobile\n"+
					"    , point\n"+
					"    , to_char(registerday, 'yyyy-mm-dd') as registerday\n"+
					"    , status\n"+
					"    from jdbc_member\n"+
					"    where userid = ? and passwd = ?";
			
			
			
			ps = conn.prepareStatement(sql);
			ps.setString(1,	paraMap.get("userid"));
			ps.setString(2, paraMap.get("passwd"));
			
			rs = ps.executeQuery();
			
			if(rs.next()) {
				member = new MemberDTO();
				member.setUserseq(rs.getInt("userseq"));
				member.setUserid(rs.getString("userid"));
				member.setPasswd(rs.getString("passwd"));
				member.setName(rs.getString("name"));
				member.setMobile(rs.getString("mobile"));
				member.setPoint(rs.getInt("point"));
				member.setRegisterday(rs.getString("registerday"));
				member.setStatus(rs.getInt("status"));
			}
			
		} catch (SQLException e) {
			
		} finally {
			close();
		}
		
		return member;
	}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 포인트 업데이트 메서드
	@Override
	public int updateMemberPoint(MemberDTO member) {
		
		int result = 0;
		
		try {
			conn = MyDBConnection.getConn();
			
		    String sql = "update jdbc_member set point = point + 10\n"+
		    		"    where userid = ?";
		    
		    ps = conn.prepareStatement(sql);
		    
		    ps.setString(1, member.getUserid());
		    
		    result = ps.executeUpdate();
		    
		    
		} catch (Exception e) {
			System.out.println("\n" + member.getName() + "님의 포인트는 현재 20이라서 30으로 증가가 불가합니다.\n");
		} finally {
			close();
		}
		
		return result;
	}
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	
}
