package test06.board;

import java.sql.*;
import java.util.*;

import test05.singleton.dbconnection.MyDBConnection;

public class TotalController {
	
	InterMemberDAO mdao = new MemberDAO();
	InterBoardDAO bdao = new BoardDAO();
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 시작메뉴 만들기
	public void menu_Start(Scanner sc) {
		String sChoice = "";
		MemberDTO member = null;
		
		do {
			String login_logout = (member == null) ? "로그인" : "로그아웃";
			String loginName = (member == null)? "" : "[" + member.getName() + "님 로그인중......]";
			String menu = (member == null) ? "" : "4.게시판 메뉴가기";
			
			
			System.out.println("\n>>>> 시작메뉴 " + loginName + "<<<<<\n1.회원가입    2." + login_logout + "    3.프로그램종료    " + menu + "\n");
			System.out.print(">> 메뉴 번호 선택 : ");
			sChoice = sc.nextLine();
			
			
			switch (sChoice) {
			case "1":  // 회원가입
				memberRegister(sc);
				break;
			case "2":  // 로그인  / 로그아웃
				
				if("로그인".contentEquals(login_logout)) {
					member = login(sc);  // 로그인 시도하기
					
					if(member != null) {
						menu_Board(member, sc);
					} else {
						System.out.println("로그인에 실패하셨습니다~\n");
					}
				} else {
					member = null;
				}
				break;
			case "3":  // 프로그램 종료
				MyDBConnection.closeConnection();
				break;
			case "4":  // 메뉴가기
				if("로그아웃".contentEquals(login_logout)) {
					menu_Board(member, sc);
				} else {
					System.out.println("메뉴에 없는 번호입니다. 다시 선택하삼!\n");					
				}
				break;
				
			default:
				System.out.println("메뉴에 없는 번호입니다. 다시 선택하삼!\n");
				break;
			}
		} while (!("3".equalsIgnoreCase(sChoice)));
	}
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	


	// 회원가입 하기
	private void memberRegister(Scanner sc) {
		System.out.println(">>>> 회원가입에 오신걸 환영쓰 <<<<");
		System.out.print(">> 아이디 : ");
		String userid = sc.nextLine();
		System.out.print(">> 비밀번호 : ");
		String passwd = sc.nextLine();
		System.out.print(">> 회원명 : ");
		String name = sc.nextLine();
		System.out.print(">> 연락처(cellphone) : ");
		String mobile = sc.nextLine();
		
		// 전송객체인 DTO(Data Transfer Object)에 담아서 DAO를 통해 보낸다.
		
		MemberDTO member = new MemberDTO();
		member.setUserid(userid);
		member.setPasswd(passwd);
		member.setName(name);
		member.setMobile(mobile);
		
		// 데이터베이스와 접속할수 있는 객체임 MemberDAO(Database Access Object)이다.
		// DAO는 데이터베이스와 관련된 것들만 한다.
		// 재활용을 위해서 DAO를 사용한다.
		
		int n = mdao.memberRegister(member, sc);
		
		if (n == 1) {
			System.out.println(">>>> 회원가입을 축하하삼!\n");
		} else {
			System.out.println(">>>> 회원가입을 취소했삼!\n");
		}
		
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	
	// 로그인 하기
	private MemberDTO login(Scanner sc) {
		
		MemberDTO member = null;
		
		System.out.println(">>>> 로그인 <<<<");
		
		System.out.print(">> 아이디 : ");
		String userid = sc.nextLine();
		
		System.out.print(">> 비밀번호 : ");
		String passwd = sc.nextLine();
		
		// 전송객체로 DTO를 많이 사용하지만 Map계열도 많이 사용한다. 
		Map<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("userid", userid);
		paraMap.put("passwd", passwd);
		
		member = mdao.login(paraMap);
		
		
		if(member != null) {
			System.out.println(">>>> 로그인에 성공했삼!!\n");
		} else {
			System.out.println(">>>> 로그인에 실패했삼!!\n");
		}
		
		return member;
	}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// 로그인 후 메뉴 보여주기
	private void menu_Board(MemberDTO member, Scanner sc) {
		
		String adminMenu = ("admin".equals(member.getUserid())) ? "10.모든회원 정보조회" : "";
		String menuNo = "";
		do {
			
			System.out.println("\n----------------게시판메뉴[" + member.getName() + "님 로그인중 ...]-----------------\n"
					+ "1.글목록보기    2.글내용보기    3.글쓰기    4.댓글쓰기 \n" + "5.글수정하기    6.글삭제하기    7.최근1주일간 일자별 게시글 작성건수 \n"
					+ "8.이번달 일자별 게시글 작성건수    9.나가기    " + adminMenu + "\n"
					+ "------------------------------------------------------------------");

			System.out.print(">> 메뉴번호 선택 : ");
			menuNo = sc.nextLine();
			
			switch (menuNo) {
			case "1":  // 글목록보기
				boardList();
				break;
			case "2":  // 글내용보기
				viewContents(member, sc);
				break;
			case "3":  // 글쓰기
				int n = write(member, sc);
				System.out.println(n);
				if (n == 1) {
					System.out.println(">>> 글쓰기 성공~! <<<");
				} else if (n == -1) {
					System.out.println(">>> 글쓰기를 취소하셨삼 ~~! <<<");
				} else {
					System.out.println(">>> 데이터베이스 내부적 문제로 인해서 오휴가 발생했삼! <<<");
				}
				break;
				
			case "4":  // 댓글쓰기
				n = writeComment(member, sc);
				
				if (n == 1) System.out.println(">>>> 댓글 쓰기 성공했삼!\n");
				else System.out.println(">>>> 댓글 쓰기 실패했쌈!!\n");
				break;
			case "5":  // 글수정하기
				n = updateBoard(member, sc);
				
				// 수정할 글 번호가 글 목록에 존재하지 않는경우
				if (n == 0) {
					System.out.println(">>> 수정할 글번호가 글 목록에 존재하지 않습니다. <<<\n");
				} else if (n == 1) {
					System.out.println(">>> 다른 사용자의 글은 수정 불가 합니다!! <<<\n");
				} else if (n == 2) {
					System.out.println(">>> 글암호가 올바르지 않습니다 <<<\n");
				} else if (n == 3) {
					System.out.println(">>> 글 수정 실패!!! <<<\n");
				} else if (n == 4) {
					System.out.println(">>> 글  수정 취소 !!!! <<<\n");
				} else if (n == 5) {
					System.out.println(">>> 글 수정 성공!!!!!!!! <<<\n");
				}
				break;
			case "6":  // 글삭제하기
				
				break;
			case "7":  // 최근1주일간 일자별 게시글 작성건수
				
				break;
			case "8":  // 이번달 일자별 게시글 작성건수
				
				break;
			case "9":  // 나가기
				
				break;
			case "10":  // 모든회원 정보조회
				
				break;

			default:
				System.out.println("존재하지 않는 메뉴번호삼! 다시선택해주삼!!!!\n");
				break;
			}
			
		} while (!("9".equals(menuNo)));
	}



/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	


	// 3. 글쓰기
	private int write(MemberDTO member, Scanner sc) {
		
		int result = 0;
		
		System.out.println(">>>> 글쓰기 <<<<");
		System.out.println("1.작성자명 : " + member.getName());
		System.out.print("2.글제목 : ");
		String subject = sc.nextLine();
		System.out.print("3.글내용 : ");
		String contents = sc.nextLine();
		System.out.print("4.글암호 : ");
		String boardpasswd = sc.nextLine();
		
		BoardDTO bdto = new BoardDTO();
		
		bdto.setFk_userid(member.getUserid());
		bdto.setSubject(subject);
		bdto.setContents(contents);
		bdto.setBoardpasswd(boardpasswd);
		
		int n1 = bdao.write(bdto);
		int n2 = mdao.updateMemberPoint(member);
		
		Connection conn = MyDBConnection.getConn();
		
		if (n1 == 1 && n2 == 1) {
			do {
				System.out.print(">> 정말로 글쓰기를 하시겠습니까?[Y/N] => ");
				String yn = sc.nextLine();
				
				if("y".equalsIgnoreCase(yn)) {
					try {
						conn.commit();
						result = 1;
						break;
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
				} else if("n".equalsIgnoreCase(yn)) {
					try {
						conn.rollback();
						result = -1;
						break;
					} catch (SQLException e) {
					}
				} else {
					System.out.println("잘못된 입력을 하셨습니다. 다시입력을 해주세요~~~~\n");
				}
			} while (true);
		} else {
			try {
				conn.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		
		return result;
	}
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	// 1. 글목록 보기
	private void boardList() {
		List<BoardDTO> boardList = bdao.boardList();
		
		StringBuilder sb = new StringBuilder();
		
		if(boardList.size() > 0) {
			
			for (int i = 0; i < boardList.size(); i++) {
				sb.append(boardList.get(i).listInfo() + "\n");
			}
			
			System.out.println("\n--------------------------[게시글목록]---------------------------");
			System.out.println("글번호\t글제목\t\t작성자\t작성일자\t\t\t조회수");
			System.out.println("--------------------------------------------------------------");
			System.out.println(sb.toString());
			
		} else {
			System.out.println(">>>> 게시글이 존재하지 않삼!\n");
		}
		
		
	}
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	// 글 내용 보기
	private void viewContents(MemberDTO member, Scanner sc) {
		System.out.println(">>>> 글내용 보기 <<<<");
		
		System.out.print(">> 글번호 : ");
		String boardNo = sc.nextLine();
		
		Map<String, String> paraMap = new HashMap<String, String>();
		
		paraMap.put("boardNo", boardNo);
		
		BoardDTO bdto = bdao.viewContents(paraMap);
		
		if(bdto != null) {
			System.out.println("[글내용] " + bdto.getContents());
			
			if(!(member.getUserid().equals(bdto.getFk_userid()))) {
				bdao.updateViewCount(paraMap);
			}
			
			System.out.println("[댓글]\n-----------------------------------------------------------------------");
			List<BoardCommentDTO> commentList = bdao.commentList(paraMap);
			
			if(commentList != null) {
				System.out.println("내용\t\t\t작성자\t작성일자");
				System.out.println("-----------------------------------------------------------------------");

				StringBuilder sb = new StringBuilder();
				
				for(BoardCommentDTO comment: commentList) {
					sb.append(comment.commentInfo() + "\n");
				}
				
				System.out.println(sb.toString());
			} else {
				System.out.println(">>>> 댓글 내용이 없ㅅ삼!!!!");
			}
		} else {
			System.out.println(">> 글번호 " + boardNo + "은 글 목록에 존재하지 않습니다!");
		}
		
	}
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	private int writeComment(MemberDTO member, Scanner sc) {
		int result = 0;
		
		System.out.println("\n>>> 댓글쓰기 <<<");
		System.out.println("1. 작성자명 : " + member.getName());
		System.out.print("2. 원글의 글번호 : ");
		String boardno = sc.nextLine();
		
		String contents = null;
		
		do {
			System.out.print("3. 댓글내용 : ");
			contents = sc.nextLine();
			
			if(contents == null || contents.trim().isEmpty()) {
				System.out.println(">>>> 댓글내용은 필수로 입력해주삼!");
				
			} else {
				break;
			}
		} while (true);
		
		BoardCommentDTO cmdto = new BoardCommentDTO();
		
		cmdto.setFk_boardno(boardno);
		cmdto.setFk_userid(member.getUserid());
		cmdto.setContents(contents);
		
		result = bdao.writeComment(cmdto);
		
		return result;
	}
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	private int updateBoard(MemberDTO member, Scanner sc) {
		
		int result = 0;
		System.out.println("\n>>> 글 수정 하기 <<<\n");

		System.out.print(">> 수정할 글 번호 : ");
		String boardNo = sc.nextLine();
		
		Map<String, String> paraMap = new HashMap<String, String>();
		
		paraMap.put("boardNo", boardNo);
		
		BoardDTO bdto = bdao.viewContents(paraMap);
		
		if(bdto != null) {
			if(bdto.getFk_userid().equals(member.getUserid())) {
				System.out.print(">> 글암호 : ");
				String boardPasswd = sc.nextLine();

				paraMap.put("boardPasswd", boardPasswd);

				bdto = bdao.viewContents(paraMap);
				
				if (bdto != null) {

					System.out.println("-------------------------------------------------------");
					System.out.println("글제목 : " + bdto.getSubject());
					System.out.println("글내용 : " + bdto.getContents());
					System.out.println("-------------------------------------------------------");

					System.out.print(">> 글제목 [변경하지 않으려면 엔터] : ");
					String subject = sc.nextLine();
					
					subject = (subject != null && !(subject.trim().isEmpty())) ? subject : bdto.getSubject();
					
					System.out.print(">> 글내용 [변경하지 않으려면 엔터] : ");
					String contents = sc.nextLine();

					contents = (contents != null && !(contents.trim().isEmpty())) ? contents : bdto.getContents();
					
					paraMap.put("subject", subject);
					paraMap.put("contents", contents);
					
					int n = bdao.updateBoard(paraMap);
					
					
				} else {
					result = 2;
				}
			} else {
				result = 1;
			}
		}
		
		return result;
	}
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}


