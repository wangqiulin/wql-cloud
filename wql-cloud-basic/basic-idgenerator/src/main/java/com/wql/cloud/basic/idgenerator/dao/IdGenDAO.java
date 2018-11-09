package com.wql.cloud.basic.idgenerator.dao;

import com.wql.cloud.basic.idgenerator.model.IdGenInfo;

public interface IdGenDAO {
	
	int update(IdGenInfo criteria);
    
    IdGenInfo get(IdGenInfo criteria);

    int insert(IdGenInfo criteria);
}
