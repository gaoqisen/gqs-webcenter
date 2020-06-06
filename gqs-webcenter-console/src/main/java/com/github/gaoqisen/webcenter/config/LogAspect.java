package com.github.gaoqisen.webcenter.config;

import com.alibaba.fastjson.JSON;
import com.github.gaoqisen.webcenter.entity.SysLog;
import com.github.gaoqisen.webcenter.entity.SysUser;
import com.github.gaoqisen.webcenter.service.SysLogService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * @ClassName LogAspect
 * @Author gaoqisen
 * @Date 2019-07-12
 * @Version 1.0
 */
@Aspect
@Component
public class LogAspect {

	@Autowired
	private SysLogService sysLogService;

	@Pointcut("@annotation(io.swagger.annotations.ApiOperation)")
	public void pointcut() {
	}

	@Around("pointcut()")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		Object result = null;
		long beginTime = System.currentTimeMillis();
		// 执行方法
		result = point.proceed();
		// 执行时长(毫秒)
		long time = System.currentTimeMillis() - beginTime;
		// 保存日志
		saveLog(point, time);
		return result;
	}

	private void saveLog(ProceedingJoinPoint joinPoint, long time) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		SysLog sysLog = new SysLog();
		ApiOperation logAnnotation = method.getAnnotation(ApiOperation.class);
		if (logAnnotation != null) {
			// 注解上的描述
			sysLog.setOperation(logAnnotation.value());
		}
		// 请求的方法名
		String className = joinPoint.getTarget().getClass().getSimpleName();
		String methodName = signature.getName();
		sysLog.setMethod(className + "." + methodName);
		// 获取request
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		Object obj = SecurityUtils.getSubject().getPrincipal();
		// 设置IP地址
		String ip = retrieveClientIp(request);
		sysLog.setIp(ip);
		// 模拟一个用户名
		if (obj != null) {
			SysUser user = (SysUser) obj;
			sysLog.setUsername(user.getUsername());
		}
		sysLog.setParams(JSON.toJSONString(request.getParameterMap()));
		sysLog.setTime(time);
		sysLog.setCreateDate(new Date());
		// 保存系统日志
		this.sysLogService.save(sysLog);
	}

	public static String retrieveClientIp(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (isUnAvailableIp(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (isUnAvailableIp(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (isUnAvailableIp(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip.split(",")[0];
	}

	private static boolean isUnAvailableIp(String ip) {
		return (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip));
	}

}
