package test01.jdbc;

import java.sql.*;
import java.util.*;

public class DQL_select_where {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			// 1. 오라클 드라이버를 로딩해 준다.
			Class.forName("oracle.jdbc.driver.OracleDriver");

			// 2. 연결할 오라클 드라이버를 선택한다.

			conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:xe", "hr", "cclass");

			// 3. 메세지를 적는다.
			String sql = " select no, name, msg, to_char(writeday, 'yyyy-mm-dd hh24:mi:ss') as writeday "
					+ " from jdbc_tbl_memo " + " order by no desc ";

			ps = conn.prepareStatement(sql);

			rs = ps.executeQuery();

			StringBuilder sb = new StringBuilder();

			while (rs.next()) {

				int no = rs.getInt(1);
				String name = rs.getString(2);
				String msg = rs.getString(3);
				String writeday = rs.getString("WRITEDAY");

				sb.append(no + "\t");
				sb.append(name + "\t");
				sb.append(msg + "\t");
				sb.append(writeday + "\n");
			} // -------------------------------------------------- end of while

			System.out.println("--------------------------------------------------------------------");
			System.out.println("글번호\t글쓴이\t글내용\t작성일자");
			System.out.println("--------------------------------------------------------------------");
			System.out.println(sb.toString());

			////////////////////////////////////////////////////////////////////////////////////////////
			// 겨기까지 일반 DQL문 !

			sb = new StringBuilder();
			sb.append("--------- 조회할대상쓰 -----------\n");
			sb.append("1.글번호	2.글쓴이	3.글내용	4.종료\n");
			sb.append("-------------------------------\n");
			String menu = sb.toString();
			String menuNo = null;
			String search = null;

			do {
				System.out.println(menu);
				System.out.print("▷ 선택번호 : ");
				menuNo = sc.nextLine();

				String colName = "";

				switch (menuNo) {
				case "1":
					colName = "no";
					break;
				case "2":
					colName = "name";
					break;
				case "3":
					colName = "msg";
					break;
				case "4":
					System.out.println("프로그램 종료~");
					break;
				}

				if ("1".equals(menuNo) || "2".equals(menuNo) || "3".equals(menuNo)) {
					System.out.print("▷ 검색어 : ");
					search = sc.nextLine();

					sql = " select no, name, msg, to_char(writeday, 'yyyy-mm-dd hh24:mi:ss') as writeday "
							+ " from jdbc_tbl_memo ";

					if (!"3".equals(menuNo)) { // 1번 혹은 2번

						sql += " where " + colName + " = ? ";

					} else { // 글 내용으로 검색시

						sql += " where " + colName + " like '%' || ? || '%' ";

					}
					sql += " order by no desc ";

					ps.close();
					ps = conn.prepareStatement(sql);
					ps.setString(1, search);
					rs = ps.executeQuery();

					sb.setLength(0);

					while (rs.next()) {

						int no = rs.getInt(1);
						String name = rs.getString(2);
						String msg = rs.getString(3);
						String writeday = rs.getString("WRITEDAY");

						sb.append(no + "\t");
						sb.append(name + "\t");
						sb.append(msg + "\t");
						sb.append(writeday + "\n");
					} // -------------------------------------------------- end of while

					System.out.println("--------------------------------------------------------------------");
					System.out.println("글번호\t글쓴이\t글내용\t작성일자");
					System.out.println("--------------------------------------------------------------------");
					System.out.println(sb.toString());

				}

			} while (!("4".equals(menuNo)));

		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(ps != null) ps.close();
				if(conn != null) conn.close();
			} catch (SQLException e) {
				
			}
			sc.close();
			
		}

	}

}
