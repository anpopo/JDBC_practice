package test06.board;

public class BoardCommentDTO {
	private int commentno;
	private String fk_boardno;
	private String fk_userid;
	private String contents;
	private String writeday;
	
	private MemberDTO member;
	
	public int getCommentno() {
		return commentno;
	}
	public void setCommentno(int commentno) {
		this.commentno = commentno;
	}
	public String getFk_boardno() {
		return fk_boardno;
	}
	public void setFk_boardno(String fk_boardno) {
		this.fk_boardno = fk_boardno;
	}
	public String getFk_userid() {
		return fk_userid;
	}
	public void setFk_userid(String fk_userid) {
		this.fk_userid = fk_userid;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public String getWriteday() {
		return writeday;
	}
	public void setWriteday(String writeday) {
		this.writeday = writeday;
	}
	public MemberDTO getMember() {
		return member;
	}
	public void setMember(MemberDTO member) {
		this.member = member;
	}
	public String commentInfo() {
		String info = contents + "\t" + member.getName() + "\t" + writeday;
		return info;
	}
	
	
}
