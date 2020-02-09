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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.marcoscl.sbibe.domain.Cliente;
import com.marcoscl.sbibe.dto.ClienteDTO;
import com.marcoscl.sbibe.dto.ClienteNovoDTO;
import com.marcoscl.sbibe.services.ClienteService;

@RestController
@RequestMapping(value = "/clientes")
public class ClienteResource {

	@Autowired
	private ClienteService service;
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Cliente> find(@PathVariable Integer id) {
		
		Cliente obj = service.buscar(id);
				
		return ResponseEntity.ok().body(obj);
	}
	
	@RequestMapping(value = "/email", method = RequestMethod.GET)
	public ResponseEntity<Cliente> buscar(@RequestParam(value = "valor") String email){
		Cliente cliente = service.buscarPorEmail(email);
		return ResponseEntity.ok().body(cliente);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> inserir(@Valid @RequestBody ClienteNovoDTO clienteNovoDTO) {
		Cliente cliente = service.apartirDTO(clienteNovoDTO);
		cliente = service.inserir(cliente);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(cliente.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> editar(@Valid @RequestBody ClienteDTO clienteDTO, @PathVariable Integer id){
		Cliente cliente = service.apartirDTO(clienteDTO);
		cliente.setId(id);
		cliente = service.editar(cliente);
		return ResponseEntity.noContent().build();
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Cliente> excluir(@PathVariable Integer id) {
		service.excluir(id);
		return ResponseEntity.noContent().build();
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<ClienteDTO>> listarTudo() {
		List<Cliente> clientes  = service.listarTudo();
		List<ClienteDTO> clientesDTO = clientes.stream().map(
				cliente -> new ClienteDTO(cliente)).collect(Collectors.toList());
		return ResponseEntity.ok().body(clientesDTO);
	}
	
	@RequestMapping(value = "/pagina", method = RequestMethod.GET)
	public ResponseEntity<Page<ClienteDTO>> buscarPagina(		
			@RequestParam(value = "pagina", defaultValue = "0") Integer pagina, 
			@RequestParam(value = "quantLinhas", defaultValue = "24") Integer quantLinhas, 
			@RequestParam(value = "ordenacao", defaultValue = "nome") String ordenacao, 
			@RequestParam(value = "direcao", defaultValue = "ASC") String direcao) {
		Page<Cliente> clientes  = service.buscarPagina(pagina, quantLinhas, ordenacao, direcao);
		Page<ClienteDTO> clientesDTO = clientes.map(cliente -> new ClienteDTO(cliente));
		return ResponseEntity.ok().body(clientesDTO);
	}
	
	@RequestMapping(value = "/foto", method = RequestMethod.POST)
	public ResponseEntity<Void> uploadFotoPerfil(@RequestParam(name = "arquivo") MultipartFile arquivo) {
		URI uri = service.uploadFotoPerfil(arquivo);
		return ResponseEntity.created(uri).build();
	}
	
}
