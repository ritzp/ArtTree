package com.example.creators.smtp;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

public class MailSender extends Authenticator {
    private String host = "smtp.gmail.com";
    private String user;
    private String password;
    private Session session;
    private String code;

    public MailSender(String user, String password) {
        this.user = user;
        this.password = password;
        this.code = createEmailCode();
        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.host", host);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.socketFactory.fallback", "false");
        properties.put("mail.smtp.quitwait", "false");
        session = Session.getDefaultInstance(properties, this);
    }

    public String createEmailCode() {
        String[] strings = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
        String code = new String();

        for (int i=0; i<6; i++) {
            int rand = (int)(Math.random() * strings.length);
            code += strings[rand];
        }
        return code;
    }

    @Override
    protected synchronized PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(user, password);
    }

    public synchronized void sendMail(String subject, String body, String recipients) throws Exception {
        MimeMessage message = new MimeMessage(session);
        DataHandler handler = new DataHandler((DataSource)new ByteArrayDataSource(body.getBytes(), "text/plain"));
        message.setSender(new InternetAddress(user));
        message.setSubject(subject);
        message.setDataHandler(handler);
        if (recipients.indexOf(',') > 0) {
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));
        } else {
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipients));
        }
        Transport.send(message);
    }
}
