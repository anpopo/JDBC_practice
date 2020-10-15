package test02.member;

import java.util.*;

public class Member_Ctrl {
	InterMemberDAO mdao = new MemberDAO();
	MemberDTO member = null;
	
	// 시작메뉴
	
	public void menu_Start(Scanner sc) {
		String sChoice = "";
		
		
		do {
			
			if (member == null) {
				System.out.println(
						">>>>> ---- 시작메뉴 ---- <<<<<\n" + "1.회원가입  2.로그인  3.프로그램종료\n" + "-----------------------------\n");
			} else {
				System.out.println(
						">>>>> ---- 시작메뉴[" + member.getName() + "님 로그인중....]---- <<<<<\n" + "1.회원가입  2.로그아웃  3.프로그램종료\n" + "-----------------------------\n");
			}
			System.out.print(">>> 메뉴 번호 선택 : ");
			sChoice = sc.nextLine();
			System.out.println();

			switch (sChoice) {
			case "1" :  // 회원가입
				memberRegister(sc);
				break;
			case "2" :  // 로그인
				if (member == null) login(sc);
				else {
					member = null;
					System.out.println("로그아웃 한당꼐요\n");
				}
				break;
			case "3" :  // 프로그램종료
				System.out.println("잘가소잉~\n");
				break;
				
				
			default:
				System.out.println("메뉴에 없는 번호여라. 다시 선택하소잉");
				break;
			}
			
			
		} while (!("3".equals(sChoice)));
		
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////
	private void memberRegister(Scanner sc) {
		System.out.println(">>>>> ---- 회원가입 ---- <<<<<");
		
		System.out.print("1.아이디 : ");
		String userid = sc.nextLine();
		System.out.print("2.암호 : ");
		String passwd = sc.nextLine();
		System.out.print("3.회원명 : ");
		String name = sc.nextLine();
		System.out.print("4.연락처(휴대폰) : ");
		String mobile = sc.nextLine();
		
		MemberDTO member = new MemberDTO();
		
		member.setUserid(userid);
		member.setPasswd(passwd);
		member.setName(name);
		member.setMobile(mobile);
		
		int n = mdao.memberRegister(member);  // 여기서 받은 정수값은 데이터 베이스에 입력이 성공한 사람의 수
		
		if(n == 1) {
			System.out.println("축하혀요~ 회원가입에 성공하셨지라\n");
		} else {
			System.out.println("회원가입에 실패했네 그려. 괜찮당가?\n");
		}
		
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private MemberDTO login(Scanner sc) {
		System.out.println(">>> --- 로그인 --- <<<\n");
		System.out.print(">>> 아이디 : ");
		String userid = sc.nextLine();
		System.out.print(">>> 암호 : ");
		String passwd = sc.nextLine();
		
		Map<String, String> paraMap = new HashMap<>();
		
		paraMap.put("userid", userid);
		paraMap.put("passwd", passwd);
		
		member = mdao.login(paraMap);
		
		if (member != null) {
			System.out.println("로그인에 성공하셨지라\n");
		} else {
			System.out.println("ㅋㅋㅋ로그인 실패지라. 다시 확인하고 해보셔잉\n");
		}
		
		return member;
	}


}
