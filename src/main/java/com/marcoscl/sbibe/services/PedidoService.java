package com.marcoscl.sbibe.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.marcoscl.sbibe.domain.Pedido;
import com.marcoscl.sbibe.repositories.PedidoRepository;
import com.marcoscl.sbibe.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {
	
	@Autowired
	private PedidoRepository repo;

	public Pedido buscar(Integer id) {
		Optional<Pedido> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException( 
				"Objeto não encontrado!" + id + ", Tipo: " + Pedido.class.getName()
			));
	}
	
}
