package com.marcoscl.sbibe.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.marcoscl.sbibe.domain.Categoria;
import com.marcoscl.sbibe.repositories.CategoriaRepository;
import com.marcoscl.sbibe.services.exceptions.DataIntegrityException;
import com.marcoscl.sbibe.services.exceptions.ObjectNotFoundException;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository repo;

	public Categoria buscar(Integer id) {
		Optional<Categoria> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException( 
				"Objeto não encontrado!" + id + ", Tipo: " + Categoria.class.getName()
			));
	}
	
	public Categoria inserir(Categoria categoria) {
		categoria.setId(null);
		return repo.save(categoria);
	}
	
	public Categoria editar(Categoria categoria) {
		buscar(categoria.getId());
		return repo.save(categoria);
	}

	public void excluir(Integer id) {
		buscar(id);
		try {
			repo.deleteById(id);
			
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException(
					"Não é possível excluir uma categoria que possui produtos");
			}
	}
	
}
