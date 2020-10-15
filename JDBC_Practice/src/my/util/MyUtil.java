package my.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MyUtil {
	
	
	public static String getDay(int n) {
		
		Calendar cal = Calendar.getInstance();
		
		cal.add(Calendar.DATE, n);
		
		SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
		
		return simple.format(cal.getTime());
		
	}
	
}
