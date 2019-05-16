package com.pallas.jclcreator.service;

import java.util.List;

import com.pallas.jclcreator.entities.Event;

public interface EventServiceDefinition {

    List<Event> searchByName(String name);
    List<Event> searchByDescription(String desc);
    void logEvent(Event event);
    void deleteEvent(String id);
    Event getEventById(String id);
    List<Event> getAllEvents();
}
