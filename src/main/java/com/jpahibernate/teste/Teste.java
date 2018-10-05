package com.jpahibernate.teste;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.jpahibernate.model.Produto;
import com.jpahibernate.repository.ProdutoRepository;
import com.jpahibernate.repository.filter.ProdutoFilter;

public class Teste {
	private final EntityManagerFactory factory = Persistence.createEntityManagerFactory("puCriteria");

	public static void main(String[] args) {
		Teste teste = new Teste();

		teste.consultar();
	}

	private void consultar() {
		EntityManager manager = factory.createEntityManager();

		final ProdutoRepository produtoRepository = new ProdutoRepository(manager);

		ProdutoFilter produtoFilter = new ProdutoFilter();
		produtoFilter.setQtdeMinima(new BigDecimal("500"));
		produtoFilter.setQtdeMaxima(new BigDecimal("1000"));
		produtoFilter.setStatus("ATIVO");

		List<Produto> produtos = produtoRepository.filtrar(produtoFilter);

		for (Produto p : produtos) {
			System.out.println(p);
		}

		manager.close();
		
		System.exit(0);
	}

}