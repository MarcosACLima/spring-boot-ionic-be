package com.marcoscl.cursomc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.marcoscl.cursomc.domain.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
	
}
