package com.wql.cloud.payservice.pojo.domain;

import java.io.Serializable;
import java.util.List;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import javax.persistence.Table;
import com.wql.cloud.basic.datasource.baseservice.BaseDO;

/**
 * Author wangqiulin
 * Date  2019-04-13
 */
@Table(name="t_order")
public class Order extends BaseDO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String orderNo;
    private String goodsName;


    public Order(){
    }

    public void setOrderNo (String orderNo) {this.orderNo = orderNo;} 
    public String getOrderNo(){ return orderNo;} 
    public void setGoodsName (String goodsName) {this.goodsName = goodsName;} 
    public String getGoodsName(){ return goodsName;} 

}