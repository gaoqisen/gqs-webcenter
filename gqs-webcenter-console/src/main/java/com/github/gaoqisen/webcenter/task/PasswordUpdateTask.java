package com.github.gaoqisen.webcenter.task;

import com.alibaba.fastjson.JSONObject;
import com.github.gaoqisen.webcenter.constant.RedisKeyConstant;
import com.github.gaoqisen.webcenter.service.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@EnableScheduling
@Component
public class PasswordUpdateTask {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Autowired
	private SysUserService sysUserService;

	private static Boolean isRuning = false;

	public void beginProcess() {
		while (true) {
			String passwordInfo = stringRedisTemplate.opsForList().rightPop(RedisKeyConstant.PASSWORDSET, 10,
					TimeUnit.SECONDS);
			if (StringUtils.isBlank(passwordInfo)) {
				return;
			}
			try {
				JSONObject jsonObject = JSONObject.parseObject(passwordInfo);
				sysUserService.updatePassword(jsonObject.getInteger("userId"), jsonObject.getString("password"),
						jsonObject.getString("newPassword"));
				logger.info("Redis消费修改密码定时.");
			}
			catch (Exception e) {
				logger.error("密码修改失败: " + passwordInfo);
				return;
			}

		}
	}

	@Scheduled(cron = "${webcenter.scheduled.cron}")
	public void handlePasswordUpdate() {
		if (isRuning) {
			return;
		}
		isRuning = true;
		beginProcess();
		isRuning = false;
	}

}
