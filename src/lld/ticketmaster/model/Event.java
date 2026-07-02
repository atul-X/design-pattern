package lld.ticketmaster.model;

import lld.ticketmaster.enums.EventStatus;
import lld.ticketmaster.enums.SeatStatus;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Event {
    private final String eventId;
    private final String name;
    private final String artist;
    private final Venue venue;
    private final Instant startTime;
    private final Map<String, Seat> seats; // seatId → Seat
    private volatile EventStatus status;

    public Event(String eventId, String name, String artist, Venue venue,
                 Instant startTime, List<Seat> seatList) {
        this.eventId = eventId;
        this.name = name;
        this.artist = artist;
        this.venue = venue;
        this.startTime = startTime;
        this.seats = new ConcurrentHashMap<>();
        seatList.forEach(s -> seats.put(s.getSeatId(), s));
        this.status = EventStatus.ACTIVE;
    }

    public Seat getSeat(String seatId) {
        Seat seat = seats.get(seatId);
        if (seat == null) throw new IllegalArgumentException("Seat not found: " + seatId);
        return seat;
    }

    public List<Seat> getSeatsByIds(List<String> seatIds) {
        return seatIds.stream().map(this::getSeat).collect(Collectors.toList());
    }

    public List<Seat> getAvailableSeats() {
        return seats.values().stream()
                .filter(s -> s.getStatus() == SeatStatus.AVAILABLE)
                .collect(Collectors.toList());
    }

    public String getEventId()      { return eventId; }
    public String getName()         { return name; }
    public String getArtist()       { return artist; }
    public Venue getVenue()         { return venue; }
    public Instant getStartTime()   { return startTime; }
    public EventStatus getStatus()  { return status; }
    public void setStatus(EventStatus s) { this.status = s; }
    public Map<String, Seat> getSeats() { return Collections.unmodifiableMap(seats); }
}