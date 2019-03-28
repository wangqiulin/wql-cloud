package ${BasePackageName}${DaoPackageName};

import ${BasePackageName}${EntityPackageName}.${ClassName};
import org.apache.ibatis.annotations.Mapper;
import com.wql.cloud.basic.datasource.tk.MyMapper;
import java.util.List;

/**
 * Author ${Author}
 * Date  ${Date}
 */
public interface ${ClassName}Mapper extends MyMapper<${ClassName}> {

}