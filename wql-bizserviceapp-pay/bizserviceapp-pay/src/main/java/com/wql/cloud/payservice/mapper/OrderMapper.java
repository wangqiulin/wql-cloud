package com.wql.cloud.payservice.mapper;

import com.wql.cloud.payservice.pojo.domain.Order;
import org.apache.ibatis.annotations.Mapper;
import com.wql.cloud.basic.datasource.tk.MyMapper;
import java.util.List;

/**
 * Author wangqiulin
 * Date  2019-04-13
 */
public interface OrderMapper extends MyMapper<Order> {

}