package test06.board;

import java.util.*;

public interface InterMemberDAO {
	
	// 회원가입 메서드
	int memberRegister(MemberDTO member, Scanner sc);
	
	// 로그인 메서드
	MemberDTO login(Map<String, String> paraMap);
	
	// 글쓰기 메서드 이후 멤버 포인트 업데이트
	int updateMemberPoint(MemberDTO member);
}
