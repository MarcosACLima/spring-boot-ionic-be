package com.marcoscl.sbibe.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.marcoscl.sbibe.domain.Estado;
import com.marcoscl.sbibe.repositories.EstadoRepository;

@Service
public class EstadoService {

	
	@Autowired
	private EstadoRepository estadoRepository;

	public List<Estado> listarTudo() {
		return estadoRepository.findAllByOrderByNome();
	}
	
}
