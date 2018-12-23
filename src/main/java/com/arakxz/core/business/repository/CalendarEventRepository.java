package com.arakxz.core.business.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.arakxz.core.business.entity.CalendarEvent;
import com.arakxz.core.business.entity.User;

public interface CalendarEventRepository extends CrudRepository<CalendarEvent, Long> {

    public List<CalendarEvent> findAllByUser(User user);
    
}
