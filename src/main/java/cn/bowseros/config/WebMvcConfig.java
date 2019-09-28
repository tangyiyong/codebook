package cn.bowseros.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *
 * Created by bowser Aug 12, 2019/12:00:47 PM
 * 
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer{

	@Autowired
	private SessionInterceptorAdapter sessionInterceptorAdapter;
	@Autowired
	private InputDataInterceptorAdapter inputDataInterceptorAdapter;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		List<String> sessionInterceptorAdapterp = new ArrayList<>();
		sessionInterceptorAdapterp.add("/login/**");//静态资源
		sessionInterceptorAdapterp.add("/static/**");//静态资源
		sessionInterceptorAdapterp.add("/share/view/**");//查看分享笔记内容
		sessionInterceptorAdapterp.add("/mails/**");//邮件服务
		sessionInterceptorAdapterp.add("/nodebook/content/**");//查看分享笔记内容
		sessionInterceptorAdapterp.add("/oauth/**");//第三方登陆
		sessionInterceptorAdapterp.add("/access/rejection");//访问拒绝
		
		registry.addInterceptor(sessionInterceptorAdapter)
				.addPathPatterns("/**")
				.excludePathPatterns(sessionInterceptorAdapterp);
		
		registry.addInterceptor(inputDataInterceptorAdapter)
				.addPathPatterns("/login/newpeople")//注册用户
				.addPathPatterns("/login/check")//用户登陆校验
				.addPathPatterns("/nodebook/mysetting/update")//更新个人设置
				.addPathPatterns("/nodebook/setting/update")//更新系统设置
				.addPathPatterns("/nodebook/mysetting/background/update")//更新背景图
				.addPathPatterns("/nodebook/mysetting/passcode/update")//更新登陆密码
				.addPathPatterns("/nodebook/mysetting/mail/validation")//邮箱验证
				.addPathPatterns("/nodebook/insert")//新增笔记
				.addPathPatterns("/nodebook/insert/group")//新增笔记分组
				.addPathPatterns("/nodebook/delete")//删除笔记
				.addPathPatterns("/mails/getucode")//邀请码获取
				.addPathPatterns("/keeplink/update");//新增收藏链接
	}
	
	
}
