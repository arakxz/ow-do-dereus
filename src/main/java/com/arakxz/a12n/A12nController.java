package com.arakxz.a12n;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.arakxz.core.business.entity.User;
import com.arakxz.core.business.service.UserService;

@Controller
@RequestMapping("/auth")
public class A12nController {

		
	@Autowired
	private UserService userserv;
	
	
	@Autowired
	private HttpServletRequest request;
	
	@GetMapping
	public String index() {
		return "redirect:/auth/login";
	}
	
	@GetMapping("/login")
	public String login(@ModelAttribute("tab") String tab, @ModelAttribute("status") String status, Model model) {

		model.addAttribute("tab", tab.isEmpty() ? "login" : tab);
		model.addAttribute("status", status.isEmpty() ? 0 : Integer.parseInt(status));

		return "a12n/login";
	}
	
	
	@PostMapping("/login")
	public String validate(@ModelAttribute User user, RedirectAttributes redirect) {

		int response = this.userserv.authenticate(user);

		if (response == UserService.OK) {
			return "redirect:/dashboard";
		}

		redirect.addFlashAttribute("status", response);

		return "redirect:/auth/login";
	}
	
	
	@GetMapping("/logout")
	public String logout()
	{

		HttpSession session = this.request.getSession(false);

		if (session != null) {
			session.invalidate();
		}

		return "redirect:/auth/login";
		
	}

	
	@PostMapping(path="/register")
	public String register(
			@RequestParam("username") String username,
			@RequestParam("email") String email, RedirectAttributes redirect) {
		
		int response = this.userserv.register(username, "123456", "123456", email);

		redirect.addFlashAttribute("status", response);

		return "redirect:/admin/dashboard/user";

	}
	
}
