package util;

import java.io.File;
import java.io.RandomAccessFile;


public class Handle_Txt_Select {


	public static void write2txt(String fileName,String insertContent){

		//String fileName;
		String strFilePath = "d:\\text\\"+fileName+".txt";
		// 每次写入时，都换行写
		String strContent = insertContent + "\r\n";
		try {
			File file = new File(strFilePath);
			if (!file.exists()) {
				System.out.println("TestFile Create the file:" + strFilePath);
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
			RandomAccessFile raf = new RandomAccessFile(file, "rwd");
			raf.seek(file.length());
			raf.write(strContent.getBytes());
			raf.close();
		} catch (Exception e) {
			System.out.println("TestFile Error on write File:" + e);
		}

	}

}
