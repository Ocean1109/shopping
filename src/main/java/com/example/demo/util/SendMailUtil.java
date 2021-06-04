package com.example.demo.util;

import java.util.Properties;
import java.util.Random;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * 发送邮件工具类
 */

public class SendMailUtil {

    /**
     * @param receiverAddress
     * @param title
     * @param body
     * @return 是否发送成功
     */
    /**发送一封邮件给指定收信人*/
    public static boolean send(String receiverAddress, String title, String body) {
        boolean result = true;

        Properties properties = new Properties();
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.host", "smtp.qq.com");
        properties.put("mail.smtp.auth", "true");
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("812929007@qq.com","aszxngpwtcctbcga");
            }
        };

        Session sendMailSession = Session.getDefaultInstance(properties,authenticator);
        Message mailMessage = new MimeMessage(sendMailSession);

        try {
            Address from = new InternetAddress("812929007@qq.com");

            mailMessage.setFrom(from);
            Address to = new InternetAddress(receiverAddress);
            mailMessage.setRecipient(Message.RecipientType.TO, to);
            mailMessage.setSubject(title);
            mailMessage.setText(body);

            Transport.send(mailMessage);

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    /**
     * @param receiverAddress
     * @return 验证码内容
     */
    /**发送一封验证码邮件给指定收信人*/
    public static String sendCode(String receiverAddress){

        Random r = new Random();
        String body;
        StringBuffer code = new StringBuffer();
        for (int i = 0; i < 6; i++) {
            code.append(r.nextInt(9)+"");
        }
        String _code = new String(code);
        body = "您的验证码为：" + _code;

        if(!SendMailUtil.send(receiverAddress, "验证码", body)){
            return "SendingException";
        }
        else{
            return _code;
        }
    }

}
