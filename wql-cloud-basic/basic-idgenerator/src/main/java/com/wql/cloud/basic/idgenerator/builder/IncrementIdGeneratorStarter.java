package com.wql.cloud.basic.idgenerator.builder;

import javax.sql.DataSource;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

public class IncrementIdGeneratorStarter {
	public BaseOnDB baseOn(DataSource dataSource) {
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(dataSource);
        PlatformTransactionManager txMgr = new DataSourceTransactionManager(dataSource);
        TransactionTemplate txTemplate = new TransactionTemplate(txMgr);
        txTemplate.setIsolationLevel(TransactionTemplate.ISOLATION_SERIALIZABLE);
        return new BaseOnDB(template, txTemplate, GeneratorType.INCR);
    }
}
