package com.marcoscl.sbibe.services;

import org.springframework.security.core.context.SecurityContextHolder;

import com.marcoscl.sbibe.security.UsuarioSpringSecurity;

public class UsuarioService {
	
	// Retorna usuario logado
	public static UsuarioSpringSecurity autenticado() {
		try {
			return (UsuarioSpringSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		} catch (Exception e) {
			return null;
		}
	}
	
}
