package util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GetDate {
	public static String getTime(){
		//�õ�long���͵�ǰʱ��
		long l = System.currentTimeMillis();
		//new���ڶ���
		Date date = new Date(l);
		//ת�������������ʽ
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH_mm_ss");
		//System.out.println(dateFormat.format(date));
		return dateFormat.format(date);
	}
}
