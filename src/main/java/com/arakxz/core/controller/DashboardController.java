package com.arakxz.core.controller;


import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.arakxz.core.business.entity.User;
import com.arakxz.core.business.entity.Calendar;
import com.arakxz.core.business.service.CalendarService;
import com.arakxz.core.business.service.UserService;

@Controller
@RequestMapping("dashboard")
public class DashboardController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private CalendarService calendarService;
    

    /**
     * @param model
     * 
     * @return
     */
    @GetMapping
    public String index(Model model) {

        User user = userService.authenticated();

        model.addAttribute("user", user);

        return "home";
    }
    
    
    /**
     * @param model
     * 
     * @return
     */
    @GetMapping("calendar")
    public String calendar(Model model) {
        
        User user = userService.authenticated();

        model.addAttribute("user", user);
        model.addAttribute("events", this.calendarService.calendarEventsAvailable(user));
        model.addAttribute("categories", this.calendarService.getCategories());

        return "calendar";
    }
    

    /**
     * @param title
     * @param category
     * @param redirect
     * 
     * @return
     */
    @PostMapping("calendar/event")
    public String calendarEventCreate(
            @RequestParam("title") String title,
            @RequestParam("category") String category, RedirectAttributes redirect) {

        int status = this.calendarService.createCalendarEvent(
                this.userService.authenticated(),
                title,
                category
        );

        redirect.addFlashAttribute("status", status);

        return "redirect:/dashboard/calendar";

    }
    
    
    /**
     * @param title
     * @param category
     * @param start
     * @param end
     * 
     * @return
     * 
     * @throws ParseException
     */
    @ResponseBody
    @PostMapping(path = "calendar/event/register", produces = "application/json")
    public Map<String, Object> calendarEventRegister(
            @RequestParam("title") String title,
            @RequestParam("category") String category,
            @RequestParam("start") String start, @RequestParam("end") String end) throws ParseException {
        
        Map<String, Object> response = new HashMap<String, Object>();
        
        response.put("code", CalendarService.OK);
        response.put("data", this.calendarService.registerOrUpdateCalendarEvent(
                this.userService.authenticated(),
                title,
                category,
                CalendarService.parse(start),
                CalendarService.parse(end)
         ));
        
        return response;
        
    }
    
    
    /**
     * @param id
     * @param title
     * @param category
     * @param start
     * @param end
     * 
     * @return
     * 
     * @throws ParseException
     */
    @ResponseBody
    @PutMapping(path = "calendar/event/{id}/update", produces = "application/json")
    public Map<String, Object> calendarEventUpdate(
            @PathVariable("id") long id,
            @RequestParam("title") String title,
            @RequestParam("category") String category,
            @RequestParam("start") String start, @RequestParam("end") String end) throws ParseException {
        
        Map<String, Object> response = new HashMap<String, Object>();
        
        response.put("code", CalendarService.OK);
        response.put("data", this.calendarService.registerOrUpdateCalendarEvent(
                this.userService.authenticated(),
                title,
                category,
                CalendarService.parse(start),
                CalendarService.parse(end),
                id
         ));
        
        return response;
        
    }
    
    
    /**
     * 
     * @param start
     * @param end
     * 
     * @return
     * 
     * @throws ParseException
     */
    @ResponseBody
    @GetMapping(path = "calendar/events", produces = "application/json")    
    public List<Calendar> calendarEvents(
            @RequestParam("start") String start, @RequestParam("end") String end) throws ParseException {
  
        return this.calendarService.eventsAvailable(
                this.userService.authenticated(),
                CalendarService.parse(start),
                CalendarService.parse(end)
        ); 

    }


}
