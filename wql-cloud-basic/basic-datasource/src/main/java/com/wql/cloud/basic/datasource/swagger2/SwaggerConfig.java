package com.wql.cloud.basic.datasource.swagger2;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 在生产环境下，我们需要关闭swagger配置，避免暴露接口的这种危险行为。
 *	禁用方法1：使用注解@Profile({"dev","test"}) 表示在开发或测试环境开启，而在生产关闭。（推荐使用）
	禁用方法2：使用注解@ConditionalOnProperty(name = "swagger.enable", havingValue = "true") 
			   然后在测试配置或者开发配置中 添加 swagger.enable = true 即可开启，生产环境不填则默认关闭Swagger.
 *
 * @author wangqiulin
 * @date 2018年5月12日
 */
@Configuration
@EnableSwagger2
@ConditionalOnExpression("${swagger.enabled:false}")
public class SwaggerConfig {

	@Value("${swagger.controller.location:}")
	private String controllerLocation;
	
	@Value("${swagger.enabled:false}") 
	private Boolean enabled;
	
	@Lazy
	@Bean
    public Docket createRestApi() {
		//添加head参数start
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        tokenPar.name("Authorization").description("登录令牌").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        pars.add(tokenPar.build());
        //添加head参数end
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .enable(enabled) //是否开启swagger
                .select()
                .apis(RequestHandlerSelectors.basePackage(controllerLocation))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(pars);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("API接口")
                .build();
    }
	
}
