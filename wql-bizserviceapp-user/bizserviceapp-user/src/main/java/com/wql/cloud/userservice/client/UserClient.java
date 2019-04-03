package com.wql.cloud.userservice.client;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.wql.cloud.basic.datasource.response.constant.DataResponse;
import com.wql.cloud.userservice.pojo.domain.User;

/**
 * FeignClient接口，不能使用@GettingMapping 之类的组合注解
 * FeignClient接口中，如果使用到@PathVariable ，必须指定其value
 * 
 * 
 * 1：多客户端时，feign接口抽取到公共jar中，此时，客户端的启动类上需要对该jar中feign所在的包进行扫描，
 * 要在spring和feign中同时注册，否则启动时会报：“Consider defining a bean of type '******Feign'
 * in your configuration.”
 * 
 * 2.使用Fegin传值时，GET变POST
 * feign在传递时默认会将数据放在RequestBody中，所以会导致默认使用POST请求（及时@RequestMapping写着GET也没用），
 * 此时需要在参数列表中声明@RequestParam才能进行正常的GET请求。
 * 
 * 3：feign请求返回复杂对象时
 * 当请求返回的是DataResponse的一个对象时，对于该对象内部的data值，会变成一个linkedHashMap，并不会被转换成相应的类对象，若直接强转会报类型错误。
 * 
 * 
 * 
 * @author wangqiulin
 *
 */
@FeignClient(value = "${feign.serviceId.user}", fallback = UserClientHystrix.class)
public interface UserClient {

	@RequestMapping(value = "/user/queryList", method = RequestMethod.POST)
	DataResponse<List<User>> queryList(@RequestBody User req);

}
