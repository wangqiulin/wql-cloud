package ${BasePackageName}${EntityPackageName};

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;
import java.math.BigDecimal;
import java.util.Date;
import java.sql.Time;
import java.sql.Timestamp;
import javax.persistence.Table;
import com.wql.cloud.basic.datasource.baseservice.BaseDO;

/**
 * Author ${Author}
 * Date  ${Date}
 */
@Setter
@Getter
@ToString
@Table(name="${TableName}")
public class ${ClassName} extends BaseDO implements Serializable {

    private static final long serialVersionUID = 1L;

    ${Properties}

    public ${ClassName}(){
    }

    ${Methods}
}