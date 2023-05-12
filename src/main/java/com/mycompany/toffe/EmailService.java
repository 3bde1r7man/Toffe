/**
 * This class provides functionality to send emails through a Gmail SMTP server.
 * It allows sending emails to a specified recipient email with a verification code.
 */
package com.mycompany.toffe;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailService {
    String senderEmail;
    String senderPassword;
    String emailBody;

    /**
     * Constructs an instance of EmailService with the default email sender, password and body.
     */
    public EmailService() {
        senderEmail = "ahanfybekheet@gmail.com";
        senderPassword = "kaqmzbotmsagmhxt";
        emailBody = "This is email from toffe to verifing your email address copy this code and write it in the application.\nthe code is: ";
    }

    /**
     * Sends an email with a verification code to a specified recipient email.
     * 
     * @param recipientEmail The email address of the recipient.
     * @param code The verification code to be included in the email.
     * @throws MessagingException if an error occurs while sending the email.
     */
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
        message.setText(emailBody+code);

        // Send the message
        Transport.send(message);

        System.out.println("Email sent successfully.");
    }
}