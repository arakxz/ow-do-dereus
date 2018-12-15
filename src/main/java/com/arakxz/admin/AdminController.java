package com.arakxz.admin;


import javax.servlet.http.HttpServletRequest;

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
import com.arakxz.core.business.service.RoleService;
import com.arakxz.core.business.service.UserService;

@Controller
@RequestMapping("admin/dashboard")
public class AdminController {

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private UserService userserv;

	@Autowired
	private RoleService roleserv;


	@GetMapping
	public String index(Model model) {

		User user = (User) this.request.getSession().getAttribute("user");

		model.addAttribute("user", user);
		model.addAttribute("users", this.userserv.top5Users());

		return "admin/home";

	}


	@GetMapping("user")
	public String user(
			@ModelAttribute("status") String status, Model model) {

		User user = this.userserv.authenticated();

		System.out.println("status: "+status);

		model.addAttribute("user", user);
		model.addAttribute("users", this.userserv.all());
		model.addAttribute("roles", this.roleserv.all());
		model.addAttribute("status", status.isEmpty() ? 0 : Integer.parseInt(status));

		return "admin/user-panel";

	}

	@PostMapping("user/update")
	public String userUpdate(
			@RequestParam("roles") String[] roles,
			@RequestParam("username") String username, RedirectAttributes redirect) {
		
		int status = this.userserv.updateRoles(
				username,
				this.roleserv.findByUsernameIn(roles)
		);
		
		redirect.addFlashAttribute("status", status);

		return "redirect:/admin/dashboard/user";

	}

}
