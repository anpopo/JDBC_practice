package test04.singletonPattern;

public class SingletonNumber {
	
	// Singleton 패턴에서 중요한 것은 다음의 세가지이다.
	
	// 첫번째
	// private 변수로 자기 자신의 클래스를 인스턴스를 가지도록 해야한다.
	// 접근제한자가 private이므로 외부 클래스에서는 직접적으로 접근이 불가하다.
	// 또한 static 변수로 지정하여 SingletonNumber 클래스를 사용할 때 객체 생성은 딱 1번만 생성되도록 해야한다.
	
	// static 변수 => 필드 영역에서 변수 설정을 했기 때문에 제일 먼저 호출
	
	private static SingletonNumber singleton = null;
	
	private int cnt;
	
	// static 초기화 블럭 => 두번째로 호출
	// static 초기화 블럭은 해당 클래스가 객체로 생성되기 전에 실행된다.
	// 딱 한번만 호출 되어 지고 그 후 새로운 인스턴스를 생성하더라도 더 이상의 호출은 없다.
	
	static {
		singleton = new SingletonNumber();
		System.out.println(">>> static 초기화 블럭 <<<");
	}
	
	// 두번째
	// 생성자의 접근제한자를 private으로 지정하여, 외부에서 절대로 인스턴스를 생성하지 못하도록 막아버린다.
	
	private SingletonNumber() {}
	
	// 세번째
	// static 메서드를 생성하여 외부에서 해당 클래스의 객체를 사용할 수 있도록 해 준다.
	
	public static SingletonNumber getInstance() {
		return singleton;
	}
	
	public int getNextNumber() {
		return ++cnt;
	}
	
}
