package com.jundger.work.util;

import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

import static com.jundger.work.constant.Consts.EMAIL_FORGET_PSW;
import static com.jundger.work.constant.Consts.EMAIL_REGISTER;

/**
 * Title: CarServiceServer
 * Date: Create in 2018/5/1 18:43
 * Description:
 *
 * @author Jundger
 * @version 1.0
 */
public class JavaEmailSender {

	private static final String EMAIL_HOST = "smtp.qq.com";
	private static final String FROM_EMAIL = "jundger@qq.com";
	private static final String SMTP_AUTH_PASSWORD = "frdihamfvznmjbfd";

	public static final String EMAIL_TITLE = "CarService verification Code";

	public static void sendEmail(String toEmailAddress, String verifCode, String type) throws Exception {

		Properties props = new Properties();

		// 开启debug调试
		props.setProperty("mail.debug", "true");
		// 发送服务器需要身份验证
		props.setProperty("mail.smtp.auth", "true");
		// 设置邮件服务器主机名
		props.setProperty("mail.host", "smtp.qq.com");
		// 发送邮件协议名称
		props.setProperty("mail.transport.protocol", "smtp");

		/**SSL认证，注意腾讯邮箱是基于SSL加密的，所有需要开启才可以使用**/
		MailSSLSocketFactory sf = new MailSSLSocketFactory();
		sf.setTrustAllHosts(true);
		props.put("mail.smtp.ssl.enable", "true");
		props.put("mail.smtp.ssl.socketFactory", sf);

		//创建会话
		Session session = Session.getInstance(props);

		//发送的消息，基于观察者模式进行设计的
		Message msg = new MimeMessage(session);
		msg.setSubject(EMAIL_TITLE);

		String content;
		if (EMAIL_REGISTER.equals(type)) {
			content = "您好，" + toEmailAddress + "！\n" +
					"您的验证码为：" + verifCode +
					"\n感谢您的使用，祝您生活愉快！\n" +
					"\n时间：" + new Date();
		} else {
			content = "您好，" + toEmailAddress + "！\n" +
					"您正在申请忘记密码，如非您本人操作请忽略此邮件。\n" +
					"验证码为：" + verifCode +
					"\n时间：" + new Date();
		}

		msg.setText(content);
		msg.setFrom(new InternetAddress(FROM_EMAIL));

		Transport transport = session.getTransport();
		transport.connect(EMAIL_HOST, FROM_EMAIL, SMTP_AUTH_PASSWORD);
		//发送消息
		transport.sendMessage(msg, new Address[]{new InternetAddress(toEmailAddress)});
		transport.close();
	}
}
