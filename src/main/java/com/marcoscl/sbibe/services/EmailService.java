package com.marcoscl.sbibe.services;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.SimpleMailMessage;

import com.marcoscl.sbibe.domain.Cliente;
import com.marcoscl.sbibe.domain.Pedido;

public interface EmailService {
	
	void enviarEmailConfirmacao(Pedido pedido);
	
	void enviarEmail(SimpleMailMessage msg);
	
	void enviarHtmlEmailConfirmacao(Pedido pedido);
	
	void enviarHtmlEmail(MimeMessage msg);

	void enviarNovaSenhaPorEmail(Cliente cliente, String novaSenha);

}