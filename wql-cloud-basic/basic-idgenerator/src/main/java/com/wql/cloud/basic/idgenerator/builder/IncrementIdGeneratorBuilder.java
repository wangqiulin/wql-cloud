package com.wql.cloud.basic.idgenerator.builder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;

import com.wql.cloud.basic.idgenerator.dao.impl.IdGenDAOImpl;
import com.wql.cloud.basic.idgenerator.generator.IdGenerator;
import com.wql.cloud.basic.idgenerator.generator.IncrementIdGenerator;
import com.wql.cloud.basic.idgenerator.model.IdGenInfo;

public class IncrementIdGeneratorBuilder implements IdGeneratorBuilder {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private BaseOnDB baseOn;

    private BaseOnDBProps props;

	public void setBaseOn(BaseOnDB baseOn) {
		this.baseOn = baseOn;
	}
	public void setProps(BaseOnDBProps props) {
		this.props = props;
	}
	private static Map<BaseOnDBProps, IncrementIdGenerator> generatorCache = new ConcurrentHashMap<>();

	private static Object lock = new Object();
	
	@Override
	public IdGenerator build() {
		IncrementIdGenerator generator = null;
        synchronized (lock) {
            generator = generatorCache.get(props);
            if (generator != null) {
                return generator;
            }
            generator = new IncrementIdGenerator();
            generatorCache.put(props, generator);
        }

        generator.setProject(props.getProject());
        generator.setModel(props.getModel());
        generator.setTxTemplate(baseOn.getTxTemplate());

        IdGenDAOImpl dao = new IdGenDAOImpl();
        dao.setTemplate(baseOn.getTemplate());
        dao.setProps(props);
        generator.setDao(dao);

        IdGenInfo criteria = new IdGenInfo();
        criteria.setProjectName(props.getProject());
        criteria.setModelName(props.getModel());
        IdGenInfo result = dao.get(criteria);
        if (result == null) {
            try {
                dao.insert(criteria);
            } catch (DuplicateKeyException e) {
                logger.warn("model" + props.getModel() + "已经创建", e);
            }
        }
        return generator;
	}

}
