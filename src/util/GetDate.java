package util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GetDate {
	public static String getTime(){
		//得到long类型当前时间
		long l = System.currentTimeMillis();
		//new日期对象
		Date date = new Date(l);
		//转换提日期输出格式
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH_mm_ss");
		//System.out.println(dateFormat.format(date));
		return dateFormat.format(date);
	}
}
