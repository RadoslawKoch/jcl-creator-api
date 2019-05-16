package com.pallas.jclcreator.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.pallas.jclcreator.entities.Event;
import com.pallas.jclcreator.repos.EventRepository;

@Service
public class EventService 
    implements EventServiceDefinition{

    @Autowired
    private EventRepository repo;
	
    @Override
    public void logEvent(Event event) {
	this.repo.save(event);		
    }

    @Override
    public void deleteEvent(String id) {
	this.repo.deleteById(id);		
    }

    @Override
    public Event getEventById(String id) {
	return this.repo.findById(id).get();
    }

    @Override
    public List<Event> getAllEvents() {
	return this.repo.findAll();
    }

    @Override
    public List<Event> searchByName(String name) {
	return this.repo.findByNameLike(name);
    }

    @Override
    public List<Event> searchByDescription(String desc) {
	return this.repo.findByDescriptionLike(desc);
    }	
}
