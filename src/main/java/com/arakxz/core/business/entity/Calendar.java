package com.arakxz.core.business.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "calendars")
public class Calendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(nullable = false)
    private String category;
    
    @Column(nullable = false)
    private Date start;
    
    @Column(nullable = false)
    private Date end;
    
    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(
            name = "id_user",
            nullable = false
    )
    private User user;

    
    public Calendar() {
        
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }


    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }
    
    
    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }


    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }


    /**
     * @return the category
     */
    public String getCategory() {
        return category;
    }
  

    /**
     * @param category the category to set
     */
    @JsonProperty("className")
    public void setCategory(String category) {
        this.category = category;
    }


    /**
     * @return the start
     */
    public Date getStart() {
        return start;
    }


    /**
     * @param start the start to set
     */
    public void setStart(Date start) {
        this.start = start;
    }


    /**
     * @return the end
     */
    public Date getEnd() {
        return end;
    }


    /**
     * @param end the end to set
     */
    public void setEnd(Date end) {
        this.end = end;
    }


    /**
     * @return the user
     */
    @JsonIgnore
    public User getUser() {
        return user;
    }


    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }
    
}
