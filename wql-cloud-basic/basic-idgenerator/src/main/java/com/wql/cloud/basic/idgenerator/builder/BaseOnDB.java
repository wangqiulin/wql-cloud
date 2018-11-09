package com.wql.cloud.basic.idgenerator.builder;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;

public class BaseOnDB {

	private NamedParameterJdbcTemplate template;

	private TransactionTemplate txTemplate;

	private GeneratorType type;

	public NamedParameterJdbcTemplate getTemplate() {
		return template;
	}

	public TransactionTemplate getTxTemplate() {
		return txTemplate;
	}

	public GeneratorType getType() {
		return type;
	}

	public BaseOnDB(NamedParameterJdbcTemplate template, TransactionTemplate txTemplate, GeneratorType type) {
		this.template = template;
		this.txTemplate = txTemplate;
		this.type = type;
	}

	public IncrementIdGeneratorBuilder props(BaseOnDBProps props) {
		IncrementIdGeneratorBuilder builder = new IncrementIdGeneratorBuilder();
		builder.setBaseOn(this);
		builder.setProps(props);
		return builder;
	}
}
