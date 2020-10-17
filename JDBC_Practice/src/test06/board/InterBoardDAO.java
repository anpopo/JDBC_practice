package test06.board;

import java.util.List;
import java.util.Map;

public interface InterBoardDAO {

	int write(BoardDTO bdto);  // 3.글쓰기 메서드

	List<BoardDTO> boardList();  // 1. 글 목록 보기

	BoardDTO viewContents(Map<String, String> paraMap);  // 2. 글 내용 보기

	void updateViewCount(Map<String, String> paraMap);  // 조회수 올리기

	List<BoardCommentDTO> commentList(Map<String, String> paraMap);  // 댓글내용보기

	int writeComment(BoardCommentDTO cmdto);  // 4. 댓글쓰기

	int updateBoard(Map<String, String> paraMap);  // 5.글 수정하기

	int deleteBoard(Map<String, String> paraMap);  // 6.글 삭제하기

	Map<String, Integer> weekCount();  // 7.최근 1주일간 게시글 작성건수

	List<Map<String, String>> countByDaily();  // 8.이번달 일자별 게시글 작성 건수

	List<MemberDTO> showMember();  // 10.전체 회원 정보 보기

	

}
