package com.marcoscl.sbibe.resources.exceptions;

import java.util.ArrayList;
import java.util.List;

public class ValidacaoErro extends StandardError {

	private static final long serialVersionUID = 1L;
	
	private List<CampoMensagem> erros = new ArrayList<>();

	public ValidacaoErro(Integer status, String msg, Long timeStamp) {
		super(status, msg, timeStamp);
	}

	public List<CampoMensagem> getErros() {
		return erros;
	}

	public void addErro(String campoNome, String mensagem) {
		erros.add(new CampoMensagem(campoNome, mensagem));
	}

}
