package com.arakxz.core.business.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arakxz.core.business.repository.CalendarEventRepository;
import com.arakxz.core.business.repository.CalendarRepository;
import com.arakxz.core.business.entity.Calendar;
import com.arakxz.core.business.entity.CalendarEvent;
import com.arakxz.core.business.entity.User;

@Service
public class CalendarService {

    public static final int OK = 1;
    public static final int ERROR_FIELD_EMPTY = 2;

    @Autowired
    private CalendarRepository calendarrepo;

    @Autowired
    private CalendarEventRepository calendareventsrepo;

    /**
     * @param categories all categories available
     */
    private Map<String, String> categories = createCategories();

    private static Map<String, String> createCategories() {
        Map<String, String> categories = new HashMap<String, String>();

        categories.put("bg-success", "Success");
        categories.put("bg-info", "Info");
        categories.put("bg-primary", "Primary");
        categories.put("bg-warning", "Warning");
        categories.put("bg-danger", "Danger");

        return categories;
    }

    /**
     * @return the categories
     */
    public Map<String, String> getCategories() {
        return categories;
    }

    public List<CalendarEvent> calendarEventsAvailable(User user) {
        return this.calendareventsrepo.findAllByUser(user);
    }

    public List<Calendar> eventsAvailable(User user, Date start, Date end) {

        return this.calendarrepo.allEventsOnDates(user.getId(), start, end);

    }

    
    /**
     * 
     * @param user
     * @param title
     * @param color
     * 
     * @return
     */
    public int createCalendarEvent(User user, String title, String category) {

        if (title.isEmpty() || category.isEmpty()) {
            return ERROR_FIELD_EMPTY;
        }

        CalendarEvent event = new CalendarEvent();

        event.setUser(user);
        event.setTitle(title);
        event.setCategory(category);

        this.calendareventsrepo.save(event);

        return OK;

    }

    
    public Calendar registerOrUpdateCalendarEvent(
            User user, String title, String category, Date start, Date end) {

        return this.registerOrUpdateCalendarEvent(user, title, category, start, end, 0);

    }

    public Calendar registerOrUpdateCalendarEvent(
            User user, String title, String category, Date start, Date end,
            long id) {

        Calendar calendar = null;
        
        if (id == 0) {
            calendar = new Calendar();
        }
        else {
            calendar = this.calendarrepo.findById(id);
        }
        
        calendar.setUser(user);
        calendar.setTitle(title);
        calendar.setStart(start);
        calendar.setEnd(end);
        calendar.setCategory(category);

        return this.calendarrepo.save(calendar);

    }

}
