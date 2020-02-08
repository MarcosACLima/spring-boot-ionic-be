package com.marcoscl.sbibe.resources;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.marcoscl.sbibe.dto.EmailDTO;
import com.marcoscl.sbibe.security.JWTUtil;
import com.marcoscl.sbibe.security.UsuarioSpringSecurity;
import com.marcoscl.sbibe.services.AuthService;
import com.marcoscl.sbibe.services.UsuarioService;

@RestController
@RequestMapping(value = "/auth")
public class AuthResource {
	
	@Autowired
	private JWTUtil jwtUtil;
	
	@Autowired
	private AuthService authService;
	
	@RequestMapping(value = "/atualizar_token", method = RequestMethod.POST)
	public ResponseEntity<Void> atualizarToken(HttpServletResponse resposta) {
		UsuarioSpringSecurity usuario = UsuarioService.autenticado();
		String token = jwtUtil.gerarToken(usuario.getUsername());
		resposta.addHeader("Authorization", "Bearer " + token);
		resposta.addHeader("access-control-expose-headers", "Authorization");
		return ResponseEntity.noContent().build();
	}
	
	@RequestMapping(value = "/esqueci", method = RequestMethod.POST)
	public ResponseEntity<Void> esqueci(@Valid @RequestBody EmailDTO emailDTO) {
		authService.enviarNovaSenha(emailDTO.getEmail());
		return ResponseEntity.noContent().build();
	}

}
