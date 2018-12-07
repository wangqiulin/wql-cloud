package com.wql.cloud.tool.number;

import java.math.BigDecimal;

@FunctionalInterface
public interface ToBigDecimalFunction<T> {
	
	BigDecimal applyAsBigDecimal(T value);
	
}
