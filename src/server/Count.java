package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

import util.CountDouble;
import util.GetDate;
import util.Handle_Txt_Select;

public class Count implements Runnable {
	public static final int NEED_STATIC_SIZE = 100;//600
	public static final int NEED_DYNAMIC_SIZE =10;
	public static boolean has_threhold = false;
	public static ArrayList<ArrayList<Double>> th = new ArrayList<ArrayList<Double>>();//�洢600����ֵ
	public Socket socket;
	// ���߳��������Socket����Ӧ��������
	public static BufferedReader br = null;

	public Count(Socket socket){
		this.socket = socket;
	}

	public void run(){
		try {
			br = new BufferedReader(new InputStreamReader(socket.getInputStream(),
					"utf-8"));
			String content = null;
			content = null;

			// ����ѭ�����ϴ�Socket�ж�ȡ�ͻ��˷��͹���������
			while ((content = br.readLine())!=null) {
				System.out.println(content);
				Main.handleJsonString(content);
				Handle_Txt_Select.write2txt(GetDate.getTime(), content);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String count(ArrayList<ArrayList<Double>> list,int num){
		int len = list.get(0).size();
		int length = NEED_DYNAMIC_SIZE+NEED_STATIC_SIZE;
		if(len < NEED_STATIC_SIZE){
			return "����"+(len+1)+"/"+NEED_STATIC_SIZE+"������";
		}
		else if(len < length){
			if(!has_threhold){
				for(int i=0;i<num;i++){
					Double[] double_static = new Double[NEED_STATIC_SIZE];
					list.get(i).toArray(double_static);

					System.out.println("��̬���ݣ�"+Arrays.toString(double_static));
					ArrayList<Double> list_double_perth = new ArrayList<Double>(Arrays.asList(CountDouble.getMyupbound(double_static)));
					th.add(list_double_perth);
					System.out.println("��ֵΪ��"+th.toString());
				}
				has_threhold = true;
				return "�������ֵ";
			}
			return "����"+(length-len)+"������";
		}else{
			int door = 1;//����
			int sum = 0;
			for(int i=0;i<num;i++){
				int l = list.get(i).size();
				Double[] double_dynamic = new Double[NEED_DYNAMIC_SIZE];
				list.get(i).subList(l-NEED_DYNAMIC_SIZE, l).toArray(double_dynamic);
				System.out.println("double_dynamic:"+Arrays.toString(double_dynamic));
				Double[] doubles = new Double[th.get(i).size()];
				th.get(i).toArray(doubles);
				sum+=CountDouble.getDetection(doubles , double_dynamic);
			}
			if(sum<door)
				return "���Ϊ0";
			else
				return "���Ϊ1";
		}
	}
}
