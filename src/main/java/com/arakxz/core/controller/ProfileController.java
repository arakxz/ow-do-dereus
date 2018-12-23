package com.arakxz.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

}
