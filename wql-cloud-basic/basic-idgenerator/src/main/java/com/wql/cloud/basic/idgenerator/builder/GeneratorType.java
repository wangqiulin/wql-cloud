package com.wql.cloud.basic.idgenerator.builder;

public enum GeneratorType {
	
	INCR(new IncrementIdGeneratorBuilder());

	private IdGeneratorBuilder builder;

	public IdGeneratorBuilder getBuilder() {
		return builder;
	}

	GeneratorType(IdGeneratorBuilder builder) {
		this.builder = builder;
	}
}
