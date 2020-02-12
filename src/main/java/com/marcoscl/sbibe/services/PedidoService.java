package com.marcoscl.sbibe.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.marcoscl.sbibe.domain.Cliente;
import com.marcoscl.sbibe.domain.ItemPedido;
import com.marcoscl.sbibe.domain.PagamentoComBoleto;
import com.marcoscl.sbibe.domain.Pedido;
import com.marcoscl.sbibe.domain.enums.EstadoPagamento;
import com.marcoscl.sbibe.repositories.ItemPedidoRepository;
import com.marcoscl.sbibe.repositories.PagamentoRepository;
import com.marcoscl.sbibe.repositories.PedidoRepository;
import com.marcoscl.sbibe.security.UsuarioSpringSecurity;
import com.marcoscl.sbibe.services.exceptions.AuthorizationException;
import com.marcoscl.sbibe.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {
	
	@Autowired
	private PedidoRepository repo;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private EmailService emailService;

	public Pedido buscar(Integer id) {
		Optional<Pedido> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException( 
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()
			));
	}
	
	public Pedido inserir(Pedido pedido) {
		pedido.setId(null);
		pedido.setInstante(new Date());
		pedido.setCliente(clienteService.buscar(pedido.getCliente().getId()));
		pedido.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		pedido.getPagamento().setPedido(pedido);
		if (pedido.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) pedido.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, pedido.getInstante());
		}
		pedido = repo.save(pedido);
		pagamentoRepository.save(pedido.getPagamento());
		for (ItemPedido ip : pedido.getItens()) {
			ip.setDesconto(0.0);
			ip.setProduto(produtoService.buscar(ip.getProduto().getId()));
			ip.setPreco(ip.getProduto().getPreco());
			ip.setPedido(pedido);
		}
		itemPedidoRepository.saveAll(pedido.getItens());
//		emailService.enviarEmailConfirmacao(pedido);
		emailService.enviarHtmlEmailConfirmacao(pedido);
		return pedido;
	}
	
	public Page<Pedido> buscarPagina(Integer pagina, Integer quantLinhas, String ordenacao, String direcao) {
		UsuarioSpringSecurity usuario = UsuarioService.autenticado();
		if(usuario == null) {
			throw new AuthorizationException("Acesso negado");
		}
		
		PageRequest pageRequest = PageRequest.of(pagina, quantLinhas, Direction.valueOf(direcao), ordenacao);
		Cliente cliente = clienteService.buscar(usuario.getId());
		return repo.findByCliente(cliente, pageRequest);
	}
	
}
