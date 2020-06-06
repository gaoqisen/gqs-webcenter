package com.github.gaoqisen.sample.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.gaoqisen.webcenter.api.ApiController;
import com.github.gaoqisen.webcenter.core.SecurityInterceptor;
import com.github.gaoqisen.webcenter.core.WebCenterClientBeanFactory;
import com.github.gaoqisen.webcenter.core.WebCenterInitializing;
import com.github.gaoqisen.webcenter.pojo.WebCenterConsole;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebCenterConfig extends WebMvcConfigurerAdapter {

	@Bean
	public SecurityInterceptor securityInterceptor() {
		return new SecurityInterceptor();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(securityInterceptor()).excludePathPatterns("/static/*").excludePathPatterns("/error")
				.addPathPatterns("/**");

	}

	@Bean
	@DependsOn("webCenterConsole")
	public WebCenterClientBeanFactory springClientBeanFactory() {
		return new WebCenterClientBeanFactory();
	}

	@Bean
	public WebCenterInitializing webCenterInitializing() {
		return new WebCenterInitializing();
	}

	@Bean
	public WebCenterConsole webCenterConsole() {
		WebCenterConsole webCenterConsole = new WebCenterConsole();
		return webCenterConsole;
	}

	@Bean
	@DependsOn("redisConnectionFactory")
	public ApiController apiController() {
		return new ApiController();
	}

	@Bean
	public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		StringRedisTemplate template = new StringRedisTemplate(redisConnectionFactory);
		Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
		ObjectMapper om = new ObjectMapper();
		om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		jackson2JsonRedisSerializer.setObjectMapper(om);
		template.setValueSerializer(jackson2JsonRedisSerializer);
		template.afterPropertiesSet();
		return template;
	}

}
