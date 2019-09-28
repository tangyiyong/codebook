package cn.bowseros.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;

import cn.bowseros.entity.People;
import codebook.commons.R;
import codebook.commons.ValueUtils;
import weibo4j.Oauth;
import weibo4j.Users;
import weibo4j.model.User;

/**
 *
 * Created by bowser Sep 19, 2019/11:12:52 AM
 * 
 */
@Controller
@RequestMapping("/oauth")
public class OAuthController extends BaseController{

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping("/weibo")
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Oauth oauth = new Oauth();
		String url = oauth.authorize("code", null);
		logger.info("url {}", url);
		response.sendRedirect(url);
		return null;
	}

	/**
	 * 新浪微博回调
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/check")
	public ModelAndView handleRequestCheck(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String code = request.getParameter("code");
		logger.info("code {}", code);
		if (ValueUtils.isEmpty(code)) {
			response.sendRedirect(request.getContextPath() + "/login");
			return null;
		}
		
		Oauth oauth = new Oauth();
		String token;
		try {
			token = oauth.getAccessTokenByCode(code).toString();
			String str[] = token.split(",");
			String accessToken = str[0].split("=")[1];
			String str1[] = str[3].split("]");
			String uid = str1[0].split("=")[1];
			Users users = new Users();
			users.client.setToken(accessToken);
			User weiboUser = users.showUserById(uid);
			String name = weiboUser.getScreenName();
			
			logger.info("JSON {}",JSON.toJSONString(weiboUser));
			People p = new People();
			p.setId(weiboUser.getIdstr());
			p.setUname(name);
			boolean extUser = peopleService.exitsId(p.getId());
			if (!extUser) {
				p.setCreateTime(new Date());
				R<String> r = peopleService.weiboNewPeople(p);
				if (r.getCode() != 200) {
					//创建用户数据失败
					response.sendRedirect(request.getContextPath() + "/login");
					return null;
				}
			}
			
			//获取用户信息
			p = peopleService.loadInfo(p.getId());
			p.setPasscode(null);
			p.setUavatar(weiboUser.getAvatarLarge());
			//weiboUser.getCreatedAt()//微博创建时间
			request.getSession().setAttribute("user_", p);
		} catch (Exception e1) {
			logger.error(e1.getMessage(),e1);
		}
		response.sendRedirect(request.getContextPath() + "/");
		return null;
	}
	
	/**
	 * GitHub回调
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 
	 * <a href="https://github.com/login/oauth/authorize?client_id=3a531f662cfeca397f7c" class="btn btn-sm btn-primary btn-block" id="githubLogin">
			<i class="fa fa-github" aria-hidden="true"></i>用Github账号登入
		</a>
	 */
	@RequestMapping("/github/check/off")
	public ModelAndView handleRequestRithubCheck(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//github回调 https://www.bowseros.cn/oauth/github/check
		//接口 https://github.com/login/oauth/authorize?client_id=
		//Client ID 3a531f662cfeca397f7c
		//Client Secret 6cb06a59a0b51687349ceb72bc2d96aa5ab8d717
		
		String code = request.getParameter("code");
		logger.info("code {}", code);
		if (ValueUtils.isEmpty(code)) {
			response.sendRedirect(request.getContextPath() + "/login");
			return null;
		}
		
		String accessTokenUrl = "https://github.com/login/oauth/access_token";
		Map<String,String> accessBody = new HashMap<String, String>();
		accessBody.put("client_id", "3a531f662cfeca397f7c");
		accessBody.put("client_secret", "6cb06a59a0b51687349ceb72bc2d96aa5ab8d717");
		accessBody.put("code", code);
		String tokenBody = restTemplate.postForObject(accessTokenUrl, accessBody, String.class);
		//access_token=e72e16c7e42f292c6912e7710c838347ae178b4a&token_type=bearer
		
		String token = tokenBody.split("&")[0].replaceAll("access_token=", "");
		String tokenType = tokenBody.split("&")[1].replaceAll("token_type=", "");
		
		
		//https://api.github.com/user
		String userUrl = "https://api.github.com/user";
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.add("access_token", token); 
		requestHeaders.add("token_type", tokenType);
		requestHeaders.add("scope", "repo,gist");
		HttpEntity<Object> requestEntity = new HttpEntity<Object>(null, requestHeaders);
		
		ResponseEntity<Map> responseEnt = restTemplate.exchange(userUrl, HttpMethod.POST, requestEntity, Map.class);
		Map userInfo = responseEnt.getBody();
		logger.info("github JSON {}",JSON.toJSONString(userInfo));
		
		response.sendRedirect(request.getContextPath() + "/");
		return null;
	}
	
}
