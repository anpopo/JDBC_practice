package test01.jdbc;

import java.sql.*;

public class DQL_select {

	public static void main(String[] args) {
		
		
		Connection conn = null;
		
		PreparedStatement ps = null;	
		
		ResultSet rs = null;
		
		try {
			
			// 1. 오라클 드라이버를 로딩시켜 준다.
			Class.forName("oracle.jdbc.driver.OracleDriver");  // 경로에 클래스가 없을 수도 있으니 예외처리를 반드시 해준다.
			
			
			// 2. 연결할 서버를 선택 해야 한다.
			conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:xe", "hr", "cclass");
			
			// 3. sql문을 작성한다.
			String sql = "select no\n"+
					"    , name\n"+
					"    , msg\n"+
					"    , to_char(writeday, 'yyyy-mm-dd hh24:mi:ss') as writeday\n"+
					"    from jdbc_tbl_memo\n"+
					"    order by 1 desc ";  // 데이터베이스에서 sql문을 직접 작성해서 우클릭 > 포함/표시를 선택하면 자바 코드로 변경이 가능하다.
			
			
			// 4. 연결한 오라클 서버에 sql문을 전달할 PreparedStatement를 만들어야 한다.
			ps = conn.prepareStatement(sql);
			
			// 5. 전달을 했으면 이제 실행을 시켜주어야 한다. DQL 문과 DML 문의 실행메서드가 다르다.
			//    현재는 DQL문(select가 있는 문장) 이기 때문에 executeQuery를 실행시켜준다.
			rs = ps.executeQuery();
			
			//////////////////////////////////////////////////////////////////////////////////////////////////
			
			StringBuilder sb = new StringBuilder();  // 실행한 후 select되어진 데이터베이스 내부의 정보를 출력해 주기위해
													 // StringBuilder 객체를 생성해 준다.
			
			// 불러온 select문의 결과를 한행마다 읽어드리면서 StringBuilder 객체에 저장시켜 준다.
			while(rs.next()) {
				int no = rs.getInt("no");
				String name = rs.getString(2);
				String msg = rs.getString(3);
				String writeday = rs.getNString("writeday");
				
				sb.append(no + "\t");
				sb.append(name + "\t");
				sb.append(msg + "\t");
				sb.append(writeday + "\n");
			}
			
			// 마지막으로 출력을 해준다.
			System.out.println("--------------------------------------------------------------------");
			System.out.println("글번호\t글쓴이\t글내용\t작성일자");
			System.out.println("--------------------------------------------------------------------");
			System.out.println(sb.toString());
			
		} catch (ClassNotFoundException | SQLException e) {
			
			
			e.printStackTrace();
		} finally {
			// 불러들였던 모든 자원들은 반드시 반납을 해주어야 한다. 반납 순서는 불러온 순서의 역순으로 해주면 된다. 
			
			try {
				if(rs != null) rs.close();
				if(ps != null) ps.close();
				if(conn != null) conn.close();
			} catch (SQLException e) {
				
			}
			
		}
		
	}

}
