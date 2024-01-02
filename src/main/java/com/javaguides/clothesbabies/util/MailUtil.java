package com.javaguides.clothesbabies.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Component
public class MailUtil {

    @Value("${mail.smtp.user}")
    private String username;

    @Value("${mail.smtp.password}")
    private String password;

    @Value("${mail.smtp.fromAddress}")
    private String fromAddress;

    @Value("${mail.smtp.auth}")
    private Boolean auth;

    @Value("${mail.smtp.starttls.enable}")
    private Boolean enable;

    @Value("${mail.smtp.host}")
    private String host;

    @Value("${mail.smtp.port}")
    private String port;

    private transient String subject;

    private transient String toAddress;

    private transient String message;

    private Session session;

    public void sendEmail(final String email, final String subject, final String message) {
        try {
            init();
            this.setToAddress(email);
            this.setMessage(message);
            this.setSubject(subject);
            this.sendMail();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private void init() throws NoSuchProviderException {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", auth);
        properties.put("mail.smtp.starttls.enable",enable);
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.ssl.trust", host);
        properties.put("jdk.tls.disableAlgorithms","TLSv1,TLSv1.1");
        connect(properties);
    }

    private void connect(Properties properties) throws NoSuchProviderException{
        if (null == session){
            session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });
        }
    }

    private void sendMail() {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromAddress, "support.services@gmail.com"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));
            message.setSubject(this.subject);
            message.setContent(this.message, "text/html");
            Transport.send(message);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
