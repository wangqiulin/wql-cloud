package com.wql.cloud.basic.idgenerator.generator;

public interface IdGenerator {
	
	long gen();
	
	long[] gen(int count);
	
}
