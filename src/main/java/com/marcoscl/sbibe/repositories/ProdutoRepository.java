package com.marcoscl.sbibe.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.marcoscl.sbibe.domain.Categoria;
import com.marcoscl.sbibe.domain.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer> {

//	Consulta usando padrao de nome do Framework Spring Data
	@Transactional(readOnly = true)
	Page<Produto> findDistinctByNomeContainingAndCategoriasIn(String nome, List<Categoria> categorias, Pageable pageRequest);

/*
 // Consulta JPQL
	@Query("SELECT DISTINCT obj FROM Produto obj JOIN obj.categorias cat WHERE obj.nome LIKE %:nome% AND cat IN :categorias")
	Page<Produto> pesquisar(@Param("nome") String nome, @Param("categorias") List<Categoria> categorias, Pageable pageRequest);
*/
}
