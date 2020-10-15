package test01.jdbc;

import java.sql.*;
import java.util.Scanner;

public class DML_insert {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		Connection conn = null;
		
		PreparedStatement ps = null;
		
		
		try {
			
			// 1. oracle Driver 를 로딩시켜 준다.
			
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			// 2. 연결할 오라클 서버를 선택한다.
			
			conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:xe", "hr", "cclass");
			
			// 3. 보내야할 메세지를 작성 한다.
			// 여기서 중요한 개념 하나!!!!!!! ? 는 위치 홀더라고 부른다.
			
			System.out.print("▷ 글쓴이 : ");
			String name = sc.nextLine();
			
			System.out.print("▷ 내용 : ");
			String msg = sc.nextLine();
			
			
			String sql = "insert into jdbc_tbl_memo(no, name, msg)\n"+
					"    values (jdbc_seq_memo.nextval, ?, ?) ";
			
			// 4. 연결한 오라클 서버에 메세지를 전송해 준다.
			
			ps = conn.prepareStatement(sql);
			
			ps.setString(1, name);   // 위치 홀더에 각각의 문자열을 매핑해 주어야 한다. 첫번째 숫자는 sql문 내부의 첫번째 ?의 위치를 말한다.
									 // 첫번째 ?에 입력받은 name값을 넣어주라는 뜻!!
			ps.setString(2, msg);	 // 위치 홀더에 각각의 문자열을 매핑해 주어야 한다. 첫번째 숫자는 sql문 내부의 두번째 ?의 위치를 말한다.
									 // 첫번째 ?에 입력받은 msg값을 넣어주라는 뜻!!
			
			
			// 5. 메세지를 보냈으니 이제 실행을 시켜야 한다.
			// DQL문과 DML문의 실행 메서드는 다르다.
			// DQL - executeQuery(),    DML - executeUpdate()
			
			int n = ps.executeUpdate();  // 반환형이 int타입인 이유는 실행 된 후 성공한 행의 수를 돌려주기 때문이다.
			
			// DML문은 자동 커밋이 아니기 때문에 수동으로 커밋을 반드시 해주어야 한다.
			
			String yn = null;
			
			if(n == 1) {
				do {
					System.out.print("▷ 정말로 입력 하시겠습니까?[Y/N] : ");
					yn = sc.nextLine();
					
					if ("y".equalsIgnoreCase(yn)) {
						conn.commit();
						System.out.println(">> 데이터 입력 성공");
						
					} else if ("n".equalsIgnoreCase(yn)){
						conn.rollback();
						System.out.println(">> 데이터 입력 취소!!!!ㅋㅋ");
						
					} else {
						System.out.println("다시입력하세용~");
						
					}
				} while (!("y".equalsIgnoreCase(yn) || "n".equalsIgnoreCase(yn)));
			} else {
				System.out.println(">> 데이터 입력에 문제가 발생했어요...:(**");	
			}
			
			
			
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			
		} finally {
			try {
				if (ps != null) ps.close();
				if (conn != null) conn.close();
			} catch(SQLException e) {
				
			}
			sc.close();
		}
		
		
	}

}
