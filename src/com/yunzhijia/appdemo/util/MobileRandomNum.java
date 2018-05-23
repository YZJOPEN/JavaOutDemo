package com.yunzhijia.appdemo.util;

import java.util.ArrayList;
import java.util.Random;

/**
 * 生成随机手机号码
 * @author Administrator
 *
 */
public class MobileRandomNum {

	public static int startMobileNo() {
		int[] mobileStart = { 139, 138, 137, 136, 135, 134, 159, 158, 157, 150, 151, 152, 188, 130, 131, 132, 156, 155,
				133, 153, 189, 180, 177, 176 };
		Random r = new Random();
		ArrayList<Integer> mobileList = new ArrayList<>();
		for (int i = 0; i < mobileStart.length; i++) {
			mobileList.add(mobileStart[i]);
		}
		return mobileList.get(r.nextInt(mobileList.size()));
	}

	public static String endMobileNo() {
		Random r = new Random();
		String temp = "";
		for (int i = 0; i < 8; i++) {
			temp += r.nextInt(10);
		}
		return temp;
	}
	
	public static String getRandomMobile() {
		return startMobileNo() + endMobileNo();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(getRandomMobile());
	}

}
