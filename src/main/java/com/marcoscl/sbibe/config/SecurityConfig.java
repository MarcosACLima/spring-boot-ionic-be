package com.marcoscl.sbibe.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.marcoscl.sbibe.security.JWTFiltroDeAutenticacao;
import com.marcoscl.sbibe.security.JWTFiltroDeAutorizacao;
import com.marcoscl.sbibe.security.JWTUtil;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private Environment env;
	
	@Autowired
	private JWTUtil jwtUtil;

	private static final String[] PUBLIC_MATCHERS = { "/h2-console/**" };

	private static final String[] PUBLIC_MATCHERS_GET = { 
			"/produtos/**",
			"/categorias/**",
			"/estados/**"
	};
	
	private static final String[] PUBLIC_MATCHERS_POST = { 
			"/clientes",
			"/auth/esqueci/**"
	};
	
//	sobrecarga do configure - WebSecurity
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers(
			"/v2/api-docs", "/configuration/ui", 
			"/swagger-resources/**", "/configuration/**",
			"/swagger-ui.html", "/webjars/**"
		);
	}

//	permiter acesso a tdos do PUBLIC_MATCHERS e pedir autenticacao para todo resto
	@Override
	protected void configure(HttpSecurity http) throws Exception {

//		Permite acesso ao banco de Test h2
		if (Arrays.asList(env.getActiveProfiles()).contains("test")) {
			http.headers().frameOptions().disable();
		}

		http.cors().and().csrf().disable();
		http.authorizeRequests()
			.antMatchers(HttpMethod.POST, PUBLIC_MATCHERS_POST).permitAll()
			.antMatchers(HttpMethod.GET, PUBLIC_MATCHERS_GET).permitAll()
			.antMatchers(PUBLIC_MATCHERS).permitAll()
			.anyRequest().authenticated();
		http.addFilter(new JWTFiltroDeAutenticacao(authenticationManager(), jwtUtil));
		http.addFilter(new JWTFiltroDeAutorizacao(authenticationManager(), jwtUtil, userDetailsService));
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
	
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(criptografarSenha());
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuracao = new CorsConfiguration().applyPermitDefaultValues();
		configuracao.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "OPTIONS"));
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuracao);
		return source;
	}
	
	@Bean
	public BCryptPasswordEncoder criptografarSenha() {
		return new BCryptPasswordEncoder();
	}
	
	
}
