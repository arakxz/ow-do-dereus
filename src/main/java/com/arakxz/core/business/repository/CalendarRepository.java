package com.arakxz.core.business.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.arakxz.core.business.entity.Calendar;

public interface CalendarRepository extends CrudRepository<Calendar, Long> {
    
    public Calendar findById(long id);

    @Query(""
            + " SELECT calendar FROM Calendar calendar"
            + " JOIN calendar.user user"
            + " WHERE user.id = ?1"
            + "   AND (calendar.start BETWEEN ?2 AND ?3 OR calendar.end BETWEEN ?2 AND ?3)" 
    )
    public List<Calendar> allEventsWithDates(
            long user, Date start, Date end
    );

}
