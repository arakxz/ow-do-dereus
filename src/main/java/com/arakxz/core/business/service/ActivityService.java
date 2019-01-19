package com.arakxz.core.business.service;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.arakxz.core.business.entity.Activity;
import com.arakxz.core.business.entity.File;
import com.arakxz.core.business.entity.User;
import com.arakxz.core.business.repository.ActivityRepository;
import com.arakxz.core.business.repository.FileRepository;
import com.arakxz.core.business.service.storage.StorageService;
import com.arakxz.core.business.service.storage.StorageServiceException;

@Service
public class ActivityService {

    public static final int OK = 1;
    public static final int ERROR = 2;

    @Autowired
    private ActivityRepository activityRepository;
    
    @Autowired
    private FileRepository fileRepository;

    
    @Autowired
    private StorageService storageService;

    
    /**
     * @param user
     * 
     * @return
     */
    public Map<String, Object> balance(User user) {

        Map<String, Object> balance = new HashMap<String, Object>();

        long total = this.activityRepository.countByAuthor(user.getId());
        long open = 0;
        long inProcess = 0;
        long closed = 0;

        balance.put("line--total", total);
        balance.put("line--month", this.activityRepository.countByAuthorWithDates(user.getId(),
                CalendarService.firstDayOfMonth(), CalendarService.lastDayOfMonth())
        );
        balance.put("line--week", this.activityRepository.countByAuthorWithDates(user.getId(),
                CalendarService.firstDayOfWeek(), CalendarService.lastDayOfWeek())
        );
        balance.put("line--data", this.currentYear(user));

        if (total > 0) {
            open = (this.activityRepository.countByAuthorWithStatus(user.getId(), Activity.STATUS_OPEN) * 100) / total;
            inProcess = (this.activityRepository.countByAuthorWithStatus(user.getId(), Activity.STATUS_IN_PROCESS) * 100) / total;
            closed = (this.activityRepository.countByAuthorWithStatus(user.getId(), Activity.STATUS_CLOSED) * 100) / total;
        }

        balance.put("pie--open", open);
        balance.put("pie--in-process", inProcess);
        balance.put("pie--closed", closed);
        
        return balance;

    }
    
    public List<Activity> list() {
        return this.activityRepository.findAll();
    }
    
    public List<Activity> list(User author) {
        return this.activityRepository.findAllByAuthor(author);
    }    
    
    public Activity find(long id) {
    	return this.activityRepository.findById(id);
    }

    public Resource file(User author, String hash) {
        try {

            File file = this.fileRepository.findByHash(hash);

            return this.storageService.loadAsResource(file.getLocation());

        } catch (FileNotFoundException error) {
            return null;
        }
    }

    public int create(User author, MultipartFile upload, String title, String content, User responsible) {

        try {

            this.storageService.store(upload);

            String location = this.storageService.load(upload.getOriginalFilename()).toString();
            
            Activity activity = new Activity();
            
            activity.setAuthor(author);
            activity.setTitle(title);
            activity.setContent(content);
            
            if (author.isAdmin()) {
        		activity.setResponsible(responsible);
        	}

            File file = new File();
            
            file.setLocation(location);
            file.setHash(UserService.passwordHash(location));
            file.setActivity(activity);
            
            this.activityRepository.save(activity);
            this.fileRepository.save(file);

        } catch (StorageServiceException error) {
            return ERROR;
        }

        return OK;
    }
    
    public int update(User author, long id, String title, String content, User responsible) {
    	
    	Activity activity = this.activityRepository.findById(id);

    	activity.setTitle(title);
    	activity.setContent(content);
    	
    	if (author.isAdmin()) {
    		activity.setResponsible(responsible);
    	}
    	
    	this.activityRepository.save(activity);
    	
    	return OK;
    }
    
    private int[] currentYear(User user) {
        
        int month;
        int total;
        String[] parts;
        
        int[] chart = new int[12];
        
        List<String> current = this.activityRepository.countByAuthorCurrentYear(user.getId());
        
        for (String string : current) {
            
            parts = string.split(";");
            
            month = Integer.parseInt(parts[0]);
            total = Integer.parseInt(parts[1]);

            chart[month -1] = total;
            
        }
        
        return chart;
    }

}
