package com.marcoscl.sbibe.services;

import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.marcoscl.sbibe.domain.Cidade;
import com.marcoscl.sbibe.domain.Cliente;
import com.marcoscl.sbibe.domain.Endereco;
import com.marcoscl.sbibe.domain.enums.Perfil;
import com.marcoscl.sbibe.domain.enums.TipoCliente;
import com.marcoscl.sbibe.dto.ClienteDTO;
import com.marcoscl.sbibe.dto.ClienteNovoDTO;
import com.marcoscl.sbibe.repositories.ClienteRepository;
import com.marcoscl.sbibe.repositories.EnderecoRepository;
import com.marcoscl.sbibe.security.UsuarioSpringSecurity;
import com.marcoscl.sbibe.services.exceptions.AuthorizationException;
import com.marcoscl.sbibe.services.exceptions.DataIntegrityException;
import com.marcoscl.sbibe.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private BCryptPasswordEncoder cpe;
	
	@Autowired
	private ClienteRepository repo;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private S3Service s3Service;
	
	@Autowired
	private ImagemService imagemService;
	
	@Value("${img.prefix.client.profile}")
	private String prefixo;
	
	@Value("${img.profile.size}")
	private Integer tamanho;  

	public Cliente buscar(Integer id) {
		
		UsuarioSpringSecurity usuario = UsuarioService.autenticado();
		if(usuario == null || !usuario.temPerfil(Perfil.ADMIN) && !id.equals(usuario.getId())) {
			throw new AuthorizationException("Acesso negado");
		}
		
		Optional<Cliente> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException( 
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()
			));
	}
	
	@Transactional
	public Cliente inserir(Cliente cliente) {
		cliente.setId(null);
		cliente =  repo.save(cliente);
		enderecoRepository.saveAll(cliente.getEnderecos());
		return cliente;
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
					"Não é possível excluir porque há pedidos relacionados");
			}
	}

	public List<Cliente> listarTudo() {
		return repo.findAll();
	}
	
	public Cliente buscarPorEmail(String email) {
		UsuarioSpringSecurity usuario = UsuarioService.autenticado();
		if (usuario == null || !usuario.temPerfil(Perfil.ADMIN) && !email.equals(usuario.getUsername())) {
			throw new AuthorizationException("Acesso negado");
		}
		
		Cliente cliente = repo.findByEmail(email);
		if(cliente == null) {
			throw new ObjectNotFoundException("Objeto não encontrado! Id: " + usuario.getId() + ", Tipo: " + Cliente.class.getName());
		}
		return cliente;
	}
	
	public Page<Cliente> buscarPagina(Integer pagina, Integer quantLinhas, String ordenacao, String direcao) {
		PageRequest pageRequest = PageRequest.of(pagina, quantLinhas, Direction.valueOf(direcao), ordenacao);
		return repo.findAll(pageRequest);
	}
	
	public Cliente apartirDTO(ClienteDTO clienteDTO) {
		return new Cliente(clienteDTO.getId(), clienteDTO.getNome(), clienteDTO.getEmail(), null, null, null);
	}
	
	public Cliente apartirDTO(ClienteNovoDTO clienteNovoDTO) {
		Cliente cliente =  new Cliente(null, clienteNovoDTO.getNome(), clienteNovoDTO.getEmail(),
				clienteNovoDTO.getCpfOuCnpj(), TipoCliente.toEnum(clienteNovoDTO.getTipo()), cpe.encode(clienteNovoDTO.getSenha()));
		Cidade cidade = new Cidade(clienteNovoDTO.getCidadeId(), null, null);
		Endereco endereco = new Endereco(null, clienteNovoDTO.getLogradouro(), clienteNovoDTO.getNumero(), 
				clienteNovoDTO.getComplemento(), clienteNovoDTO.getBairro(), clienteNovoDTO.getCep(),
				cliente, cidade);
		cliente.getEnderecos().add(endereco);
		cliente.getTelefones().add(clienteNovoDTO.getTelefone1());
		if(clienteNovoDTO.getTelefone2() != null) {
			cliente.getTelefones().add(clienteNovoDTO.getTelefone2());
		}
		if(clienteNovoDTO.getTelefone3() != null) {
			cliente.getTelefones().add(clienteNovoDTO.getTelefone3());
		}
		return cliente;
	}
	
//	auxiliar a atualizar/editar os dados novo
	private void atualizarDados(Cliente novoCliente, Cliente cliente) {
		novoCliente.setNome(cliente.getNome());
		novoCliente.setEmail(cliente.getEmail());
	}
	
	public URI uploadFotoPerfil(MultipartFile arquivoMultiPart) {
		
		UsuarioSpringSecurity usuario = UsuarioService.autenticado();
		if(usuario == null) {
			throw new AuthorizationException("Acesso negado");
		}
		
		BufferedImage imagem = imagemService.getJpgImagemArquivo(arquivoMultiPart);
		
		imagem = imagemService.cortarQuadrado(imagem);
		imagem = imagemService.redimensionar(imagem, tamanho);
		
		String nomeArquivo = prefixo + usuario.getId() + ".jpg";
		
		return s3Service.uploadArquivo(imagemService.getInputStream(imagem, "jpg"), nomeArquivo, "imagem");
	}
	
}
