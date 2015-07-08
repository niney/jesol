package com.coupang.webapp007.niney.util;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;

/**
 * 리소스 파일을 관리하는 클래스
 * 
 */
public class Resourcer {
	private static ResourceBundle bundle;
	static {
		bundle = ResourceBundle.getBundle("com.coupang.webapp007.niney.Config");
	}

	/**
	 * 키값으로 리소스의 Data를 가져온다.
	 * @param key
	 * @return
	 */
	public static String getString(String key) {
		try {
			return bundle.getString(key);
		}
		catch (Exception e) {
			return null;
		}
	}
	
	public static List<String> getPropertyList() {
		Enumeration<String> keys = bundle.getKeys();
		List<String> list = new ArrayList<String>();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			list.add(String.format("%s=%s", key, bundle.getString(key)));
		}
		return list;
	}
	

}
