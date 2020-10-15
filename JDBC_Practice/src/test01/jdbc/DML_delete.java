package test01.jdbc;

import java.sql.*;
import java.util.*;

public class DML_delete {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		Connection conn = null;
		
		PreparedStatement ps = null;
		
		
		try {
			
			// 1. Oracle Driver 를 연결한다.
			
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			
			// 2. 연결할 oracle 서버를 선택한다.
			
			conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:xe", "hr", "cclass");
			
			// 3. 메세지 작성
			
			System.out.print("▷ 삭제할 글번호 : ");
			String no = sc.nextLine();
			
			String sql = " delete from jdbc_tbl_memo\n" +
					   "    where no = ? ";
			
			// 4. 메세지를 보내준다.
			
			ps = conn.prepareStatement(sql);
			ps.setString(1, no);
			
			
			// 5. 전달한 메세지를 실행시켜 준다.
			
			int n = ps.executeUpdate(); // 리턴되는 타입이 int 인 이유는 실행 되어지고 난후 결과의 행의 수를 리턴해 준다.

			String yn = null;

			if (n == 1) {
				do {
					System.out.print("▷ 정말로 삭제 하시겠습니까?[Y/N] : ");
					yn = sc.nextLine();

					if ("y".equalsIgnoreCase(yn)) {
						conn.commit();
						System.out.println(">> 데이터 삭제 성공");

					} else if ("n".equalsIgnoreCase(yn)) {
						conn.rollback();
						System.out.println(">> 데이터 삭제 취소!!!!ㅋㅋ");

					} else {
						System.out.println("다시입력하세용~");

					}
				} while (!("y".equalsIgnoreCase(yn) || "n".equalsIgnoreCase(yn)));
			} else if (n == 0){ 
				System.out.println(">> 삭제할 글 번호 " + no + "은(는) 존재하지 않는 글번호 입니당.");
			}else {
				System.out.println(">> 데이터 입력에 문제가 발생했어요...:** (");
			}
			
			
			
			
			
			
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(ps != null) ps.close();
				if(conn != null) conn.close();
			} catch (SQLException e ) {
				
			}
			
			sc.close();
			System.out.println("쓰로그램 송료~~");
		}
		
		
	}

}
