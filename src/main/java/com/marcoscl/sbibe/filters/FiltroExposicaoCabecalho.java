package com.marcoscl.sbibe.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

@Component
public class FiltroExposicaoCabecalho implements Filter {

	@Override
	public void doFilter(ServletRequest requisicao, ServletResponse resposta, FilterChain chain)
			throws IOException, ServletException {
		HttpServletResponse resp = (HttpServletResponse) resposta;
		resp.addHeader("access-control-expose-headers", "location");
		chain.doFilter(requisicao, resposta);
	}
	
}
