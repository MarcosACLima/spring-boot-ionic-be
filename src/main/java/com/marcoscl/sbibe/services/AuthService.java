package com.marcoscl.sbibe.services;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.marcoscl.sbibe.domain.Cliente;
import com.marcoscl.sbibe.repositories.ClienteRepository;
import com.marcoscl.sbibe.services.exceptions.ObjectNotFoundException;

@Service
public class AuthService {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private BCryptPasswordEncoder cpe;
	
	@Autowired
	private EmailService emailService; 
	
	private Random aleatorio = new Random();
	
	public void enviarNovaSenha(String email) {
		Cliente cliente = clienteRepository.findByEmail(email);
		if (cliente == null) {
			throw new ObjectNotFoundException("Email não encontrado");
		}
		
		String novaSenha = newSenha();
		cliente.setSenha(cpe.encode(novaSenha));
		
		clienteRepository.save(cliente);
		emailService.enviarNovaSenhaPorEmail(cliente, novaSenha);
	}

	private String newSenha() {
		char[] vet = new char[10];
		for (int i = 0; i < 10; i++) {
			vet[i] = charAleatorio();
		}
		return new String(vet);
	}

	private char charAleatorio() {
		int opt = aleatorio.nextInt(3);
		if(opt == 0) { // gera um digito
			return (char) (aleatorio.nextInt(10) + 48);
		} else if (opt == 1) { // gera letra maiuscula
			return (char) (aleatorio.nextInt(26) + 65);
		} else { // gera letra minuscula
			return (char) (aleatorio.nextInt(26) + 97);
		}
	}
	
}
