package test06.board;

public class BoardDTO {
	
	private int boardno;
	private String fk_userid;
	private String subject;
	private String contents;
	private String writeday;
	private int viewcount;
	private String boardpasswd;
	
	private MemberDTO member;
	private int commentcnt;
	
	
	
	public int getBoardno() {
		return boardno;
	}
	public void setBoardno(int boardno) {
		this.boardno = boardno;
	}
	public String getFk_userid() {
		return fk_userid;
	}
	public void setFk_userid(String fk_userid) {
		this.fk_userid = fk_userid;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
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
	public int getViewcount() {
		return viewcount;
	}
	public void setViewcount(int viewcount) {
		this.viewcount = viewcount;
	}
	public String getBoardpasswd() {
		return boardpasswd;
	}
	public void setBoardpasswd(String boardpasswd) {
		this.boardpasswd = boardpasswd;
	}
	public MemberDTO getMember() {
		return member;
	}
	public void setMember(MemberDTO member) {
		this.member = member;
	}
	public int getCommentcnt() {
		return commentcnt;
	}
	public void setCommentcnt(int commentcnt) {
		this.commentcnt = commentcnt;
	}
	
	
	public String listInfo() {
		
		if(this.subject != null && !(this.subject.isEmpty())) {
			subject = (this.subject.length() > 10) ? subject.substring(0,10) + ".." : subject;
		}
		if(this.commentcnt > 0) {
			subject = subject + "[" + this.commentcnt + "]";
		}
		String info = boardno + "\t" + subject + "\t" + member.getName() + "\t" + writeday + "\t" + viewcount;
		
		return info;
	}
	
	
	
}
