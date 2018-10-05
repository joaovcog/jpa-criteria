package com.jpahibernate.repository.filter;

import java.math.BigDecimal;

public class ProdutoFilter {

	private String descricao;
	private BigDecimal qtdeMinima;
	private BigDecimal qtdeMaxima;
	private String status;

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public BigDecimal getQtdeMinima() {
		return qtdeMinima;
	}

	public void setQtdeMinima(BigDecimal qtdeMinima) {
		this.qtdeMinima = qtdeMinima;
	}

	public BigDecimal getQtdeMaxima() {
		return qtdeMaxima;
	}

	public void setQtdeMaxima(BigDecimal qtdeMaxima) {
		this.qtdeMaxima = qtdeMaxima;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}