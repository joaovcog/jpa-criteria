package com.jpahibernate.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

import com.jpahibernate.model.Produto;
import com.jpahibernate.repository.filter.ProdutoFilter;

public class ProdutoRepository {

	private final EntityManager manager;

	public ProdutoRepository(EntityManager manager) {
		if (manager == null) {
			throw new IllegalArgumentException("Entity Manager não pode ser nulo.");
		}

		this.manager = manager;
	}

	public List<Produto> todos() {
		List<Produto> produtos = manager.createQuery("from Produto", Produto.class).getResultList();

		return produtos;
	}

	public List<Produto> filtrar(ProdutoFilter filtro) {
		CriteriaBuilder cBuilder = manager.getCriteriaBuilder();
		CriteriaQuery<Produto> cQuery = cBuilder.createQuery(Produto.class); // select
		Root<Produto> rootProduto = cQuery.from(Produto.class); // from
		rootProduto.fetch("grupo");
		
		gerarFiltro(cBuilder, cQuery, rootProduto, filtro);

		cQuery.orderBy(cBuilder.asc(rootProduto.get("quantidade")));

		TypedQuery<Produto> tQuery = manager.createQuery(cQuery);

		if (filtro != null) {
			if (!StringUtils.isEmpty(filtro.getDescricao())) {
				tQuery.setParameter("descricao", "%" + filtro.getDescricao().toUpperCase() + "%");
			}
			
			if (!StringUtils.isEmpty(filtro.getStatus())) {
				tQuery.setParameter("status", filtro.getStatus());
			}

			if (filtro.getQtdeMinima() != null) {
				tQuery.setParameter("quantidadeMinima", filtro.getQtdeMinima());
			}

			if (filtro.getQtdeMaxima() != null) {
				tQuery.setParameter("quantidadeMaxima", filtro.getQtdeMaxima());
			}
		}

		List<Produto> produtos = tQuery.getResultList();

		return produtos;
	}

	private void gerarFiltro(CriteriaBuilder cBuilder, CriteriaQuery<Produto> cQuery, Root<Produto> rootProduto,
			ProdutoFilter filtro) {
		List<Predicate> predicates = new ArrayList<>();

		if (filtro != null) {
			if (!StringUtils.isEmpty(filtro.getDescricao())) {
				ParameterExpression<String> descricao = cBuilder.parameter(String.class, "descricao");
				predicates.add(cBuilder.like(rootProduto.get("descricao"), descricao));
			}
			
			if (!StringUtils.isEmpty(filtro.getStatus())) {
				ParameterExpression<String> status = cBuilder.parameter(String.class, "status");
				predicates.add(cBuilder.equal(rootProduto.get("status"), status));
			}

			if (filtro.getQtdeMinima() != null) {
				ParameterExpression<BigDecimal> quantidadeMinima = cBuilder.parameter(BigDecimal.class,
						"quantidadeMinima");
				predicates.add(cBuilder.ge(rootProduto.get("quantidade"), quantidadeMinima));
			}

			if (filtro.getQtdeMaxima() != null) {
				ParameterExpression<BigDecimal> quantidadeMaxima = cBuilder.parameter(BigDecimal.class,
						"quantidadeMaxima");
				predicates.add(cBuilder.le(rootProduto.get("quantidade"), quantidadeMaxima));
			}
		}

		if (!predicates.isEmpty()) {
			cQuery.select(rootProduto).where(cBuilder.and(predicates.toArray(new Predicate[0])));
		}
	}

}