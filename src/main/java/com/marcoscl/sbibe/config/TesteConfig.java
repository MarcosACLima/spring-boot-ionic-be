package com.marcoscl.sbibe.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.marcoscl.sbibe.services.BDService;

@Configuration
@Profile("test")
public class TesteConfig {
	
	@Autowired
	private BDService bdService;
	
	@Bean
	public boolean instanciarBancoDado() throws ParseException {
		bdService.instanciarTesteBancoDado();
		return true;
	}

}
