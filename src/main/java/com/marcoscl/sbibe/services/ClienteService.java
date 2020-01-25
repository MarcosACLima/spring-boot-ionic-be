package com.marcoscl.sbibe.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.marcoscl.sbibe.domain.Cliente;
import com.marcoscl.sbibe.repositories.ClienteRepository;
import com.marcoscl.sbibe.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository repo;

	public Cliente buscar(Integer id) {
		Optional<Cliente> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException( 
				"Objeto não encontrado!" + id + ", Tipo: " + Cliente.class.getName()
			));
	}
	
}
