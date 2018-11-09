package com.wql.cloud.basic.idgenerator.util;

import com.wql.cloud.basic.idgenerator.builder.IncrementIdGeneratorStarter;

/**
 * 调用方式：(默认表名修改位置：BaseOnDBProps)
 * 
 *  @Autowired
	private DataSource dataSource;
	private BaseOnDBProps props = BaseOnDBProps.of("projectName", "modelName");
		
   	IncrementIdGenerator generator = (IncrementIdGenerator) IdPluginUtil.incrementGenerator().baseOn(dataSource).props(props).build();
 * 
 */
public class IdPluginUtil {
	
	public static IncrementIdGeneratorStarter incrementGenerator() {
        return new IncrementIdGeneratorStarter();
    }
	
}
