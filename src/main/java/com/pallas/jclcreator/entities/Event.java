package com.pallas.jclcreator.entities;

import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="events")
public class Event {
	
    @Id
    private String id;	
    @Indexed(unique = true, direction = IndexDirection.DESCENDING)
    private LocalDateTime date;
    private String name;
    private String description;
    
    private Event() {}
    
    public Event(LocalDateTime date, String title, String description) {
	this.date = date;
	this.name = title;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
	return date;
    }

    public void setDate(LocalDateTime date) {
	this.date = date;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
