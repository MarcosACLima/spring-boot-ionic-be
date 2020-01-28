package com.marcoscl.sbibe.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.marcoscl.sbibe.domain.enums.TipoCliente;
import com.marcoscl.sbibe.dto.ClienteNovoDTO;
import com.marcoscl.sbibe.resources.exceptions.CampoMensagem;
import com.marcoscl.sbibe.services.validation.utils.BR;

public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, ClienteNovoDTO> {
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
		
// inclua os testes aqui, inserindo erros na lista
		for (CampoMensagem cm : lista) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(cm.getMensagem()).addPropertyNode(cm.getCampoNome())
					.addConstraintViolation();
		}
		return lista.isEmpty();
	}
}