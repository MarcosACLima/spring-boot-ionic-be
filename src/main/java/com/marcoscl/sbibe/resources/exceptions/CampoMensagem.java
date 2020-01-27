package com.marcoscl.sbibe.resources.exceptions;

import java.io.Serializable;

public class CampoMensagem implements Serializable {

	private static final long serialVersionUID = 1L;

	private String campoNome;
	
	private String mensagem;
	
	public CampoMensagem() {
	}

	public CampoMensagem(String campoNome, String mensagem) {
		super();
		this.campoNome = campoNome;
		this.mensagem = mensagem;
	}

	public String getCampoNome() {
		return campoNome;
	}

	public void setCampoNome(String campoNome) {
		this.campoNome = campoNome;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	
}
