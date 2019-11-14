package com.tedu.common.util;

import org.apache.commons.lang3.RandomStringUtils;

public class StringUtils {
	
	/*public static String getRandomNum(){
		
	}*/

	public static void main(String[] args) {
		for(int i=0; i<100; i++){
			System.out.println(RandomStringUtils.random(4, "0123456789"));
		}
	}
	
}
