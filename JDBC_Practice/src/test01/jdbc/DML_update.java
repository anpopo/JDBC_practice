package test01.jdbc;

import java.sql.*;
import java.util.Scanner;

public class DML_update {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		Connection conn = null;

		PreparedStatement ps = null;

		try {

			// 1. Oracle Driver 로딩해 준다.

			Class.forName("oracle.jdbc.driver.OracleDriver");

			// 2. 연결하고자하는 서버를 선택한다.

			conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:xe", "hr", "cclass");

			// 3. 메세지를 작성해 준다!

			System.out.print("▷ 수정할 글번호 : ");
			String no = sc.nextLine();

			System.out.print("▷ 글쓴이 : ");
			String name = sc.nextLine();

			System.out.print("▷ 글내용 : ");
			String msg = sc.nextLine();

			String sql = " update jdbc_tbl_memo set name = ? "
						+ ", msg = ? " 
						+ " where no = ? ";
			
			// 4. 연결할 오라클 서버에 메세지를 전송해 준다.
			
			ps = conn.prepareStatement(sql);
			ps.setString(1, name);
			ps.setString(2, msg);
			ps.setString(3, no);
			
			// 5. 전송한 메세지를 실행시켜 준다.
			
			int n = ps.executeUpdate();
			
			
			String yn = null;

			if (n == 1) {
				do {
					System.out.print("▷ 정말로 입력 하시겠습니까?[Y/N] : ");
					yn = sc.nextLine();

					if ("y".equalsIgnoreCase(yn)) {
						conn.commit();
						System.out.println(">> 데이터 입력 성공");

					} else if ("n".equalsIgnoreCase(yn)) {
						conn.rollback();
						System.out.println(">> 데이터 입력 취소!!!!ㅋㅋ");

					} else {
						System.out.println("다시입력하세용~");

					}
				} while (!("y".equalsIgnoreCase(yn) || "n".equalsIgnoreCase(yn)));
			} else if (n == 0){ 
				System.out.println(">> 해당하는 행이 없습니당 ~~~~~ㅋㅋ");
			}else {
				System.out.println(">> 데이터 입력에 문제가 발생했어요...:** (");
			
			}
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// 불러왔던 자원들을 전부 닫아주어야 한다.
			try {
				if(ps != null) ps.close();
				if(conn != null) conn.close();
			} catch (SQLException e) {
				
			}
			sc.close();
		}

	}

}
