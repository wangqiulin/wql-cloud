package ${BasePackageName}${InterfacePackageName};

import ${BasePackageName}${EntityPackageName}.${ClassName};

import java.util.List;
import com.github.pagehelper.PageInfo;

/**
 * Author ${Author}
 * Date  ${Date}
 */
public interface ${ClassName}Service {

	/**
	 * 新增
	 * 
	 * @param ${EntityName}
	 * @return
	 */
	Integer save(${ClassName} ${EntityName});

	/**
	 * 修改
	 * 
	 * @param ${EntityName}
	 * @return
	 */
	Integer update(${ClassName} ${EntityName});

	/**
	 * 删除记录
	 * 
	 * @param ${EntityName}
	 * @return
	 */
	Integer delete(${ClassName} ${EntityName});

	/**
	 * 查询记录
	 * 
	 * @param ${EntityName}
	 * @return
	 */
	${ClassName} query(${ClassName} ${EntityName});
	
	/**
	 * 查询列表
	 * 
	 * @param ${EntityName}
	 * @return
	 */
	List<${ClassName}> queryList(${ClassName} ${EntityName});

	/**
	 * 分页查询列表
	 * 
	 * @param page
	 * @param pageSize
	 * @param ${EntityName}
	 * @return
	 */
	PageInfo<${ClassName}> queryPageList(Integer page, Integer pageSize, ${ClassName} ${EntityName});

}
