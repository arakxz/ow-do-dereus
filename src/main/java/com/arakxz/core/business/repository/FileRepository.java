package com.arakxz.core.business.repository;

import org.springframework.data.repository.CrudRepository;

import com.arakxz.core.business.entity.File;

public interface FileRepository extends CrudRepository<File, Long> {
    
    public File findByHash(String hash);

}
