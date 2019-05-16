package com.pallas.jclcreator.repos;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.pallas.jclcreator.entities.JobEntity;

@Repository
public interface JobEntityRepository 
    extends MongoRepository<JobEntity,String>{
    JobEntity getJobByName(String name);
    List<JobEntity> findByNameLike(String name);
    List<JobEntity> findByJobCardAuthorLnameLike(String lname);
}
