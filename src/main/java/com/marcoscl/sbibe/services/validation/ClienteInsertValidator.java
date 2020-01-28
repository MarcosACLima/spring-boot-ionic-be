package com.marcoscl.sbibe.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.marcoscl.sbibe.domain.Cliente;
import com.marcoscl.sbibe.domain.enums.TipoCliente;
import com.marcoscl.sbibe.dto.ClienteNovoDTO;
import com.marcoscl.sbibe.repositories.ClienteRepository;
import com.marcoscl.sbibe.resources.exceptions.CampoMensagem;
import com.marcoscl.sbibe.services.validation.utils.BR;

public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, ClienteNovoDTO> {
	
	@Autowired
	private ClienteRepository repo;
	
	@Override
	public void initialize(ClienteInsert ann) {
	}

	@Override
	public boolean isValid(ClienteNovoDTO objDto, ConstraintValidatorContext context) {
		List<CampoMensagem> lista = new ArrayList<>();
		
		if (objDto.getTipo().equals(TipoCliente.PESSOAFISICA.getCod()) 
				&& !BR.isValidCPF(objDto.getCpfOuCnpj())) {
			lista.add(new CampoMensagem("cpfOuCnpj", "CPF inválido"));
		}
		
		if (objDto.getTipo().equals(TipoCliente.PESSOAJURIDICA.getCod()) 
				&& !BR.isValidCNPJ(objDto.getCpfOuCnpj())) {
			lista.add(new CampoMensagem("cpfOuCnpj", "CNPJ inválido"));
		}
		
		Cliente aux = repo.findByEmail(objDto.getEmail());
		if (aux != null) {
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