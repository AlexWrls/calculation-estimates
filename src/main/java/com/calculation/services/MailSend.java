package com.calculation.services;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
@Slf4j
public class MailSend {

    @Value("host")
    private static  String host;
    @Value("email")
    private static String username ;
    @Value("password")
    private static String password;
    @Value("protocol")
    private static  String protocol ;
    @Value("port")
    private static String port;

    public void sendEmail(String email, String messageText, String subject) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.default-encoding", "UTF-8");
        props.put("mail.smtp.auth", true);
        props.put("mail.smtp.starttls.enable", true);
        props.put("mail.port", Integer.parseInt(port));
        props.put("mail.host", host);
        props.put("mail.protocol",protocol);
        props.put("mail.username", username);
        props.put("mail.password", password);

        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
        message.setSubject(subject);
        message.setText(messageText);
        Transport.send(message);

    }
}
