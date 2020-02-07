package com.marcoscl.sbibe.services.exceptions;

public class ArquivoException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public ArquivoException(String msg) {
		super(msg);
	}
	
	public ArquivoException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
