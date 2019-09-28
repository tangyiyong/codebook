package cn.bowseros.service;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import cn.bowseros.config.ListItem;
import cn.bowseros.entity.People;
import codebook.commons.R;
import codebook.commons.SHA;
import codebook.commons.UserCheckUtils;
import codebook.commons.ValueUtils;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;

/**
 *
 * Created by bowser Sep 7, 2019/4:38:57 PM
 * 
 */
@Service
public class PeopleServiceImple extends BaseService implements PeopleService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public String check(People people) {
		if (people == null) {
			return "操作失败";
		}

		if (ValueUtils.isEmpty(people.getPasscode()) || ValueUtils.isEmpty(people.getUname())) {
			return "用户名或密码不正确";
		}

		QueryWrapper<People> queryWrapper = new QueryWrapper<People>();
		queryWrapper.eq("uname", people.getUname());
		People p = peopleMapper.selectOne(queryWrapper);
		if (p == null) {
			return "请检测用户名是否正确";
		}

		// 通行证异常
		if (p.getUpass() == 1) {
			return p.getUpassmessage() + ",有任何问题请邮件管理员[" + adminEmail + "]";
		}

		String pcode = p.getPasscode();
		if (ValueUtils.isEmpty(pcode)) {
			return "密码不正确";
		}

		if (!pcode.equals(SHA.getResult(people.getPasscode()))) {
			return "密码不正确";
		}

		p.setPasscode("");
		p.setUavatar(
				"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAHgAAAB4CAIAAAC2BqGFAAABAElEQVR42u3cWw3AIBBFQYRWHRYwVgn1wKPZ284JAmDC3wbarVdqCECDFmjQoAUatECHQvdxTa9EjnPnBQ0aNGjQoEGDBg0aNGjQoEGDBg0aNGjQoEGDBg06E7rmphMvB2jQoEGDBg0aNGjQoEGDBg0aNGjQoEGDBg0aNGjQmdArm06EjpyCgwYNGjRo0KBBgwYNGjRo0KBBgwYNGjRo0KBBgwZd9J3h9wasRafgoEGDBg0aNGjQoEGDBg0aNGjQoEGDBg0aNGjQoEEfhPYJrN92QYMGDRo0aNCgQYMGDRo0aNCgQYMGDRo0aNCgQf8bWrsCDRq0QIMGjQA0aIEu2wORd/dHtznabwAAAABJRU5ErkJggg==");
		session.setAttribute("user_", p);

		String umail = p.getMyemail();
		// 登陆邮件提醒
		if (p.getLoginReminder() == 1 && !ValueUtils.isEmpty(umail)) {
			UserAgent ua = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
			Browser browser = ua.getBrowser();
			OperatingSystem os = ua.getOperatingSystem();
			String remoteAddr = request.getRemoteAddr(); // 请求IP

			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper;
			try {
				helper = new MimeMessageHelper(mimeMessage, true);
				helper.setFrom(efrom);
				helper.setTo(umail);
				helper.setSubject("代码笔记登陆提醒");

				String text = "此次登入信息:";
				text += "<p>登入IP地址: " + remoteAddr + "</p>";
				text += "<p>浏览器信息: " + browser.toString() + "</p>";
				text += "<p>操作系统: " + os.toString() + "</p>";
				text += "<p>时间: " + new Date() + "</p>";
				helper.setText("<html><body>" + text + "</body></html>", true);

				mailSender.send(mimeMessage);
			} catch (MessagingException e) {
				logger.error("登陆提醒邮件发送异常," + e.getMessage(), e);
			}
		}
		return "success";
	}

	@Transactional
	@Override
	public String newPeople(People people) {
		String ip_ = request.getRemoteAddr();
		if (ListItem.toDayRegisterList != null) {
			if (ListItem.toDayRegisterList.contains(ip_)) {
				return "抱歉,最近一小时内您已经注册过了!";
			}
		}

		// 空校验
		if (people == null) {
			return "操作失败";
		}

		String ucode = UserCheckUtils.replaceBlank(people.getUcode());// 邀请码
		String uname = UserCheckUtils.replaceBlank(people.getUname());// 用户名
		String passcode = UserCheckUtils.replaceBlank(people.getPasscode());// 密码

		if (ValueUtils.isEmpty(ucode) || ValueUtils.isEmpty(uname) || ValueUtils.isEmpty(passcode)) {
			return "参数非法!";
		}

		// 邀请码校验
		if (!ucode.equalsIgnoreCase(SHA.yqCode(uname))) {
			return "邀请码无效";
		}

		// 用户名规则校验
		if (!uname.matches("[a-zA-Z0-9_]*")) {
			return "用户名只能由字母,数字,下划线组成";
		}
		if (uname.length() > 15 || uname.length() < 6) {
			return "用户名长度最短为6最长15";
		}

		// 密码规则校验
		if (passcode.length() > 16 || passcode.length() < 6) {
			return "密码长度最短为6最长为16";
		}

		QueryWrapper<People> queryWrapper = new QueryWrapper<People>();
		queryWrapper.eq("uname", people.getUname().trim());
		People p = peopleMapper.selectOne(queryWrapper);
		if (p != null) {
			return "用户名不可用";
		}

		people.setId(String.valueOf(gid.nextId()));
		people.setUname(people.getUname().trim());
		people.setPasscode(SHA.getResult(people.getPasscode()));
		people.setCreateTime(new Date());
		people.setMyemail(people.getUemail());
		int r = peopleMapper.insert(people);

		// 保存今日已经注册的IP
		ListItem.toDayRegisterList.add(ip_);
		return r > 0 ? "success" : "创建失败,请稍后再试!";
	}

	/**
	 * 更新个人信息
	 */
	@Transactional
	@Override
	public R<String> update(People p) {
		People pep = (People) session.getAttribute("user_");
		if (pep == null) {
			return new R<>(766, "未登录");
		}

		People thipep = peopleMapper.selectById(pep.getId());
		if (thipep == null) {
			return new R<>(777, "操作失败");
		}

		thipep.setMyemail(p.getMyemail());
		thipep.setMygitee(p.getMygitee());
		thipep.setMygithub(p.getMygithub());
		thipep.setMyhome(p.getMyhome());
		int r = peopleMapper.updateById(thipep);
		return r > 0 ? R.SUCCESS : R.FAIL;
	}

	/**
	 * 更新个人信息
	 */
	@Override
	public R<People> myInfoget() {
		People pep = (People) session.getAttribute("user_");
		if (pep == null) {
			return new R<>(766, "未登录");
		}

		People thipep = peopleMapper.selectById(pep.getId());
		return new R<People>(thipep);
	}

	public static ConcurrentHashMap<String, Integer> mailCode = new ConcurrentHashMap<String, Integer>();
	/** 邮箱服务 */
	@Value("${spring.mail.username}")
	protected String efrom;
	@Autowired
	protected JavaMailSender mailSender;

	/**
	 * 邮箱验证
	 */
	@Override
	public R<String> mailValidation() {
		People pep = (People) session.getAttribute("user_");
		if (pep == null) {
			return new R<>(766, "未登录");
		}
		People thipep = peopleMapper.selectById(pep.getId());
		int code = (int) ((Math.random() * 9 + 1) * 1000);

		SimpleMailMessage mlm = new SimpleMailMessage();
		mlm.setFrom(efrom);
		mlm.setTo(thipep.getMyemail());
		mlm.setSubject("代码笔记邮箱验证码");
		mlm.setText("验证码：" + code);
		// 发送邮件
		mailSender.send(mlm);
		mailCode.put(thipep.getId(), code);

		logger.info("已向{}发送邮箱验证码{}", thipep.getMyemail(), code);
		return R.SUCCESS;
	}

	/**
	 * 更新密码
	 */
	@Transactional
	@Override
	public R<String> updatePassword(People p) {
		People pep = (People) session.getAttribute("user_");
		if (pep == null) {
			return new R<>(766, "未登录");
		}
		if (p == null) {
			return new R<>(97, "操作失败");
		}

		if (ValueUtils.isEmpty(p.getOldpasscode()) || ValueUtils.isEmpty(p.getNewpasscode())) {
			return new R<>(330, "密码输入有误");
		}
		if (p.getOldpasscode().length() > 16 || p.getOldpasscode().length() < 6) {
			return new R<>(330, "密码长度最短为6最长为16");
		}
		if (p.getNewpasscode().length() > 16 || p.getNewpasscode().length() < 6) {
			return new R<>(330, "密码长度最短为6最长为16");
		}

		People thipep = peopleMapper.selectById(pep.getId());
		if (thipep == null) {
			return new R<>(97, "操作失败");
		}
		// w微博用户密码为空
		String passcode = thipep.getPasscode();
		if (ValueUtils.isEmpty(passcode)) {
			return new R<>(811, "微博用户无需变更密码");
		}

		String oldpasscode = SHA.getResult(p.getOldpasscode());
		if (!passcode.equals(oldpasscode)) {
			return new R<>(331, "旧密码输入有误");
		}

		thipep.setPasscode(SHA.getResult(p.getNewpasscode()));
		int r = peopleMapper.updateById(thipep);

		return r > 0 ? R.SUCCESS : R.FAIL;
	}

	/**
	 * 系统设置更新
	 */
	@Transactional
	@Override
	public R<String> settingUpdate(People p) {
		People pep = (People) session.getAttribute("user_");
		if (pep == null) {
			return new R<>(766, "未登录");
		}

		People thipep = peopleMapper.selectById(pep.getId());
		if (thipep == null) {
			return new R<>(97, "操作失败");
		}

		thipep.setOpenshare(p.getOpenshare());
		thipep.setAllowdelete(p.getAllowdelete());
		thipep.setLoginReminder(p.getLoginReminder());
		int r = peopleMapper.updateById(thipep);
		return r > 0 ? R.SUCCESS : R.FAIL;
	}

	/**
	 * 用户名是否存在
	 */
	@Override
	public boolean exits(String username) {
		QueryWrapper<People> queryWrapper = new QueryWrapper<People>();
		queryWrapper.eq("uname", username);
		int userCount = peopleMapper.selectCount(queryWrapper);
		return userCount > 0;
	}

	/**
	 * 邮箱是否存在
	 */
	@Override
	public boolean exitsEmail(String email) {
		QueryWrapper<People> queryWrapper = new QueryWrapper<People>();
		queryWrapper.eq("myemail", email);
		int myemailCount = peopleMapper.selectCount(queryWrapper);
		return myemailCount > 0;
	}

	@Override
	public boolean exitsId(String id) {
		QueryWrapper<People> queryWrapper = new QueryWrapper<People>();
		queryWrapper.eq("id", id);
		int userCount = peopleMapper.selectCount(queryWrapper);
		return userCount > 0;
	}

	/**
	 * weibo登陆用户数据同步
	 */
	@Transactional
	@Override
	public R<String> weiboNewPeople(People p) {
		int r = peopleMapper.insert(p);
		return r > 0 ? R.SUCCESS : R.FAIL;
	}

	@Override
	public People loadInfo(String id) {
		return peopleMapper.selectById(id);
	}

	/**
	 * 更新背景图
	 */
	@Override
	public R<String> updateBackground(People p) {
		People pep = (People) session.getAttribute("user_");
		if (pep == null) {
			return new R<>(766, "未登录");
		}

		People thipep = peopleMapper.selectById(pep.getId());
		if (thipep == null) {
			return new R<>(97, "操作失败");
		}

		String bgt = p.getUbackground();
		if (ValueUtils.isEmpty(bgt)) {
			return new R<>(97, "操作失败,没有更新背景图～");
		}
		thipep.setUbackground(bgt);
		int r = peopleMapper.updateById(thipep);
		return r > 0 ? R.SUCCESS : R.FAIL;
	}

}
