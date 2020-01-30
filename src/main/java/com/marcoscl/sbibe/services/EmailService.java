package com.marcoscl.sbibe.services;

import org.springframework.mail.SimpleMailMessage;

import com.marcoscl.sbibe.domain.Pedido;

public interface EmailService {
	
	void enviarEmailConfirmacao(Pedido pedido);
	
	void enviarEmail(SimpleMailMessage msg);

}