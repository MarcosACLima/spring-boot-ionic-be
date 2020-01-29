package com.marcoscl.sbibe.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.marcoscl.sbibe.domain.Produto;
import com.marcoscl.sbibe.dto.ProdutoDTO;
import com.marcoscl.sbibe.resources.utils.URL;
import com.marcoscl.sbibe.services.ProdutoService;

@RestController
@RequestMapping(value = "/produtos")
public class ProdutoResource {

	@Autowired
	private ProdutoService service;
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Produto> find(@PathVariable Integer id) {
		
		Produto obj = service.buscar(id);
				
		return ResponseEntity.ok().body(obj);
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Page<ProdutoDTO>> buscarPagina(	
			@RequestParam(value = "nome", defaultValue = "") String nome, 
			@RequestParam(value = "categorias", defaultValue = "") String categorias, 
			@RequestParam(value = "pagina", defaultValue = "0") Integer pagina, 
			@RequestParam(value = "quantLinhas", defaultValue = "24") Integer quantLinhas, 
			@RequestParam(value = "ordenacao", defaultValue = "nome") String ordenacao, 
			@RequestParam(value = "direcao", defaultValue = "ASC") String direcao) {
		String nomeDecodificar = URL.decodeParam(nome);
		List<Integer> ids = URL.decodeIntLista(categorias);
		Page<Produto> produtos  = service.pesquisar(nomeDecodificar, ids, pagina, quantLinhas, ordenacao, direcao);
		Page<ProdutoDTO> categoriasDTO = produtos.map(produto -> new ProdutoDTO(produto));
		return ResponseEntity.ok().body(categoriasDTO);
	}
	
}
