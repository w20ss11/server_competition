package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;

import util.HandleJsonString;
import util.HandleProperties;

public class Main {
	public static LoginWindow loginWindow;
	public static ImageIcon background0 = new ImageIcon("src\\img\\green.png");
	public static ImageIcon background1 = new ImageIcon("src\\img\\red.png");

	public static ArrayList<ArrayList<Double>> list = null;
	public static boolean has_staticData_container = false;
	public static Map<String, String> map_mac = null;
	public static int num = -1;
	
	public static ServerSocket tcpSocket = null;
	public static final int socket_port = 10046;


	public static List<Map<String, String>> list_map = null;

	public static void main(String[] args) throws IOException, InterruptedException {
		//建立窗口
		loginWindow=new LoginWindow();
		//检查静态数据
		readDataTxt();
		//接收数据
		receive();
		
	}
	public  static void receive() throws IOException {
		
		tcpSocket = new ServerSocket(socket_port);
		System.out.println("开启服务端");
		loginWindow.work_state.setText("开启UDP");
		while(true){
			//接收客户端的数据并计算 获取返回值
			Socket socket = tcpSocket.accept();
			System.out.println("收到信息");
			new Thread(new Count(socket)).start();
			}
	}
	static{
		list = new ArrayList<ArrayList<Double>>();
		try {
			map_mac = HandleProperties.readAll("src\\util\\apMacAddress.properties");
		} catch (Exception e) {
			e.printStackTrace();
		}
		num = Integer.parseInt(map_mac.get("num"));
		
		if(!has_staticData_container){
			for(int i=0;i<num;i++){
				ArrayList<Double> list_double = new ArrayList<Double>();
				list.add(list_double);
			}
			has_staticData_container = true;
		}
	}

	public static void readDataTxt(){
		try {
			File file=new File("D:\\text\\data.txt");
			if (file.exists()) {//没有data.txt时
				BufferedReader br = new BufferedReader(new FileReader(file));
				String str =null;
				List<Map<String, String>> list_temp = null;
				while((str=br.readLine()) != null){
					list_temp = HandleJsonString.readJsonString(str);
					add2list(list_temp);
				}
				br.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void add2list(List<Map<String, String>> list_temp){

		//ArrayList<ArrayList<Double>> list
		for(int i=0;i<num;i++){
			for(Map<String,String> map_temp:list_temp){
				if(map_mac.get("apMacAddress"+i).equals(map_temp.get("MacAddress"))){
					Double e = Double.parseDouble(map_temp.get("wifiStrength"));
					list.get(i).add(e);
				}
			}
		}
	}

	public static void handleJsonString(String jString) {
		list_map=HandleJsonString.readJsonString(jString);
		add2list(list_map);
		String reString = Count.count(list,num);
		loginWindow.work_state.append("\r\n"+reString);
		loginWindow.work_state.setCaretPosition(loginWindow.work_state.getText().length());
		
		if(reString.endsWith("0"))
			loginWindow.back.setIcon(background0);
		else if(reString.endsWith("1"))
			loginWindow.back.setIcon(background1);
	}

}
