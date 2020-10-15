package test04.singletonPattern;

public class NoSingletonNumber {
	private int cnt = 0;
	
	public int getNextNumber() {
		return ++cnt;
	}
}
