package com.marcoscl.sbibe.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.marcoscl.sbibe.domain.Categoria;
import com.marcoscl.sbibe.dto.CategoriaDTO;
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
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName()
			));
	}
	
	public Categoria inserir(Categoria categoria) {
		categoria.setId(null);
		return repo.save(categoria);
	}
	
	public Categoria editar(Categoria categoria) {
		Categoria novaCategoria = buscar(categoria.getId());
		atualizarDados(novaCategoria, categoria);
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

	public List<Categoria> listarTudo() {
		return repo.findAll();
	}
	
	public Page<Categoria> buscarPagina(Integer pagina, Integer quantLinhas, String ordenacao, String direcao) {
		PageRequest pageRequest = PageRequest.of(pagina, quantLinhas, Direction.valueOf(direcao), ordenacao);
		return repo.findAll(pageRequest);
	}
	
	public Categoria apartirDTO(CategoriaDTO categoriaDTO) {
		return new Categoria(categoriaDTO.getId(), categoriaDTO.getNome());
	}

	private void atualizarDados(Categoria novaCategoria, Categoria categoria) {
		novaCategoria.setNome(categoria.getNome());
	}
	
}
	
