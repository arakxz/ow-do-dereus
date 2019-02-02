package com.arakxz.core.business.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arakxz.core.business.repository.CalendarEventRepository;
import com.arakxz.core.business.repository.CalendarRepository;
import com.arakxz.core.business.entity.Activity;
import com.arakxz.core.business.entity.Calendar;
import com.arakxz.core.business.entity.CalendarEvent;
import com.arakxz.core.business.entity.User;

@Service
public class CalendarService {

    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    
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

    
    /**
     * @param user
     * 
     * @return
     */
    public List<CalendarEvent> calendarEventsAvailable(User user) {
        return this.calendareventsrepo.findAllByUser(user);
    }

    
    /**
     * @param user
     * @param start
     * @param end
     * 
     * @return
     */
    public List<Calendar> eventsAvailable(User user, Date start, Date end) {

        return this.calendarrepo.allEventsWithDates(user.getId(), start, end);

    }

    
    /**
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

    
    /**
     * @param user
     * @param title
     * @param category
     * @param start
     * @param end
     * 
     * @return
     */
    public Calendar registerOrUpdateCalendarEvent(
            User user, String title, String category, Date start, Date end) {

        return this.registerOrUpdateCalendarEvent(user, title, category, start, end, 0);

    }
    
    /**
     * @param user
     * @param title
     * @param category
     * @param start
     * @param end
     * @param activity
     * 
     * @return
     */
    public Calendar registerOrUpdateCalendarEvent(
            User user, String title, String category, Date start, Date end, Activity activity) {

    	long id = 0;
    	
    	if (activity.getCalendar() != null) {
    		id = activity.getCalendar().getId();
    	}

        return this.registerOrUpdateCalendarEvent(user, title, category, start, end, id);

    }

    
    /**
     * @param user
     * @param title
     * @param category
     * @param start
     * @param end
     * @param id
     * 
     * @return
     */
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
    
    
    /**
     * @param date
     * 
     * @return
     * 
     * @throws ParseException
     */
    public static Date parse(String date) throws ParseException {
    	if (date.length() == DATE_FORMAT.length()) {
    		return new SimpleDateFormat(DATE_FORMAT).parse(date);
    	}
    	return new SimpleDateFormat(DATETIME_FORMAT).parse(date);
    }

    
    /**
     * @return the first day of the current month 
     */
    public static Date firstDayOfMonth() {

        java.util.Calendar month = java.util.Calendar.getInstance();

        month.set(java.util.Calendar.DAY_OF_MONTH, 1);
        month.set(java.util.Calendar.HOUR_OF_DAY, 0);
        month.set(java.util.Calendar.SECOND, 0);
        month.set(java.util.Calendar.MINUTE, 0);

        return month.getTime();

    }
    
    
    /**
     * @return the last day of the current month
     */
    public static Date lastDayOfMonth() {

        java.util.Calendar month = java.util.Calendar.getInstance();

        month.set(java.util.Calendar.DAY_OF_MONTH, month.getActualMaximum(java.util.Calendar.DAY_OF_MONTH));
        month.set(java.util.Calendar.HOUR_OF_DAY, 23);
        month.set(java.util.Calendar.SECOND, 59);
        month.set(java.util.Calendar.MINUTE, 59);

        return month.getTime();

    }
    
    
    public static Date firstDayOfWeek() {

        java.util.Calendar month = java.util.Calendar.getInstance();

        month.set(java.util.Calendar.DAY_OF_WEEK, java.util.Calendar.SUNDAY);
        month.set(java.util.Calendar.HOUR_OF_DAY, 0);
        month.set(java.util.Calendar.SECOND, 0);
        month.set(java.util.Calendar.MINUTE, 0);

        return month.getTime();

    }
    
    
    /**
     * @return the last day of the current month
     */
    public static Date lastDayOfWeek() {

        java.util.Calendar month = java.util.Calendar.getInstance();

        month.set(java.util.Calendar.DAY_OF_WEEK, java.util.Calendar.SATURDAY);
        month.set(java.util.Calendar.HOUR_OF_DAY, 23);
        month.set(java.util.Calendar.SECOND, 59);
        month.set(java.util.Calendar.MINUTE, 59);

        return month.getTime();

    }
    
}
