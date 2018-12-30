package com.arakxz.core.business.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.arakxz.core.business.entity.Activity;
import com.arakxz.core.business.entity.User;

public interface ActivityRepository extends CrudRepository<Activity, Long> {

    public Activity findById(long id);
        
    public List<Activity> findAll();
    public List<Activity> findAllByAuthor(User author);
    
    
    @Query(""
            + " SELECT COUNT(activity) FROM Activity activity"
            + " JOIN activity.author author"
            + " WHERE author.id = ?1"
    )
    public Long countByAuthor(long id);
    
    @Query(""
            + " SELECT COUNT(activity) FROM Activity activity"
            + " JOIN activity.author author"
            + " WHERE author.id = ?1 AND activity.status = ?2"
    )
    public Long countByAuthorWithStatus(long id, int status);
    
    
    @Query(""
            + " SELECT COUNT(activity) FROM Activity activity"
            + " JOIN activity.author author"
            + " WHERE author.id = ?1 AND activity.created BETWEEN ?2 AND ?3"
    )
    public Long countByAuthorWithDates(long id, Date start, Date end);
    
    
    @Query(""
            + " SELECT CONCAT(MONTH(activity.created), ';', COUNT(activity)) AS amount FROM Activity activity"
            + " JOIN activity.author author"
            + " WHERE author.id = ?1 AND YEAR(NOW()) = YEAR(activity.created)"
            + " GROUP BY MONTH(activity.created)"
    )
    public List<String> countByAuthorCurrentYear(long id);
    
}
