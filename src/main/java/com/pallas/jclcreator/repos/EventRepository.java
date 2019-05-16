package com.pallas.jclcreator.repos;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.pallas.jclcreator.entities.Event;

@Repository
public interface EventRepository 
    extends MongoRepository<Event,String>{
    List<Event> findByNameLike(String name);
    List<Event> findByDescriptionLike(String desc);
}
