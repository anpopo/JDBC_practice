package test06.board;

import java.sql.*;
import java.util.*;

import javax.swing.SpringLayout.Constraints;

import test05.singleton.dbconnection.MyDBConnection;


public class BoardDAO implements InterBoardDAO {
	
	private Connection conn;
	private PreparedStatement ps;
	private ResultSet rs;
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 자원반납 메서드
	private void close() {
		try {
			if(ps != null) ps.close();
			if(rs != null) rs.close();
		} catch (Exception e) {}
	}
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// 글쓰기
	@Override
	public int write(BoardDTO bdto) {
		int result = 0;
		
		try {
			conn = MyDBConnection.getConn();
			
			String sql = " insert into jdbc_board(BOARDNO, FK_USERID, SUBJECT, CONTENTS, BOARDPASSWD)\n"+
					"    values(board_seq.nextval, ?, ?, ?, ?)";
			
			ps = conn.prepareStatement(sql);
			
			ps.setString(1, bdto.getFk_userid());
			ps.setString(2, bdto.getSubject());
			ps.setString(3, bdto.getContents());
			ps.setString(4, bdto.getBoardpasswd());
			
			
			result = ps.executeUpdate();

		} catch (Exception e) {
			
		} finally {
			close();
		}
		
		return result;
	}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// 글 목록 보기
	@Override
	public List<BoardDTO> boardList() {
		
		List<BoardDTO> boardList = new ArrayList<>();
		
		try {
			conn = MyDBConnection.getConn();
			
			String sql = "select b.boardno\n"+
					"    , b.subject\n"+
					"    , m.name\n"+
					"    , to_char(b.writeday, 'yyyy-mm-dd hh24:mi:ss') as writeday\n"+
					"    , b.viewcount\n"+
					"    , c.commentcnt\n"+
					"    from jdbc_board b join jdbc_member m\n"+
					"    on b.fk_userid = m.userid\n"+
					"    left join (\n"+
					"        select fk_boardno\n"+
					"        , count(*) as commentcnt\n"+
					"        from jdbc_comment\n"+
					"        group by fk_boardno\n"+
					"    ) c\n"+
					"    on b.boardno = c.fk_boardno\n"+
					"    order by 1 desc";
			
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			
			while( rs.next() ) {
				BoardDTO board = new BoardDTO();
				
				board.setBoardno(rs.getInt("boardno"));
				board.setSubject(rs.getString("subject"));
				
				MemberDTO member = new MemberDTO();
				member.setName(rs.getString("name"));
				
				board.setMember(member);
				board.setWriteday(rs.getString("writeday"));
				board.setViewcount(rs.getInt("viewcount"));
				board.setCommentcnt(rs.getInt("commentcnt"));
				
				boardList.add(board);
			}
			
		} catch (SQLException e) {
		} finally {
			close();
		}
		return boardList;
	}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public BoardDTO viewContents(Map<String, String> paraMap) {
		
		BoardDTO bdto = null;
		
		try {
			conn = MyDBConnection.getConn();
			
			String sql = "select contents\n"+
					"    , fk_userid\n"+
					"    , subject\n"+
					"    from jdbc_board";
			
			if(paraMap.get("boardPasswd") != null) {
				sql += " where boardno =? and boardpasswd = ? ";
				
				ps = conn.prepareStatement(sql);
				ps.setString(1, paraMap.get("boardNo"));
				ps.setNString(2, paraMap.get("boardPasswd"));
			} else  {
				sql += " where boardno = ? ";
				ps = conn.prepareStatement(sql);
				ps.setString(1, paraMap.get("boardNo"));
			}
			
			rs = ps.executeQuery();
			
			if( rs.next() ) {
				bdto = new BoardDTO();
				
				bdto.setContents(rs.getString("contents"));
				bdto.setFk_userid(rs.getString("fk_userid"));
				bdto.setSubject(rs.getString("subject"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return bdto;
	}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void updateViewCount(Map<String, String> paraMap) {
		
	    try {
	    	conn = MyDBConnection.getConn();
	    	
	    	String sql = "update jdbc_board set viewcount = viewcount + 1\n"+
	    			"    where boardno = ?";
	    	
			ps = conn.prepareStatement(sql);
			
			ps.setString(1, paraMap.get("boardNo"));
			
			ps.executeUpdate();
			
			conn.commit();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
	    
	    
		
	}
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public List<BoardCommentDTO> commentList(Map<String, String> paraMap) {
		
		List<BoardCommentDTO> commentList = null;
		
		
		
		try {
			conn = MyDBConnection.getConn();
			
			String sql = "select c.contents\n"+
					"    , m.name\n"+
					"    , to_char(c.writeday, 'yyyy-mm-dd hh24:mi:ss') as writeday\n"+
					"    from jdbc_comment c join jdbc_member m\n"+
					"    on c.fk_userid = m.userid\n"+
					"    where c.fk_boardno = ?\n"+
					"    order by c.commentno desc";
			
			ps = conn.prepareStatement(sql);
			
			ps.setString(1, paraMap.get("boardNo"));
			
			rs = ps.executeQuery();
			
			int cnt = 0;
			
			while( rs.next() ) {
				BoardCommentDTO cmdto = new BoardCommentDTO();
				cmdto.setContents(rs.getString(1));
				
				MemberDTO member = new MemberDTO();
				member.setName(rs.getString(2));
				cmdto.setMember(member);
				cmdto.setWriteday(rs.getString(3));
				
				cnt++;
				
				if(cnt == 1) {
					commentList = new ArrayList<>();
				}
				
				commentList.add(cmdto);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return commentList;
	}

	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public int writeComment(BoardCommentDTO cmdto) {
		
		int result = 0;
		
		try {
			conn = MyDBConnection.getConn();
			
			String sql = "insert into jdbc_comment(commentno, fk_boardno, fk_userid, contents)\n"+
					"    values(seq_comment.nextval, ?, ?, ?)";
			
			ps = conn.prepareStatement(sql);
			
			ps.setString(1, cmdto.getFk_boardno());
			ps.setString(2, cmdto.getFk_userid());
			ps.setNString(3,  cmdto.getContents());
			
			result = ps.executeUpdate();
			
			if(result == 1) {
				conn.commit();
			} else {
				conn.rollback();
			}
			
		} catch(SQLIntegrityConstraintViolationException e) {
			System.out.println("\n>>> 원글번호 " + cmdto.getFk_boardno() + "은 존재하지 않습니다~~ <<<\n");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close();
		}
		
		return result;
	}
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	
	
	
	
	
	
	
	
	
}
