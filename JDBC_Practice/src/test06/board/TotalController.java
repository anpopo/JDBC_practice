package test06.board;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;

import my.util.MyUtil;
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
				n = deleteBoard(member, sc);
				
				if (n == 0) {
					System.out.println(">>> 삭제할 글번호가 글 목록에 존재하지 않습니다. <<<\n");
				} else if (n == 1) {
					System.out.println(">>> 다른 사용자의 글은 삭제가 불가 합니다!! <<<\n");
				} else if (n == 2) {
					System.out.println(">>> 글암호가 올바르지 않습니다 <<<\n");
				} else if (n == 3) {
					System.out.println(">>> 글 삭제 실패!!! <<<\n");
				} else if (n == 4) {
					System.out.println(">>> 글  삭제 취소 !!!! <<<\n");
				} else if (n == 5) {
					System.out.println(">>> 글 삭제 성공!!!!!!!! <<<\n");
				}
				break;
			case "7":  // 최근1주일간 일자별 게시글 작성건수
				weekCount();
				break;
			case "8":  // 이번달 일자별 게시글 작성건수
				countByDaily();
				break;
			case "9":  // 나가기
				
				break;
			case "10":  // 모든회원 정보조회
				if("admin".equals(member.getUserid())) {
					showMember();
				} else {
					System.out.println("존재하지 않는 메뉴번호삼! 다시선택해주삼!!!!\n");
				}
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
	// 글 수정하기 
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
					
					String yn = "";
					
					if (n == 1) {
						Connection conn = MyDBConnection.getConn();
						do {
							System.out.print(">> 정말로 수정하시겠습니까?[Y/N] > ");
							yn = sc.nextLine();
							try {
								if("y".equalsIgnoreCase(yn)) {  // 사용자가 확인버튼을 누르고 확인한 경우
									result = 5;
									conn.commit();
									break;
								} else if("n".equalsIgnoreCase(yn)) {  // 사용자가 직접 입력을 취소한 경우
									result = 4;
									conn.rollback();
									break;
								} else {
									System.out.println("입력이 옳지 않삼! 다시입력해주삼!\n");
								}
							} catch (SQLException e) {
								break;
							}
						} while (true);
					} else {  // 데이터베이스 내부적으로 문제가 생겨 글을 업데이트 한 결과값이 1이 나오지 않았을 경우
						result = 3;
					}
				} else {  // 글 암호가 올바르지 않을 경우 글을 조회할 수 없다.
					result = 2;
				}
			} else {  // 로그인한 사용자가 아닌 다른 사용자가 쓴 글을 수정하려고 하는경우 
				result = 1;
			}
		}
		return result;
	}
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	// 글 삭제하기
	private int deleteBoard(MemberDTO member, Scanner sc) {
		int result = 0;
		System.out.println("\n>>> 글 삭제 하기 <<<");
		System.out.print(">> 삭제할 글번호 : ");
		String boardNo = sc.nextLine();
		
		Map<String, String> paraMap = new HashMap<String,String>();
		paraMap.put("boardNo", boardNo);
		
		BoardDTO bdto = bdao.viewContents(paraMap);
		
		if(bdto != null) {  // 수정할 글 번호가 글 목록에 존재하는 경우라면
			if (bdto.getFk_userid().equals(member.getUserid())) {  // 현재 로그인한 사용자가 글을 쓴 사람의 아이디와 동일하다면
				System.out.print(">> 글암호 : ");
				String boardPasswd = sc.nextLine();
				
				paraMap.put("boardPasswd", boardPasswd);
				bdto = bdao.viewContents(paraMap);  // 글 암호가 맞는지 아닌지 확인 해 준다.
				
				if(bdto != null) {  // 글 암호가 올바른 경우 뺌
					System.out.println("-------------------------------------------------------");
					System.out.println("글제목 : " + bdto.getSubject());
					System.out.println("글내용 : " + bdto.getContents());
					System.out.println("-------------------------------------------------------");
					
					int n = bdao.deleteBoard(paraMap);
					
					String yn = "";
					
					if(n == 1) {
						Connection conn = MyDBConnection.getConn();
						
						do {
							System.out.print("\n>> 정말로 삭제하시겠습니까?[Y/N] > ");
							yn = sc.nextLine();
							
							try {
								if ("y".equalsIgnoreCase(yn)) { // 사용자가 확인을 한 경우
									result = 5;
									conn.commit();
									break;
								} else if ("n".equalsIgnoreCase(yn)) { // 사용자가 직접 입력을 취소한 경우
									result = 4;
									conn.rollback();
									break;
								} else { // 입력이 옳지 않을 경우 (y/n 이외의 입력)
									System.out.println("입력이 옳지 않습니다~!!@!@!@ 다시입력해주세용~\n");
								}
							} catch (SQLException e) {
								e.printStackTrace();
								break;
							}
						} while (true);
						
					} else {
						result = 3;
					}
					
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
	
	// 최근 1주일간 게시글 작성건수
	private void weekCount() {
		String col = "전체\t";
		for (int i = 0; i < 7; i++) {
			col += MyUtil.getDay(i - 6) + "\t";
		}
		
		System.out.println(
				"-------------------------------------------[최근 1주일간 일자별 게시글 작성 건수]-----------------------------------------------");
		System.out.println(col);
		System.out.println(
				"---------------------------------------------------------------------------------------------------------------------");
		
		
		// Map 은 DTO와 마찬가지로 DB에서 가져오는 값의 1개의 행으로 많이 사용되어진다.
		
		Map<String, Integer> resultMap = bdao.weekCount();
		
		String temp = resultMap.get("total") + "회\t\t"
				+ resultMap.get("prev6") + "회\t\t" 
				+ resultMap.get("prev5") + "회\t\t" 
				+ resultMap.get("prev4") + "회\t\t" 
				+ resultMap.get("prev3") + "회\t\t" 
				+ resultMap.get("prev2") + "회\t\t"
				+ resultMap.get("prev1") + "회\t\t" 
				+ resultMap.get("today")+ "회";
		
		System.out.println(temp);
	}
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	private void countByDaily() {
		Calendar currentDate = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월");
		
		String ym = dateFormat.format(currentDate.getTime());
		
		System.out.println(">>> [" + ym + " 일자별 게시글 작성건수] <<<");
		System.out.println("-----------------------------------------------------------------");
		System.out.println("작성일자\t\t\t작성건수");
		System.out.println("-----------------------------------------------------------------");
		
		List<Map<String, String>> resultList = bdao.countByDaily();
		
		StringBuilder sb = new StringBuilder();
		
		if(resultList.size() > 0) {
			for (Map<String, String> map : resultList) {  // writeday로 불러올 수 있는 이유는 각각의 map객체가 존재하기 때문!
				sb.append(map.get("writeday") + "\t\t" + map.get("writecount") + "\n");
			}
			System.out.println(sb.toString());
			
		} else {
			System.out.println(">>> d이번달에 작성된 게시물이 없삼!\n");
		}
		
		
	}
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	// 10.모든회원정보 조회
	private void showMember() {
		List<MemberDTO> memberList = bdao.showMember();
		
		if(memberList.size() > 0) {
			StringBuilder sb = new StringBuilder();
			
			for (MemberDTO member : memberList) {
				sb.append(member.getUserInfo() + "\n");		
			}
			
			System.out.println("\n--------------------------------[유저정보]---------------------------------");
			System.out.println("유저번호\t유저아이디\t이름\t전화번호\t\t포인트\t등록일자\t상태(1:회원,0:비회원)\t작성게시글수\t작성댓글수\t");
			System.out.println("---------------------------------------------------------------------------");
			
			System.out.println(sb.toString());
			
			
		} else {
			System.out.println("등록된 회원이 없삼...ㅠㅠ\n");
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}


