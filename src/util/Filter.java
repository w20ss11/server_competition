package util;

import java.util.Arrays;

public class Filter {
	
	/**
	 * 
	 * @param dou 输入数组
	 * @param num 滤波个数 如3 5
	 * @return
	 */
	public static Double[] getMid(Double[] dou,int num) {
//		int num=3;
//		Double[] dou={2.23,6.2,10.0};
		int len=dou.length;
		Double res[]=new Double[len];
		int index_start=num-1;//2 4
		for(int i= 0;i<len;i++){
			if(i<index_start){//01 0123
				res[i]=dou[i];
			}else{//2 4
				Double[] temp=new Double[num];//3 5
				int uemp=i;
				for(int j=0;j<num;j++){
					temp[j]=dou[uemp];
					uemp--;
				}
				Arrays.sort(temp);
				res[i]=temp[num/2];
				System.out.println("temp:"+Arrays.toString(temp));
			}
		}
		System.out.println("res:"+Arrays.toString(res));
		return res;
	}
}
