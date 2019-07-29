package com.wql.cloud.userservice.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.wql.cloud.basic.datasource.tk.MyMapper;
import com.wql.cloud.userservice.pojo.domain.User;

/**
 * 
 * @author wangqiulin
 *
 */
public interface UserMapper extends MyMapper<User>{
	
	/**
	 * id 从0开始，limit表示一次多少条
	 * 
	 * @param id
	 * @param limit
	 * @return
	 */
	@Select({
		"select id from t_user where id > #{id} limit #{limit}"
	})
	List<User> pageById(@Param("id")Long id, @Param("limit")Integer limit);

}
