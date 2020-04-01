package ${BasePackageName}${ServicePackageName};

import ${BasePackageName}${EntityPackageName}.${ClassName};
${InterfaceImport}
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.wql.cloud.basic.datasource.baseservice.BaseService;
import java.util.List;
import org.springframework.util.Assert;
import com.github.pagehelper.PageInfo;
import com.wql.cloud.basic.datasource.dynamic.TargetDataSource;
import org.springframework.transaction.annotation.Transactional;

/**
 * Author ${Author}
 * Date  ${Date}
 */
@Service
public class ${ClassName}ServiceImpl extends BaseService<${ClassName}> ${Impl} {

    public final Logger logger = LoggerFactory.getLogger(this.getClass()); 

	@Override
	@Transactional
	public int save(${ClassName} ${EntityName}) {
		return this.save(${EntityName});
	}

	@Override
	@Transactional
	public int update(${ClassName} ${EntityName}) {
		return this.updateById(${EntityName});
	}

	@Override
	@Transactional
	public int delete(${ClassName} ${EntityName}) {
		Assert.notNull(${EntityName}.getId(), "id为空");
		return this.removeById(${EntityName}.getId());
	}
	
	@Override
	@TargetDataSource(name = "read")
	public ${ClassName} query(${ClassName} ${EntityName}) {
		Assert.notNull(${EntityName}.getId(), "id为空");
		return this.getById(${EntityName}.getId());
	}
	
	@Override
	@TargetDataSource(name = "read")
	public List<${ClassName}> queryList(${ClassName} ${EntityName}) {
		return this.listByRecord(${EntityName});
	}

	@Override
	@TargetDataSource(name = "read")
	public PageInfo<${ClassName}> queryPageList(${ClassName} ${EntityName}) {
		return this.pageByRecord(${EntityName}.getPage(), ${EntityName}.getPageSize(), ${EntityName});
	}
	
}
