package com.marcoscl.sbibe.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTUtil {
	
	@Value("${jwt.secret}")
	private String secret;
	
	@Value("${jwt.expiration}")
	private Long expiration;
	
	public String gerarToken(String email) {
		return Jwts.builder().setSubject(email)
				.setExpiration(new Date(System.currentTimeMillis() + expiration))
				.signWith(SignatureAlgorithm.HS512, secret.getBytes())
				.compact();
	}
	
	public boolean tokenValido(String token) {
		Claims reivindicacoes = getReivindicacoes(token);
		if (reivindicacoes != null) {
			String username = reivindicacoes.getSubject();
			Date dataExpiracao = reivindicacoes.getExpiration();
			Date agora = new Date(System.currentTimeMillis());
			if(username != null && dataExpiracao != null && agora.before(dataExpiracao)) {
				return true;
			}
		}
		return false;
	}
	
	public String getUsername(String token) {
		Claims reivindicacoes = getReivindicacoes(token);
		if (reivindicacoes != null) {
			return reivindicacoes.getSubject();
		}
		return null;
	}

	private Claims getReivindicacoes(String token) {
		try {
			return Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody();
		} catch (Exception e) {
			return null;
		}
	}
	
}
