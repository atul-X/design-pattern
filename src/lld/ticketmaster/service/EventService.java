package lld.ticketmaster.service;

import lld.ticketmaster.enums.EventStatus;
import lld.ticketmaster.model.Event;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class EventService {
    private final Map<String, Event> events = new ConcurrentHashMap<>();

    public Event addEvent(Event event) {
        events.put(event.getEventId(), event);
        return event;
    }

    public Event getEvent(String eventId) {
        Event event = events.get(eventId);
        if (event == null) throw new IllegalArgumentException("Event not found: " + eventId);
        return event;
    }

    public List<Event> getActiveEvents() {
        return events.values().stream()
                .filter(e -> e.getStatus() == EventStatus.ACTIVE)
                .collect(Collectors.toList());
    }

    public List<Event> getEventsByCity(String city) {
        return events.values().stream()
                .filter(e -> e.getVenue().getCity().equalsIgnoreCase(city))
                .filter(e -> e.getStatus() == EventStatus.ACTIVE)
                .collect(Collectors.toList());
    }
}