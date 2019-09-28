package cn.bowseros.config;

import javax.servlet.Filter;
import javax.servlet.Servlet;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;

/**
 *
 * Created by bowser Jul 15, 2019/10:22:08 AM
 * 
 */
@Component
public class DruidMonitorConfig {

	/**
	 * 注册ServletRegistrationBean
	 * 
	 * @return
	 */
	@Bean
	public ServletRegistrationBean<Servlet> registrationBean() {
		ServletRegistrationBean<Servlet> bean = new ServletRegistrationBean<Servlet>(new StatViewServlet(), "/druid/*");
		/** 初始化参数配置，initParams **/
		// 白名单
		bean.addInitParameter("allow", "127.0.0.1");// 多个ip逗号隔开
		// IP黑名单 (存在共同时，deny优先于allow) : 如果满足deny的话提示:Sorry, you are not permitted to
		// view this page.
		// bean.addInitParameter("deny", "192.168.1.73");
		// 登录查看信息的账号密码.
		bean.addInitParameter("loginUsername", "admin");
		bean.addInitParameter("loginPassword", "admin");
		bean.addInitParameter("resetEnable", "false");// 是否能够重置数据.
		return bean;
	}

	/**
	 * 注册FilterRegistrationBean
	 * 
	 * @return
	 */
	@Bean
	public FilterRegistrationBean<Filter> druidStatFilter() {
		FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<Filter>(new WebStatFilter());
		bean.addUrlPatterns("/*");// 添加过滤规则.
		bean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");// 添加不需要忽略的格式信息.
		return bean;
	}
}
