package com.jundger.common.util;

import java.security.MessageDigest;

/**
 * Title: CarServiceServer
 * Date: Create in 2018/5/1 21:15
 * Description:
 *
 * @author Jundger
 * @version 1.0
 */
public class MD5Util {

	/**
	 * MD5加密
	 * @param str 加密前字符串
	 * @return 加密后字符串
	 */
	public static String encode(String str) {
		String strDigest;
		try {
			// 此 MessageDigest 类为应用程序提供信息摘要算法的功能，必须用try,catch捕获
			MessageDigest md5 = MessageDigest.getInstance("MD5");

			byte[] data;
			data = md5.digest(str.getBytes("utf-8"));// 转换为MD5码
			strDigest = bytesToHexString(data);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		return strDigest;
	}

	/**
	 * 解析
	 * @param hexString 十六进制字符串
	 * @return 字节数组
	 */
	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	/**
	 * 将指定byte数组以16进制的形式打印到控制台
	 * @param b byte数组
	 */
	public static void printHexString(byte[] b) {
		for (byte aB : b) {
			String hex = Integer.toHexString(aB & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			System.out.print(hex.toUpperCase());
		}

	}

	private static byte charToByte(char c) {
		return (byte) "0123456789abcdef".indexOf(c);
	}

	private static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (byte aSrc : src) {
			int v = aSrc & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}
}
