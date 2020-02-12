package com.marcoscl.sbibe.resources.exceptions;

import java.util.ArrayList;
import java.util.List;

public class ValidacaoErro extends StandardError {

	private static final long serialVersionUID = 1L;
	
	private List<CampoMensagem> erros = new ArrayList<>();
	
	public ValidacaoErro(Long timeStamp, Integer status, String erro, String mensagem, String caminho) {
		super(timeStamp, status, erro, mensagem, caminho);
	}

	public List<CampoMensagem> getErros() {
		return erros;
	}

	public void addErro(String campoNome, String mensagem) {
		erros.add(new CampoMensagem(campoNome, mensagem));
	}

}
