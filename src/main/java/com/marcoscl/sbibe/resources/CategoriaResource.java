package com.marcoscl.sbibe.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.marcoscl.sbibe.domain.Categoria;
import com.marcoscl.sbibe.dto.CategoriaDTO;
import com.marcoscl.sbibe.services.CategoriaService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/categorias")
public class CategoriaResource {

	@Autowired
	private CategoriaService service;
	
//	Descrições personalizadas para os endpoints
	@ApiOperation(value = "Buscar por id")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Categoria> find(@PathVariable Integer id) {
		Categoria obj = service.buscar(id);
		return ResponseEntity.ok().body(obj);
	}
	
	@ApiOperation(value = "Insere nova categoria")
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> inserir(@Valid @RequestBody CategoriaDTO categoriaDTO) {
		Categoria categoria = service.apartirDTO(categoriaDTO);
		categoria = service.inserir(categoria);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(categoria.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@ApiOperation(value = "Atualiza categoria")
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> editar(@Valid @RequestBody CategoriaDTO categoriaDTO, @PathVariable Integer id){
		Categoria categoria = service.apartirDTO(categoriaDTO);
		categoria.setId(id);
		categoria = service.editar(categoria);
		return ResponseEntity.noContent().build();
	}
	
	@ApiOperation(value = "Remove categoria")
	@PreAuthorize("hasAnyRole('ADMIN')")
//	Mensagens de resposta específicas
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "Não é possível excluir uma categoria que possui produtos"),
			@ApiResponse(code = 404, message = "Código inexistente") })
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Categoria> excluir(@PathVariable Integer id) {
		service.excluir(id);
		return ResponseEntity.noContent().build();
	}
	
	@ApiOperation(value = "Retorna todas categorias")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<CategoriaDTO>> listarTudo() {
		List<Categoria> categorias  = service.listarTudo();
		List<CategoriaDTO> categoriasDTO = categorias.stream().map(
				categoria -> new CategoriaDTO(categoria)).collect(Collectors.toList());
		return ResponseEntity.ok().body(categoriasDTO);
	}
	
	@ApiOperation(value = "Retorna todas categorias com paginação")
	@RequestMapping(value = "/pagina", method = RequestMethod.GET)
	public ResponseEntity<Page<CategoriaDTO>> buscarPagina(		
			@RequestParam(value = "pagina", defaultValue = "0") Integer pagina, 
			@RequestParam(value = "quantLinhas", defaultValue = "24") Integer quantLinhas, 
			@RequestParam(value = "ordenacao", defaultValue = "nome") String ordenacao, 
			@RequestParam(value = "direcao", defaultValue = "ASC") String direcao) {
		Page<Categoria> categorias  = service.buscarPagina(pagina, quantLinhas, ordenacao, direcao);
		Page<CategoriaDTO> categoriasDTO = categorias.map(categoria -> new CategoriaDTO(categoria));
		return ResponseEntity.ok().body(categoriasDTO);
	}
	
}
