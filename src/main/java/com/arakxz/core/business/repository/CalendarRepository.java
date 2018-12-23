package com.arakxz.core.business.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.arakxz.core.business.entity.Calendar;

public interface CalendarRepository extends CrudRepository<Calendar, Long> {
    
    public Calendar findById(long id);

    @Query(""
            + " SELECT cal FROM Calendar cal"
            + " JOIN cal.user usr"
            + " WHERE usr.id = ?1"
            + "   AND (cal.start BETWEEN ?2 AND ?3 OR cal.end BETWEEN ?2 AND ?3)" 
    )
    public List<Calendar> allEventsOnDates(
            long user, Date start, Date end
    );

}
