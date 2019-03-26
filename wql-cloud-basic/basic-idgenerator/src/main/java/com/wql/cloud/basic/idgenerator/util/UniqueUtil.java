package com.wql.cloud.basic.idgenerator.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;

import com.wql.cloud.basic.idgenerator.builder.BaseOnDBProps;
import com.wql.cloud.basic.idgenerator.builder.IncrementIdGeneratorStarter;
import com.wql.cloud.basic.idgenerator.generator.IncrementIdGenerator;

/**
 * 通用流水号获取
 * (默认表名修改位置：BaseOnDBProps)
 * 
 * 	@Autowired
	private DataSource dataSource;
 * 
 */
public class UniqueUtil {

	public static BaseOnDBProps PROJECT_MODEL_NAME = BaseOnDBProps.of("项目名", "model名", "表名");

	/**
	 * 分布式获取唯一值
	 * 
	 * @param props ： BaseOnDBProps.of("项目名", "model名", "表名")
	 * @param dataSource ： 数据源
	 * @return
	 */
	public static String getNumber(BaseOnDBProps props, DataSource dataSource) {
		IncrementIdGenerator generator = (IncrementIdGenerator) incrementGenerator().baseOn(dataSource)
				.props(props).build();
		String number = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + generator.gen();
		return number;
	}
	
	/**
	 * 分布式获取唯一值
	 * 
	 * @param prefix ：前缀
	 * @param props ： BaseOnDBProps.of("项目名", "model名", "表名")
	 * @param dataSource ： 数据源
	 * @return
	 */
	public static String getNumber(String prefix, BaseOnDBProps props, DataSource dataSource) {
		IncrementIdGenerator generator = (IncrementIdGenerator) incrementGenerator().baseOn(dataSource)
				.props(props).build();
		String number = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + generator.gen();
		return StringUtils.isNotBlank(prefix) ? prefix + number : number;
	}
	
	
	public static IncrementIdGeneratorStarter incrementGenerator() {
        return new IncrementIdGeneratorStarter();
    }
	
	
}
