package com.marcoscl.sbibe.services;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class SmtpEmailService extends AbstrataEmailService {

	@Autowired
	private MailSender mailsender;
	
	@Autowired
	private JavaMailSender javaMailSender;

	private static final Logger LOG = LoggerFactory.getLogger((SmtpEmailService.class));

	@Override
	public void enviarEmail(SimpleMailMessage msg) {
		LOG.info("Enviando email...");
		mailsender.send(msg);;
		LOG.info("Email enviado!");
	}

	@Override
	public void enviarHtmlEmail(MimeMessage msg) {
		LOG.info("Enviando email HTML...");
		javaMailSender.send(msg);;
		LOG.info("Email enviado!");
	}

}
