package com.jundger.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * Title: UmbrellaStorage
 * Date: Create in 2018/3/30 23:51
 * Description:
 *
 * @author Jundger
 * @version 1.0
 */
public class CreateRandomCharacter {

	/**
	 * 获取随机生成的订单号
	 *
	 * @return 订单号
	 */
	public static String getOrderno() {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmssSSS");//只需改变输出格式

		return sdf.format(new Date());
	}

	/**
	 * 获取通用唯一识别码UUID(Universally Unique Identifier)
	 * @return UUID
	 */
	public static String getUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	/**
	 * 生成一定长度的随机字符串
	 * @param length 字符串长度
	 * @return 随机字符串
	 */
	public static String getRandomString(int length) {
		//定义一个字符串（A-Z，a-z，0-9）即62位；
		String str = "zxcvbnmlkjhgfdsaqwertyuiopQWERTYUIOPASDFGHJKLZXCVBNM1234567890";
		//由Random生成随机数
		Random random = new Random();
		StringBuilder sb = new StringBuilder();
		//长度为几就循环几次
		for (int i = 0; i < length; ++i) {
			//产生0-61的数字
			int number = random.nextInt(62);
			//将产生的数字通过length次承载到sb中
			sb.append(str.charAt(number));
		}
		//将承载的字符转换成字符串
		return sb.toString();
	}
}
