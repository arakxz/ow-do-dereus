package com.arakxz.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.arakxz.core.business.entity.User;
import com.arakxz.core.business.service.ActivityService;
import com.arakxz.core.business.service.UserService;

@Controller
@RequestMapping("activity")
public class ActivityController {

    @Autowired
    private UserService userService;

    @Autowired
    private ActivityService activityService;
    
    
    @GetMapping("create")
    public String create(Model model) {
        
        User user = userService.authenticated();

        model.addAttribute("user", user);

        return "activity/create";

    }

    @GetMapping("list")
    public String list(Model model) {
        
        User user = userService.authenticated();

        model.addAttribute("user", user);
        model.addAttribute("activities", this.activityService.list(user));
        
        return "activity/list";
        
    }
    
    
    @GetMapping("account/stats")
    public String stats(Model model) {

        User user = userService.authenticated();

        model.addAttribute("user", user);
        model.addAttribute("balance", this.activityService.balance(user));

        return "activity/account-stats";
        
    }
    
    @GetMapping("/{hash}/download")
    @ResponseBody
    public ResponseEntity<Resource> download(@PathVariable("hash") String hash) {

        User user = userService.authenticated();
        Resource file = this.activityService.file(user, hash);

        if (file == null) {
            System.out.println("File not found");
            return null;
        }

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);

    }

    
    @PostMapping("register")
    public String register(
            @RequestParam("file") MultipartFile file,
            @RequestParam("subject") String subject,
            @RequestParam("content") String content, RedirectAttributes redirect) {
        
        User user = userService.authenticated();
        
        int status = this.activityService.create(user, file, subject, content);
        
        redirect.addFlashAttribute("status", status);

        return "redirect:/activity/create";
    }
    
}
