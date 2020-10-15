package test03.objectCreateOrder;

public class MyChild extends MyParent {
	
	String name = "이순신";
	
	static String address = "서울시 서대문구 창천동";
	
	
	// ***** static 초기화 블럭 *****
	static {
		System.out.println("### 1. 자식클래스 MyChild의 static 초기화 블럭 실행됨(딱 1번만 초기화 됨)\n");
		address = "서울시  강남구 ";
	}
	
	// ***** instance 초기화 블럭 *****
	{
		System.out.println("### 3. 자식 클래스 MyChild 의  instance 초기화 블럭 실행됨 ###\n");
		name = "안세형";
	}
	
	public MyChild() {
		System.out.println("### 4. 자식 클래스 MyChild 의 default 생성자 실행됨 ###\n");
		name = "이소룡";
	}
	
}
