package com.marcoscl.sbibe.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.marcoscl.sbibe.domain.Cliente;
import com.marcoscl.sbibe.dto.ClienteDTO;
import com.marcoscl.sbibe.repositories.ClienteRepository;
import com.marcoscl.sbibe.services.exceptions.DataIntegrityException;
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
	
	public Cliente editar(Cliente cliente) {
		Cliente novoCliente = buscar(cliente.getId());
		atualizarDados(novoCliente, cliente);
		return repo.save(novoCliente);
	}

	public void excluir(Integer id) {
		buscar(id);
		try {
			repo.deleteById(id);
			
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException(
					"Não é possível excluir porque há entidades relacionadas");
			}
	}

	public List<Cliente> listarTudo() {
		return repo.findAll();
	}
	
	public Page<Cliente> buscarPagina(Integer pagina, Integer quantLinhas, String ordenacao, String direcao) {
		PageRequest pageRequest = PageRequest.of(pagina, quantLinhas, Direction.valueOf(direcao), ordenacao);
		return repo.findAll(pageRequest);
	}
	
	public Cliente apartirDTO(ClienteDTO clienteDTO) {
		return new Cliente(clienteDTO.getId(), clienteDTO.getNome(), clienteDTO.getEmail(), null, null);
	}
	
//	auxiliar a atualizar/editar os dados novo
	private void atualizarDados(Cliente novoCliente, Cliente cliente) {
		novoCliente.setNome(cliente.getNome());
		novoCliente.setEmail(cliente.getEmail());
	}
	
}
