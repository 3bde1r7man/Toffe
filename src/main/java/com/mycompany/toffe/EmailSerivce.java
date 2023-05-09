package com.mycompany.toffe;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailSerivce {
    String senderEmail;
    String senderPassword;
    String message;

    public EmailSerivce() {
        senderEmail = "ahanfybekheet@gmail.com";
        senderPassword = "kaqmzbotmsagmhxt";
        message = "This is email from toffe to verifing your email address copy this code and write it in the application.\nthe code is: ";
    }

    public void sendEmail(String recipientEmail, String code) throws MessagingException {
        // Mail server properties
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Create a new Session object
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(senderEmail, senderPassword);
                    }
                });

        // Create a new message
        Message message = new MimeMessage(session);

        // Set the From and To addresses
        message.setFrom(new InternetAddress(senderEmail));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));

        // Set the subject and message body
        message.setSubject("Toffe verifing email system");
        message.setText(message+code);

        // Send the message
        Transport.send(message);

        System.out.println("Email sent successfully.");
    }
}
