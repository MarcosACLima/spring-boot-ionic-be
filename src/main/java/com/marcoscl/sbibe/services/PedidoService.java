package com.marcoscl.sbibe.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.marcoscl.sbibe.domain.ItemPedido;
import com.marcoscl.sbibe.domain.Pagamento;
import com.marcoscl.sbibe.domain.PagamentoComBoleto;
import com.marcoscl.sbibe.domain.Pedido;
import com.marcoscl.sbibe.domain.enums.EstadoPagamento;
import com.marcoscl.sbibe.repositories.ItemPedidoRepository;
import com.marcoscl.sbibe.repositories.PagamentoRepository;
import com.marcoscl.sbibe.repositories.PedidoRepository;
import com.marcoscl.sbibe.repositories.ProdutoRepository;
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

	public Pedido buscar(Integer id) {
		Optional<Pedido> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException( 
				"Objeto não encontrado!" + id + ", Tipo: " + Pedido.class.getName()
			));
	}
	
	public Pedido inserir(Pedido pedido) {
		pedido.setId(null);
		pedido.setInstante(new Date());
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
			ip.setPreco(produtoService.buscar(ip.getProduto().getId()).getPreco());
			ip.setPedido(pedido);
		}
		itemPedidoRepository.saveAll(pedido.getItens());
		return pedido;
	}
	
}
