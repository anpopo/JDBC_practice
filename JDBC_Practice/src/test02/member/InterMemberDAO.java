package test02.member;

import java.util.Map;

public interface InterMemberDAO {
	
	// 회원가입 메서드
	int memberRegister(MemberDTO member);
	
	// 로그인 메서드
	MemberDTO login(Map<String, String> paraMap);
	
}
