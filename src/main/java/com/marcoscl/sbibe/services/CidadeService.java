package com.marcoscl.sbibe.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.marcoscl.sbibe.domain.Cidade;
import com.marcoscl.sbibe.repositories.CidadeRepository;

@Service
public class CidadeService {
	
	@Autowired
	private CidadeRepository cidadeRepository;

	public List<Cidade> listarCidadePorEstado(Integer estadoId) {
		return cidadeRepository.findCidades(estadoId);
	}
	
}