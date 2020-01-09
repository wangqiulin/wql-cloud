package com.wql.cloud.basic.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.wql.cloud.basic.security.provider.MyAuthenticationProvider;

/**
 * TODO Spring Security 配置
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	/**登录链接*/
	@Value("${login.path:/user/login}")
    private String loginPath;

    @Value("${business.whitelist:192.168.1.93}")
    private String[] whitelist;

	/**405 错误处理*/
	@Autowired
	private MyAuthenticationEntryPoint unauthorizedHandler;
	
	/**访问拒绝处理*/
	@Autowired
	private MyAccessDeniedHandler accessDeniedHandler;

	/**自定义认证*/
	@Autowired
	private MyAuthenticationProvider authenticationProvider;
	
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    	// 使用自定义登录身份认证组件
    	auth.authenticationProvider(authenticationProvider);
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
				// 使用JWT，不需要csrf
				.csrf().disable()
				// 认证失败处理、无权访问失败处理
				.exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
				.accessDeniedHandler(accessDeniedHandler).and()
				// 基于Token，不使用Session
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				// 请求授权
				.authorizeRequests()
				// 允许对于网站静态资源的无授权访问
				.antMatchers(HttpMethod.GET,
						"/",
						"/*.html",
						"/favicon.ico",
						"/**/*.html",
						"/**/*.vue",
						"/**/*.css",
						"/**/*.js",
						"/**/*.js.map",
						"/**/*.svg",
						"/**/fonts/*.*",
						"/**/*.png",
						"/error",
						"/hystrix",
						"/env")
				.permitAll()
				// 允许所有OPTIONS请求
				.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
				// 授权api放开
				.antMatchers(new String[]{loginPath}).permitAll()
				// 白名单放开
				.antMatchers(whitelist).permitAll()
                // 公共资源放开
                .antMatchers(this.queryPublicResource()).permitAll()
				//swagger ui
				.antMatchers(HttpMethod.GET, "/swagger**/**", "/webjars/**", "/v2/**").permitAll()
				// 禁用其他链接（需要认证）
				.anyRequest().authenticated();
		// 开启登录认证流程过滤器
		httpSecurity.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class)
        //添加跨域支持
        .cors();
		// 禁用缓存
		httpSecurity.headers().cacheControl();
	}

	
	@Bean
	public MyAuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
		return new MyAuthenticationTokenFilter();
	}
	
	
	
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * 获取公共资源配置
     * @return
     */
    private String[] queryPublicResource(){
        try {
            //TODO 查询公共资源
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String[0];
    }


}
