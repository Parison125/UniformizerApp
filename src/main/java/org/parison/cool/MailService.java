package org.parison.cool;

import org.apache.log4j.Logger;
import org.parison.cool.data.MailData;
import org.springframework.context.ApplicationContext;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailService {

    final static Logger LOGGER = Logger.getLogger(MailService.class);
    ApplicationContext applicationContext;
    MailData mailData;
    String to ;
    String from ;
    String username;
    String password;

    public MailService(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        mailData = (MailData) this.applicationContext.getBean("mailData");
        this.username = mailData.getUsername();
        this.password = mailData.getPassword();
        this.from = mailData.getFrom();
        this.to = mailData.getTo();
    }

    public void sendEmail () {

        LOGGER.debug("Setting email configuration");

        String host = "smtp.gmail.com";
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "465");
        props.put("mail.debug", "true");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        LOGGER.debug("Getting email session");

        // Get the Session object.
        Session session = Session.getInstance(props,
        new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        LOGGER.debug("Creating email body");

        try {
            // Create a default MimeMessage object.
            Message message = new MimeMessage(session);
            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));
            // Set To: header field of the header.
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));

            // Set Subject: header field
            message.setSubject("Programme Uniformisation");

            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();

            // Now set the actual message
            messageBodyPart.setText("Veuillez trouver en pièce jointe le fichier excel traité. \nCordialement");

            // Create a multipar message
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            // Part two is attachment
            messageBodyPart = new MimeBodyPart();
            String filename = System.getProperty("user.dir")+"\\etc\\theseModifie.xlsx";
            LOGGER.debug("Attach file to email : " + filename);
            DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filename);
            multipart.addBodyPart(messageBodyPart);

            // Send the complete message parts
            message.setContent(multipart);

            LOGGER.debug("Sending email");
            // Send message
            Transport.send(message);

            LOGGER.debug("Sent message successfully.");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
