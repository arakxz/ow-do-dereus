package com.arakxz.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import com.arakxz.core.business.entity.User;

public class AdminControllerInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(
			HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

		User user = (User) request.getSession().getAttribute("user");

		if (!user.isAdmin()) {
			response.sendRedirect("/dashboard");
			return false;
		}

		return true;
	}

}
