package ${BasePackageName}${ControllerPackageName};

import ${BasePackageName}${EntityPackageName}.${ClassName};
import ${BasePackageName}${ServicePackageName}.${ClassName}Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.wql.cloud.basic.datasource.response.constant.BusinessEnum;
import com.wql.cloud.basic.datasource.response.constant.DataResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Author ${Author}
 * Date  ${Date}
 */
@RestController
public class ${ClassName}Controller {

    @Autowired
    private ${ClassName}Service ${EntityName}Service;

	@ApiOperation(value = "新增")
	@PostMapping("/${EntityName}/save")
	public DataResponse save(@RequestBody ${ClassName} ${EntityName}) {
		return ${EntityName}Service.save(${EntityName}) == 1 ? new DataResponse(BusinessEnum.SUCCESS) : new DataResponse(BusinessEnum.FAIL);
	}
	
	
	@ApiOperation(value = "修改")
	@PostMapping("/${EntityName}/update")
	public DataResponse update(@RequestBody ${ClassName} ${EntityName}) {
		return ${EntityName}Service.update(${EntityName}) > 0 ? new DataResponse(BusinessEnum.SUCCESS) : new DataResponse(BusinessEnum.FAIL);
	}
	
	
	@ApiOperation(value = "删除")
	@PostMapping("/${EntityName}/delete")
	public DataResponse delete(@RequestBody ${ClassName} ${EntityName}) {
		return ${EntityName}Service.delete(${EntityName}) > 0 ? new DataResponse(BusinessEnum.SUCCESS) : new DataResponse(BusinessEnum.FAIL);
	}
	
	
	@ApiOperation(value = "查询列表")
	@PostMapping("/${EntityName}/queryList")
	public DataResponse queryList(@RequestBody ${ClassName} ${EntityName}) {
		DataResponse dr = new DataResponse(BusinessEnum.SUCCESS);
		dr.setData(${EntityName}Service.queryList(${EntityName}));
		return dr;
	}
	
	
	@ApiOperation(value = "分页查询列表")
	@PostMapping("/${EntityName}/queryPageList")
	public DataResponse queryPageList(@RequestBody ${ClassName} ${EntityName}) {
		DataResponse dr = new DataResponse(BusinessEnum.SUCCESS);
		dr.setData(${EntityName}Service.queryPageList(${EntityName}.getPage(), ${EntityName}.getPageSize(), ${EntityName}));
		return dr;
	}
	
	
	@ApiOperation(value = "查询记录")
	@PostMapping("/${EntityName}/query")
	public DataResponse query(@RequestBody ${ClassName} ${EntityName}) {
		DataResponse dr = new DataResponse(BusinessEnum.SUCCESS);
		dr.setData(${EntityName}Service.query(${EntityName}));
		return dr;
	}
	
}
