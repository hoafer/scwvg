package com.scwvg.gather.config;

import java.util.concurrent.BlockingQueue;

import com.scwvg.gather.util.ESClientUtil;

public class Global {
	
	/**����*/
	public static Context context = null;
	
	/**�Ƿ��˳�*/
	public static boolean isExit = false;
	
	/**Es�ͻ�������*/
	public static ESClientUtil esClientUtil = null;
	
	/**����*/
	public static BlockingQueue<String> queue = null;

}
