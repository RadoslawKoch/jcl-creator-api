package com.pallas.jclcreator.builders;

import java.time.LocalDateTime;
import com.pallas.jclcreator.entities.Event;

public class EventBuilder {

    private LocalDateTime date;  
    private String title;  
    private String description;

    public EventBuilder setDate(LocalDateTime date) {
	this.date = date;
	return this;
    }
	
    public EventBuilder setTitle(String title) {
	this.title = title;
	return this;
    }
	
    public EventBuilder setDescription(String description) {
	this.description = description;
	return this;
    }
	
    public Event build() {
	return new Event(date,title,description);
    }
}
