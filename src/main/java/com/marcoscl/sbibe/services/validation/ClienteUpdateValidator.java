package com.marcoscl.sbibe.services.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import com.marcoscl.sbibe.domain.Cliente;
import com.marcoscl.sbibe.dto.ClienteDTO;
import com.marcoscl.sbibe.repositories.ClienteRepository;
import com.marcoscl.sbibe.resources.exceptions.CampoMensagem;

public class ClienteUpdateValidator implements ConstraintValidator<ClienteUpdate, ClienteDTO> {
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private ClienteRepository repo;
	
	@Override
	public void initialize(ClienteUpdate ann) {
	}

	@Override
	public boolean isValid(ClienteDTO objDto, ConstraintValidatorContext context) {
		
		@SuppressWarnings("unchecked")
		Map<String, String> map = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		Integer uriId = Integer.parseInt(map.get("id"));
		
		List<CampoMensagem> lista = new ArrayList<>();
		
		Cliente aux = repo.findByEmail(objDto.getEmail());
		if (aux != null && !aux.getId().equals(uriId)) {
			lista.add(new CampoMensagem("email", "Email já existente"));
		}
		
// inclua os testes aqui, inserindo erros na lista
		for (CampoMensagem cm : lista) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(cm.getMensagem()).addPropertyNode(cm.getCampoNome())
					.addConstraintViolation();
		}
		return lista.isEmpty();
	}
}