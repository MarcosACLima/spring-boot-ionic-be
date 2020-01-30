package com.marcoscl.sbibe.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;

import com.marcoscl.sbibe.domain.Pedido;

public abstract class AbstrataEmailService implements EmailService {
	
	@Value("${default.sender}")
	private String remetente;

	@Override
	public void enviarEmailConfirmacao(Pedido pedido) {
		SimpleMailMessage sm = preparaSimpleMailMessagePedido(pedido);
		enviarEmail(sm);
	}

	protected SimpleMailMessage preparaSimpleMailMessagePedido(Pedido pedido) {
		SimpleMailMessage sm =new SimpleMailMessage();
		sm.setTo(pedido.getCliente().getEmail());
		sm.setFrom(remetente);
		sm.setSubject("Pedido confirmado! Código: " + pedido.getId());
		sm.setSentDate(new Date(System.currentTimeMillis()));
		sm.setText(pedido.toString());
		return sm;
	}
	
}
