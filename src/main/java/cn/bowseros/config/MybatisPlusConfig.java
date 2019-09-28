package cn.bowseros.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;

/**
 *
 * Created by bowser Jul 16, 2019/8:29:52 PM
 * 
 */
@EnableTransactionManagement
@Configuration
@MapperScan("cn.bowseros.mapper")
public class MybatisPlusConfig {

	/**
	 * 分页插件
	 */
	@Bean
	public PaginationInterceptor paginationInterceptor() {
		return new PaginationInterceptor();
	}

	/**
	 * mybatis-plus SQL执行效率插件【生产环境可以关闭】
	 */
//	@Bean
//	public PerformanceInterceptor performanceInterceptor() {
//		PerformanceInterceptor pi = new PerformanceInterceptor();
//		//pi.setMaxTime(200);//SQL 执行最大时长，超过自动停止运行，有助于发现问题。
//		//pi.setFormat(false);//SQL 是否格式化
//		//pi.setWriteInLog(true);//是否写入日志文件, true 写入日志文件，不阻断程序执行！超过设定的最大执行时长异常提示！
//		return pi;
//	}
}
