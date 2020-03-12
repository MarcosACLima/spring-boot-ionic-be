package com.marcoscl.sbibe.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.marcoscl.sbibe.services.BDService;
import com.marcoscl.sbibe.services.EmailService;
import com.marcoscl.sbibe.services.SmtpEmailService;

@Configuration
@Profile("prod")
public class ProdConfig {
	
	@Autowired
	private BDService bdService;
	
	@Value("${spring.jpa.hibernate.ddl-auto}")
	private String estrategia;
	
	@Bean
	public boolean instanciarBancoDado() throws ParseException {
		
		if (!"create".equals(estrategia)) {
			return false;
		}
		
		bdService.instanciarTesteBancoDado();
		return true;
	}
	
	@Bean
	public EmailService emailService() {
		return new SmtpEmailService();
	}
			
}
