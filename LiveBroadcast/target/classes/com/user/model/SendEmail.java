package com.user.model;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

public class SendEmail {
    public static final String HOST = "smtp.163.com";
    public static final String PROTOCOL = "smtp";
    public static final int PORT = 25;
    public static final String FROM = "LiveGamesList@163.com";//发件人的email
    public static final String PWD = "cc546404101";//发件人密码

    /**
     * 获取session
     */
    private static Session getSession(){
        Properties props = new Properties();
        props.put("mail.smtp.host", HOST);//设置服务器地址
        props.put("mail.store.protocol" , PROTOCOL);//设置协议
        props.put("mail.smtp.port", PORT);//设置端口
        props.put("mail.smtp.auth" , true);

        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM, PWD);
            }
        };

        Session session = Session.getDefaultInstance(props, authenticator);
        return session;
    }

    /**
     * 发送邮件
     */
    public static void send(String toEmail, String content){
        Session session = getSession();
        try{
//            创建一条消息
            Message msg = new MimeMessage(session);
//            设置消息参数
            msg.setFrom(new InternetAddress(FROM));
            InternetAddress[] addresses = {new InternetAddress(toEmail)};
            msg.setRecipients(Message.RecipientType.TO, addresses);
            msg.setSubject("账号激活邮件");
            msg.setSentDate(new Date());
            msg.setContent(content, "text/html;charset=utf-8");

//            发送邮件
            Transport.send(msg);
        }catch (MessagingException mex){
            mex.printStackTrace();
        }
    }
















}
