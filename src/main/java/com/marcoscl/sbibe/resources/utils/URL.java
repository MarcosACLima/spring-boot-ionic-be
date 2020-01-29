package com.marcoscl.sbibe.resources.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class URL {
	
	// decoficar um param em String basico
	public static String decodeParam(String s) {
		try {
			return URLDecoder.decode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}
	
	// Converter String em Lista de Inteiros
	public static List<Integer> decodeIntLista(String s) {
		String[] vet = s.split(",");
		List<Integer> lista = new ArrayList<>();
		for (int i = 0; i < vet.length; i++) {
			lista.add(Integer.parseInt(vet[i]));
		}
		return lista;
//		Lambda
//		return Arrays.asList(s.split(",")).stream().map(x -> Integer.parseInt(x)).collect(Collectors.toList());
	}

}
