package com.jwt.service.mail;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;

public class Mailer {

	Properties props;
	String username;
	String password;

	public Mailer(ServletContext context) {
		props = new Properties();
		 String host = "smtp.gmail.com";
	     props.put("mail.smtp.starttls.enable", "true");
	     props.put("mail.smtp.host", host);
	     props.put("mail.smtp.port", "587");
	     props.put("mail.smtp.auth", "true");

		String realPath = context.getRealPath("/WEB-INF/mail.properties");
		try {
			Properties props = new Properties();
			props.load(new FileInputStream(new File(realPath)));
		
			this.username = props.getProperty("mail");
			this.password = props.getProperty("mail_pwd");	
		}catch(IOException ex) {
			ex.printStackTrace();
		}
		
	}

	public boolean sendMail(String from, String to, String subject, String text) {
		boolean success = true;
		Authenticator authenticator = new Authenticator() {
		    protected PasswordAuthentication getPasswordAuthentication() {
		        return new PasswordAuthentication(username, password);
		    }
		};
		
		Session session = Session.getInstance(props, authenticator);

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			message.setSubject(subject);
			//message.setText(text);
			//html texts
			message.setContent(text, "text/html");
			Transport transport = session.getTransport("smtp");
			Transport.send(message);
	        transport.close();

			System.out.println("Message send to " + to);

		} catch (MessagingException e) {
			e.printStackTrace();
			success = false;
		}
		
		return success;
	}

	// TODO
	public boolean sendRegistrationMail(String to, String userName) {
		String text = String.format("<h1>You have registered for adrenaline - the motorbike community app!</h1><p>You successfully registered for our app. Your username is %s.</p>", userName);
		
		return sendMail("registration@adrenaline.com", to, "Your registration for adrenaline", text);
	}
	
	public boolean sendResetPasswordMail(String to, String newPassword) {
		String text = String.format("<h1>You have requested a password update</h1><p>Your new password is %s.</p>", newPassword);
		
		return sendMail("registration@adrenaline.com", to, "Your Password was restored", text);
	}
}
