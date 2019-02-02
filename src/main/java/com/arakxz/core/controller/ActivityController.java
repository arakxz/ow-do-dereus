package com.arakxz.core.controller;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.arakxz.core.business.entity.Activity;
import com.arakxz.core.business.entity.Calendar;
import com.arakxz.core.business.entity.User;
import com.arakxz.core.business.service.ActivityService;
import com.arakxz.core.business.service.CalendarService;
import com.arakxz.core.business.service.UserService;

@Controller
@RequestMapping("activity")
public class ActivityController {

    @Autowired
    private UserService userService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private CalendarService calendarService;
    
    
    @GetMapping("/create")
    public String create(@ModelAttribute("status") String status, Model model) {
        
        User user = userService.authenticated();

        model.addAttribute("user", user);
        
        if (user.isAdmin()) {
        	model.addAttribute("technicians", this.userService.all());
        }
        
        model.addAttribute("status", status.isEmpty() ? 0 : Integer.parseInt(status));

        return "activity/create";

    }

    @GetMapping("/manage")
    public String manage(Model model) {
        
        User user = userService.authenticated();

        model.addAttribute("user", user);
        model.addAttribute("myActivities", this.activityService.list(user));
        model.addAttribute("userActivities", user.isAdmin()
        		? this.activityService.list()
        		: null
        );
        
        return "activity/manage-activities";
        
    }

    
    @GetMapping("/{activity:[0-9]+}/show")
    public String show(@PathVariable("activity") String activity, Model model) {
        
    	User user = userService.authenticated();

        model.addAttribute("user", user);
        model.addAttribute("activity", this.activityService.find(Long.parseLong(activity)));
        
        if (user.isAdmin()) {
        	model.addAttribute("technicians", this.userService.all());
        }
        
        System.out.println(this.activityService.find(Long.parseLong(activity)));
        
        return "activity/show";
        
    }
    
    
    @GetMapping("/account/stats")
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
    
    @PostMapping("/{activity:[0-9]+}/update")
    public String update(
    		@PathVariable("activity") String identifier,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value = "start", required = false) String start,
            @RequestParam(value = "end", required = false) String end,
            @RequestParam(value = "responsible", required = false) String responsible, RedirectAttributes redirect) {
        
        User user = userService.authenticated();
        User technical = null;

        Calendar calendar = null;
        Activity activity = this.activityService.find(Long.parseLong(identifier));
        
        if (user.isAdmin()) {
        	technical = userService.find(responsible);
        	try {
				calendar = calendarService.registerOrUpdateCalendarEvent(
						activity.getAuthor(),
						title,
						ActivityService.ACTIVITY_COLOR,
						CalendarService.parse(start),
						CalendarService.parse(end),
						activity
				);
			} catch (ParseException error) {
				error.printStackTrace();
			}
        }

        int status = this.activityService.update(
        		activity,
        		user,
        		title,
        		content,
        		technical,
        		calendar
        );
        
        redirect.addFlashAttribute("status", status);

        return "redirect:/activity/"+ identifier +"/show";
    }

    
    @PostMapping("register")
    public String register(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value = "start", required = false) String start,
            @RequestParam(value = "end", required = false) String end,
            @RequestParam(value = "responsible", required = false) String responsible, RedirectAttributes redirect) {
        
        User user = userService.authenticated();
        User technical = null;
        Calendar calendar = null;
        
        if (user.isAdmin()) {
        	technical = userService.find(responsible);
        	// Only create a calendar if start and end date is available
        	if (!start.isEmpty() && !end.isEmpty()) {
        		try {
					calendar = calendarService.registerOrUpdateCalendarEvent(
							user,
							title,
							ActivityService.ACTIVITY_COLOR,
							CalendarService.parse(start),
							CalendarService.parse(end)
					);
				} catch (ParseException error) {
					error.printStackTrace();
				}
        	}
        }

        int status = this.activityService.create(user, file, title, content, technical, calendar);
        
        redirect.addFlashAttribute("status", status);

        return "redirect:/activity/create";
    }
    
}
