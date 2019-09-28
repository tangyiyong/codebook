package cn.bowseros.config;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;

import cn.bowseros.entity.People;
import codebook.commons.DateTimeUtils;
import codebook.commons.R;

/**
 *
 * Created by bowser Aug 12, 2019/5:48:39 PM
 * 
 */
@Component
public class InputDataInterceptorAdapter extends HandlerInterceptorAdapter {

	private Logger logger = LoggerFactory.getLogger(InputDataInterceptorAdapter.class);

	/**
	 * 令牌桶
	 * 
	 * @return
	 */
	private static long time = System.currentTimeMillis();
	// 流速
	private static int createTokenRate = 3;
	// 桶大小
	private static int sizeToken = 10;
	// 当前令牌数
	private static int tokens = 0;

	private static boolean tokenBucket() {
		long now = System.currentTimeMillis();
		int in = (int) ((now - time) * createTokenRate);
		tokens = Math.min(sizeToken, tokens + in);
		time = now;
		if (tokens > 0) {
			--tokens;
			return true;
		} else {
			return false;
		}
	}
	
	//上一次操作时间
	public static ConcurrentHashMap<String, Long> nextAccTime = new ConcurrentHashMap<String, Long>();
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		String method = request.getMethod();
		String ip_ = request.getRemoteAddr();
		String Uri = request.getRequestURI();
		logger.info("Ip:{} Method:{} URI:{}", ip_, method, Uri);
		
		// 限制并发流量控制
		boolean pass = tokenBucket();
		if (!pass) {
			logger.info("触发了极限值 Ip:{} Method:{} URI:{}", ip_, method, Uri);
			if (jsJson(handler)) {
				//记住这个IP，它触发了极限值，将它限制
				isJson(request, response, handler, "网络繁忙,请稍后重试!");
				return false;
			}
		}
		
		// 此次操作必须在上一次操作五秒之后
		Long optime = nextAccTime.get(ip_);
		if (optime != null) {
			long now = System.currentTimeMillis();
			long second = DateTimeUtils.SECOND(optime, now);
			if (second < 3) {
				logger.info("操作太频繁 Ip:{} Method:{} URI:{}", ip_, method, Uri);
				isJson(request, response, handler, "操作太频繁");
				//刷新操作时间,必须等待完成5秒
				nextAccTime.put(ip_, System.currentTimeMillis());
				return false;
			}
		}
		
		// 限制访问的ip检查
//		if (notAccessList.contains(ip_)) {
//			if (jsJson(handler)) {
//				isJson(request, response, handler, "拒绝访问");
//				notAccessList.add(ip_);
//				return false;
//			}
//		}
		
		// 新用户操作时间等待
		People p = (People) request.getSession().getAttribute("user_");
		if (p != null) {
			int time = 600;
			long userRegisterTime = p.getCreateTime().getTime();
			long now = System.currentTimeMillis();
			long second = DateTimeUtils.SECOND(userRegisterTime, now);
			if (second < time) {
				if (method.equalsIgnoreCase("POST")) {
					HandlerMethod handlerMethod = (HandlerMethod) handler;
					if (jsJson(handlerMethod)) {
						isJson(request, response, handler, "新用户距离可操作还有" + (time - second) + "秒");
						return false;
					}
				}
			}
		}
		
		//刷新操作时间
		nextAccTime.put(ip_, System.currentTimeMillis());
		return true;
	}

	public boolean jsJson(Object handler) {
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		Method qmethod = handlerMethod.getMethod();
		ResponseBody rb = qmethod.getAnnotation(ResponseBody.class);
		return rb != null;
	}

	public void isJson(HttpServletRequest request, HttpServletResponse response, Object handler, String msg) {
		if (jsJson(handler)) {
			try {
				response.setContentType("application/json;charset=utf-8");
				response.getWriter().append(JSON.toJSONString(new R<>(998, msg)));
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	public static void main(String[] args) throws InterruptedException {
		AtomicInteger a = new AtomicInteger();
		AtomicInteger b = new AtomicInteger();
		for (int i = 0; i < 100; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					if (tokenBucket()) {
						a.getAndIncrement();
					} else {
						b.getAndIncrement();
					}
				}
			}).start();
		}
		
		TimeUnit.SECONDS.sleep(2);
		
		System.out.println(a);
		System.out.println(b);
	}
	
	
//	public void notJson(HttpServletRequest request, HttpServletResponse response, Object handler, String msg) {
//
//	}
//
////	if(method.equalsIgnoreCase("POST")) {
////		HandlerMethod handlerMethod = (HandlerMethod) handler;
////        Method qmethod = handlerMethod.getMethod();
////		ResponseBody rb = qmethod.getAnnotation(ResponseBody.class);
////		if (rb != null) {
////			
////			// not login
//////			if (p == null) {
//////				try {
//////					response.setContentType("application/json;charset=utf-8");
//////					response.getWriter().append(JSON.toJSONString(new R<>(766, "未登录")));
//////				} catch (IOException e) {
//////					logger.error(e.getMessage(), e);
//////				}
//////				return false;
//////			}
////			//json响应
////			
////		}else {
//////			if (p == null) {
//////				response.sendRedirect(request.getContextPath() +"/login");
//////				return false;
//////			}
////			//非json
////			//   /access/rejection 拒绝访问接口
////			
////		}
////	}
////	public static void main(String[] args) throws InterruptedException {
//////		long s = System.currentTimeMillis();
//////		Calendar cal = Calendar.getInstance();
//////		cal.setTimeInMillis(s);
//////		
//////		int hour = cal.get(Calendar.HOUR);
//////		int munute = cal.get(Calendar.MINUTE);
//////		
//////		System.out.println(hour+":"+munute);
////		// TimeUnit.SECONDS.sleep(timeout);
////		// long e = System.currentTimeMillis();
////		// System.out.println((e - s) / 1000);
////		long s = System.currentTimeMillis();
////		TimeUnit.SECONDS.sleep(3);
////		long e = System.currentTimeMillis();
////		System.out.println(DateTimeUtils.SECOND(s, e));
////	}
//
////	public static long SECOND(long s, long e) {
////		Calendar scal = Calendar.getInstance();
////		scal.setTimeInMillis(e - s);
////		return scal.get(Calendar.SECOND);
////	}
	
}
