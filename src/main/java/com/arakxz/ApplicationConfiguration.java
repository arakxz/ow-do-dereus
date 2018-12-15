package com.arakxz;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.arakxz.a12n.A12nControllerInterceptor;
import com.arakxz.admin.AdminControllerInterceptor;

@Configuration
public class ApplicationConfiguration implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {

		registry.addInterceptor(new A12nControllerInterceptor())
				.excludePathPatterns("/auth/**")
				.excludePathPatterns("/assets/**")
				.order(Ordered.HIGHEST_PRECEDENCE);

		registry.addInterceptor(new AdminControllerInterceptor())
				.addPathPatterns("/admin/**")
				.order(Ordered.LOWEST_PRECEDENCE);

	}

}
