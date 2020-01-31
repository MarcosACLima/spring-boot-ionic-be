package com.marcoscl.sbibe.services;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;

public class MockEmailService extends AbstrataEmailService {
	
	private static final Logger LOG = LoggerFactory.getLogger((MockEmailService.class));

	@Override
	public void enviarEmail(SimpleMailMessage msg) {
		LOG.info("Simulando envio de Email...");
		LOG.info(msg.toString());
		LOG.info("Email enviado!");
	}

	@Override
	public void enviarHtmlEmail(MimeMessage msg) {
		LOG.info("Simulando envio de Email HTML...");
		LOG.info(msg.toString());
		LOG.info("Email enviado!");
	}

}
