package test02.member;

import java.util.Scanner;

public class Member_Main {

	public static void main(String[] args) {
		
		
		Member_Ctrl mc = new Member_Ctrl();
		Scanner sc = new Scanner(System.in);
		
		mc.menu_Start(sc);
		sc.close();
		System.out.println("프로그램을 종료합니다~");
	}

}
