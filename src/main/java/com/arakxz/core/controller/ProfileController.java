package com.arakxz.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.arakxz.core.business.entity.User;
import com.arakxz.core.business.service.UserService;

@Controller
@RequestMapping("profile")
public class ProfileController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    public String index(Model model) {
        
        User user = userService.authenticated();

        model.addAttribute("user", user);
        
        return "profile";

    }
    
    @PostMapping("create")
    public String create(
    		@RequestParam("name") String name,
    		// @RequestParam("email") String email,
    		@RequestParam("phone") String phone,
    		@RequestParam("message") String message) {
    	
    	this.userService.profile(name, phone, message);

    	return "redirect:/profile";
    	
    }

}
