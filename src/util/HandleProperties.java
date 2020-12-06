package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;



public class HandleProperties {
	public static Map<String, String> readAll(String pathOfProperties) throws FileNotFoundException, IOException {
		Properties pps = new Properties();
		File f=new File(pathOfProperties);
		pps.load(new FileInputStream(f));
		Map<String, String> map=new HashMap<String, String>();
		@SuppressWarnings("unchecked")
		Enumeration<String> enum1 = (Enumeration<String>) pps.propertyNames();//�õ������ļ�������
		while(enum1.hasMoreElements()) {
			String strKey = (String) enum1.nextElement();
			String strValue = pps.getProperty(strKey);
			map.put(strKey,strValue);
			System.out.println(strKey + "=" + strValue);
		}
		return map;
	}

}
