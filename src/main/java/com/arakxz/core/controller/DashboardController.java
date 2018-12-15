package com.arakxz.core.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.arakxz.core.business.entity.User;


@Controller
@RequestMapping("dashboard")
public class DashboardController {

	@Autowired
	private HttpServletRequest request;
	
	@GetMapping
	public String index(Model model) {

		User user = (User) this.request.getSession().getAttribute("user");
		
		model.addAttribute("user", user);
		
		return "home";
	}
	
	@GetMapping("profile")
	public String profile() {
		
		return "profile";
	}
	
}
