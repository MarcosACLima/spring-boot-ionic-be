package com.marcoscl.sbibe.services;

import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.marcoscl.sbibe.domain.Pedido;

public abstract class AbstrataEmailService implements EmailService {

	@Value("${default.sender}")
	private String remetente;

	@Autowired
	private TemplateEngine templateEngine;

	@Autowired
	private JavaMailSender javaMailSender;

	@Override
	public void enviarEmailConfirmacao(Pedido pedido) {
		SimpleMailMessage sm = preparaSimpleMailMessagePedido(pedido);
		enviarEmail(sm);
	}

	protected SimpleMailMessage preparaSimpleMailMessagePedido(Pedido pedido) {
		SimpleMailMessage sm = new SimpleMailMessage();
		sm.setTo(pedido.getCliente().getEmail());
		sm.setFrom(remetente);
		sm.setSubject("Pedido confirmado! Código: " + pedido.getId());
		sm.setSentDate(new Date(System.currentTimeMillis()));
		sm.setText(pedido.toString());
		return sm;
	}

	protected String htmlFromTemplatePedido(Pedido pedido) {
		Context context = new Context();
		context.setVariable("pedido", pedido); // apelido
		return templateEngine.process("email/confirmacaoPedido", context); // caminho do arquivo
	}

	@Override
	public void enviarHtmlEmailConfirmacao(Pedido pedido) {
		try {
			MimeMessage mm = preparaMimeMessageParaPedido(pedido);
			enviarHtmlEmail(mm);
		} catch (MessagingException e) {
			enviarEmailConfirmacao(pedido);
		}
	}

	protected MimeMessage preparaMimeMessageParaPedido(Pedido pedido) throws MessagingException {
		MimeMessage mm = javaMailSender.createMimeMessage();
		MimeMessageHelper mmh = new MimeMessageHelper(mm, true);
		mmh.setTo(pedido.getCliente().getEmail());
		mmh.setFrom(remetente);
		mmh.setSubject("Pedido confirmado! Código: " + pedido.getId());
		mmh.setSentDate(new Date(System.currentTimeMillis()));
		mmh.setText(htmlFromTemplatePedido(pedido), true);
		return mm;
	}

}
