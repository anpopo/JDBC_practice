package test04.singletonPattern;

public class Main {
	public void a_method() {
		NoSingletonNumber aObj = new NoSingletonNumber();  // 객체 생성
		
		System.out.println("aObj : " + aObj);
		System.out.println("a_method() 에서 cnt값 호출 : " + aObj.getNextNumber());
	}
	
	public void b_method() {
		NoSingletonNumber bObj = new NoSingletonNumber();  // 객체 생성
		
		System.out.println("bObj : " + bObj);
		System.out.println("b_method() 에서 cnt값 호출 : " + bObj.getNextNumber());
	}
	
	public void c_method() {
		NoSingletonNumber cObj = new NoSingletonNumber();  // 객체 생성
		
		System.out.println("cObj : " + cObj);
		System.out.println("c_method() 에서 cnt값 호출 : " + cObj.getNextNumber());
	}

	///////////////////////////////////////////////////////////////////////////////
	
	
	public void d_method() {
		SingletonNumber dObj = SingletonNumber.getInstance();  // 객체 생성
		
		System.out.println("dObj : " + dObj);
		System.out.println("d_method() 에서 cnt값 호출 : " + dObj.getNextNumber());
	}
	
	public void e_method() {
		SingletonNumber eObj = SingletonNumber.getInstance();  // 객체 생성
		
		System.out.println("eObj : " + eObj);
		System.out.println("e_method() 에서 cnt값 호출 : " + eObj.getNextNumber());
	}
	
	public void f_method() {
		SingletonNumber fObj = SingletonNumber.getInstance();  // 객체 생성
		
		System.out.println("fObj : " + fObj);
		System.out.println("f_method() 에서 cnt값 호출 : " + fObj.getNextNumber());
	}
	
	///////////////////////////////////////////////////////////////////////////////
	public static void main(String[] args) {
		Main ma = new Main();
		ma.a_method();
		
		/*
		 * aObj : test04.singletonPattern.NoSingletonNumber@15db9742
		   a_method() 에서 cnt값 호출 : 1
		 */
		System.out.println();
		ma.b_method();
		/*
		 * bObj : test04.singletonPattern.NoSingletonNumber@6d06d69c
		   b_method() 에서 cnt값 호출 : 1
		 */
		System.out.println();
		ma.c_method();
		/*
		 * cObj : test04.singletonPattern.NoSingletonNumber@7852e922
		   c_method() 에서 cnt값 호출 : 1
		 */
		
		ma.d_method();
		/*
		 * dObj : test04.singletonPattern.SingletonNumber@4e25154f
   		   d_method() 에서 cnt값 호출 : 1
		 */
		System.out.println();
		
		ma.e_method();
		/*
		 * eObj : test04.singletonPattern.SingletonNumber@4e25154f
		   e_method() 에서 cnt값 호출 : 2
		 */
		System.out.println();
		
		ma.f_method();
		/*
		 * fObj : test04.singletonPattern.SingletonNumber@4e25154f
		   f_method() 에서 cnt값 호출 : 3
		 */
		System.out.println();
		
	}

}
