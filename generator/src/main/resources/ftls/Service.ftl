package ${BasePackageName}${ServicePackageName};

import ${BasePackageName}${EntityPackageName}.${ClassName};
${InterfaceImport}
import org.springframework.stereotype.Service;
import com.wql.cloud.basic.datasource.baseservice.BaseService;
import java.util.List;
import org.springframework.util.Assert;
import com.github.pagehelper.PageInfo;
import com.wql.cloud.basic.datasource.dynamic.TargetDataSource;

/**
 * Author ${Author}
 * Date  ${Date}
 */
@Service
public class ${ClassName}ServiceImpl extends BaseService<${ClassName}> ${Impl} {

	@Override
	public Integer save(${ClassName} ${EntityName}) {
		return this.saveSelective(${EntityName});
	}

	@Override
	public Integer update(${ClassName} ${EntityName}) {
		return this.updateSelectiveById(${EntityName});
	}

	@Override
	public Integer delete(${ClassName} ${EntityName}) {
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
	public PageInfo<${ClassName}> queryPageList(Integer page, Integer pageSize, ${ClassName} ${EntityName}) {
		return this.pageListByRecord(page, pageSize, ${EntityName});
	}
	
}