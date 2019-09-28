package cn.bowseros.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import codebook.commons.SimpleBloomFilter;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;

/**
 *
 * Created by bowser Aug 19, 2019/1:47:00 PM
 * 
 */
@Component
public class SessionInterceptorAdapter extends HandlerInterceptorAdapter {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public static void filterRefresh() {
		ListItem.accfilter = null;
		ListItem.accfilter = new SimpleBloomFilter();
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		String UserAgentStr = request.getHeader("User-Agent");
		UserAgent ua = UserAgent.parseUserAgentString(UserAgentStr);
		Browser browser = ua.getBrowser();// 浏览器
		OperatingSystem os = ua.getOperatingSystem();// 操作系统
		DeviceType dt = os.getDeviceType();
		String remoteAddr = request.getRemoteAddr(); // 请求IP

		StringBuffer sbf = new StringBuffer();
		sbf.append("ip:");
		sbf.append(remoteAddr);
		sbf.append(" browser:");
		sbf.append(browser);
		sbf.append(" os:");
		sbf.append(os);
		sbf.append(" DeviceType:");
		sbf.append(getDevicetype(dt, UserAgentStr));

		// 系统未知 UNKNOWN
		// 浏览器未知可以接受browser.toString().equalsIgnoreCase("UNKNOWN") ||
		// 防御后可能搜索引擎无法收录
//		if (os.toString().equalsIgnoreCase("UNKNOWN") || browser.toString().equalsIgnoreCase("BOT")) {
//			logger.info("系统未知,访问拒绝 {}", sbf.toString());
//			response.sendRedirect(request.getContextPath() + "/access/rejection");
//			return false;
//		}

		if (ListItem.accfilter != null) {
			if (!ListItem.accfilter.contains(sbf.toString())) {
				ListItem.accfilter.add(sbf.toString());
				logger.info(sbf.toString());
			}
		}
		return super.preHandle(request, response, handler);
	}

	public static String getDevicetype(DeviceType deviceType, String agentString) {
		switch (deviceType) {
		case COMPUTER:
			return "PC";
		case TABLET: {
			if (agentString.contains("Android"))
				return "Android Pad";
			if (agentString.contains("iOS"))
				return "iPad";
			return "Unknown";
		}
		case MOBILE: {
			if (agentString.contains("Android"))
				return "Android";
			if (agentString.contains("iOS"))
				return "IOS";
			return "Unknown";
		}
		default:
			return "Unknown";
		}
	}
}
