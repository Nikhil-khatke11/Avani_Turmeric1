package com.avani.util;

import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class EmailUtil {

    public static void sendOrderEmail(String subject, String messageBody) {

        // Read credentials from environment variables (secure for cloud deployment)
        final String fromEmail = System.getenv("EMAIL_FROM") != null
                ? System.getenv("EMAIL_FROM") : "nkhatke2@gmail.com";
        final String password = System.getenv("EMAIL_PASSWORD") != null
                ? System.getenv("EMAIL_PASSWORD") : "nikuudishuu1117";
        final String toEmail = System.getenv("EMAIL_TO") != null
                ? System.getenv("EMAIL_TO") : "khatkenikhil2003@gmail.com";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
            new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(fromEmail, password);
                }
            });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(toEmail)
            );
            message.setSubject(subject);
            message.setText(messageBody);

            Transport.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
