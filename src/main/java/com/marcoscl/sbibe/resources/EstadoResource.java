package com.marcoscl.sbibe.resources;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.marcoscl.sbibe.domain.Cidade;
import com.marcoscl.sbibe.domain.Estado;
import com.marcoscl.sbibe.dto.CidadeDTO;
import com.marcoscl.sbibe.dto.EstadoDTO;
import com.marcoscl.sbibe.services.CidadeService;
import com.marcoscl.sbibe.services.EstadoService;

@RestController
@RequestMapping(value = "/estados")
public class EstadoResource {
	
	@Autowired
	private EstadoService estadoService;
	
	@Autowired
	private CidadeService cidadeService;

//	 endpint obter estados 
	@RequestMapping(method =  RequestMethod.GET)
	public ResponseEntity<List<EstadoDTO>> listarEstados() {
		List<Estado> estados = estadoService.listarTudo();
		List<EstadoDTO> estadosDTO = estados.stream().map(
				estado -> new EstadoDTO(estado)).collect(Collectors.toList());
		return ResponseEntity.ok().body(estadosDTO);
	}
	
//	endpoint obter cidades
	@RequestMapping(value = "{estadoId}/cidades",method =  RequestMethod.GET)
	public ResponseEntity<List<CidadeDTO>> listarCidadesPorEstado(@PathVariable Integer estadoId) {
		List<Cidade> cidades = cidadeService.listarCidadePorEstado(estadoId);
		List<CidadeDTO> cidadesDTO = cidades.stream().map(
				cidade -> new CidadeDTO(cidade)).collect(Collectors.toList());
		return ResponseEntity.ok().body(cidadesDTO);
	}
	
}
