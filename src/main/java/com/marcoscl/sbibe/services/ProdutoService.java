package com.marcoscl.sbibe.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.marcoscl.sbibe.domain.Categoria;
import com.marcoscl.sbibe.domain.Produto;
import com.marcoscl.sbibe.repositories.CategoriaRepository;
import com.marcoscl.sbibe.repositories.ProdutoRepository;
import com.marcoscl.sbibe.services.exceptions.ObjectNotFoundException;

@Service
public class ProdutoService {
	
	@Autowired
	private ProdutoRepository repo;
	
	@Autowired
	private CategoriaRepository categoriaRepository;

	public Produto buscar(Integer id) {
		Optional<Produto> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException( 
				"Objeto não encontrado! IDd: " + id + ", Tipo: " + Produto.class.getName()
			));
	}
	
	public Page<Produto> pesquisar(String nome, List<Integer> ids, Integer pagina, Integer quantLinhas, String ordenacao, String direcao) {
		PageRequest pageRequest = PageRequest.of(pagina, quantLinhas, Direction.valueOf(direcao), ordenacao);
		List<Categoria> categorias = categoriaRepository.findAllById(ids);
		return repo.findDistinctByNomeContainingAndCategoriasIn(nome, categorias, pageRequest);
	}
	
}
