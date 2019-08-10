package com.ddd.sun.sms2;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Created by Sun on 2016/9/9.
 */

public class SimpleMailSender{

    public boolean sendTextMail(MainActivity.MailSenderInfo mailSenderInfo) {
        MyAuthenticator authenticator = null;
        Properties pro = mailSenderInfo.getProperties();
        //是否验证
        //if(mailInfo.isValidate()){
        authenticator = new MyAuthenticator(mailSenderInfo.username, mailSenderInfo.password);
        //System.out.println("in sendTextMail authenticator!");
        //}
        // 根据邮件会话属性和密码验证器构造一个发送邮件的session
        Session sendMailSession = Session.getDefaultInstance(pro,authenticator);
        try {
            // 根据session创建一个邮件消息
           //System.out.println("in sendTextmail try");
            Message mailMessage = new MimeMessage(sendMailSession);
            // 创建邮件发送者地址
            Address from = new InternetAddress(mailSenderInfo.fromAddress);
            // 设置邮件消息的发送者
            mailMessage.setFrom(from);
            // 创建邮件的接收者地址，并设置到邮件消息中
            Address to = new InternetAddress(mailSenderInfo.toAddress);
            mailMessage.setRecipient(Message.RecipientType.TO, to);
            // 设置邮件消息的主题
            mailMessage.setSubject(mailSenderInfo.subject);
            // 设置邮件消息发送的时间
            mailMessage.setSentDate(new Date());
            // 设置邮件消息的主要内容
            String mailContent = mailSenderInfo.content;
            mailMessage.setText(mailContent);
            // 发送邮件
            System.out.println("before transport send mail message! "+mailMessage+" "+from+" "+to+" "+mailContent);
            Transport.send(mailMessage);
            System.out.println("transport send mail message ok!");
            return true;
        } catch (Exception e) {
            Log.e("In SendMail...", e.getMessage(), e);
            //ex.printStackTrace();
        }
        return false;
    }

}