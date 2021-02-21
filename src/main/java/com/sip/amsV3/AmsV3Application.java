package com.sip.amsV3;

import java.io.File;
import java.io.IOException;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.sip.amsV3.controllers.ArticleController;
import com.sip.amsV3.controllers.ProviderController;

@SpringBootApplication
public class AmsV3Application extends SpringBootServletInitializer {
	
	//@Autowired
	// private JavaMailSender javaMailSender;
	
	public static void main(String[] args) {
		new File(ArticleController.uploadDirectory).mkdir();
		new File(ProviderController.uploadDirectoryProvider).mkdir();
		SpringApplication.run(AmsV3Application.class, args);
		System.out.println("Welcome AMS project V3 Running...");
		System.out.println("Retour a la branche >>>> master avec Merge");
	}
	/*
	 * extends SpringBootServletInitializer implements CommandLineRunner
	 * void sendEmail() {
	 SimpleMailMessage msg = new SimpleMailMessage();
	 msg.setTo("marouen.methnani@yahoo.fr");
	 msg.setSubject("Testing from Spring Boot");
	 msg.setText("Hello World Spring Boot Email");
	 javaMailSender.send(msg);
	 }
	
	@Override
	 public void run(String... args) throws MessagingException, IOException {
	 System.out.println("Sending Email...");
	 sendEmail();
	 System.out.println("Done");
	 }
*/
}
