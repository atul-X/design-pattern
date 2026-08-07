//package lld.ticketmanager.service;
//
//import lld.ticketmanager.exception.InvalidStatusTransitionException;
//import lld.ticketmanager.exception.TicketNotFoundException;
//import lld.ticketmanager.model.Priority;
//import lld.ticketmanager.model.Ticket;
//import lld.ticketmanager.model.TicketStatus;
//import lld.ticketmanager.model.User;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//public class TicketService {
//
//    private final Map<String, Ticket> tickets = new HashMap<>();
//    private int idCounter = 1;
//
//    // valid transitions
//    private static final Map<TicketStatus, List<TicketStatus>> TRANSITIONS = new HashMap<>();
//
//    static {
//        List<TicketStatus> fromOpen = new ArrayList<>();
//        fromOpen.add(TicketStatus.IN_PROGRESS);
//        fromOpen.add(TicketStatus.CLOSED);
//        TRANSITIONS.put(TicketStatus.OPEN, fromOpen);
//
//        List<TicketStatus> fromInProgress = new ArrayList<>();
//        fromInProgress.add(TicketStatus.RESOLVED);
//        fromInProgress.add(TicketStatus.OPEN);
//        TRANSITIONS.put(TicketStatus.IN_PROGRESS, fromInProgress);
//
//        List<TicketStatus> fromResolved = new ArrayList<>();
//        fromResolved.add(TicketStatus.CLOSED);
//        fromResolved.add(TicketStatus.OPEN);
//        TRANSITIONS.put(TicketStatus.RESOLVED, fromResolved);
//
//        TRANSITIONS.put(TicketStatus.CLOSED, new ArrayList<>());
//    }
//
//    public Ticket createTicket(String title, String description, Priority priority, User createdBy) {
//        String ticketId = "TKT-" + idCounter++;
//        Ticket ticket = new Ticket(ticketId, title, description, priority, createdBy);
//        tickets.put(ticketId, ticket);
//        System.out.println("Ticket created: " + ticket);
//        return ticket;
//    }
//
//    public Ticket getTicket(String ticketId) {
//        Ticket ticket = tickets.get(ticketId);
//        if (ticket == null) throw new TicketNotFoundException(ticketId);
//        return ticket;
//    }
//
//    public void assignTicket(String ticketId, User assignee) {
//        Ticket ticket = getTicket(ticketId);
//        ticket.setAssignedTo(assignee);
//        System.out.println("Ticket " + ticketId + " assigned to " + assignee.getName());
//    }
//
//    public void updateStatus(String ticketId, TicketStatus newStatus) {
//        Ticket ticket = getTicket(ticketId);
//        TicketStatus current = ticket.getStatus();
//        if (!TRANSITIONS.get(current).contains(newStatus)) {
//            throw new InvalidStatusTransitionException(current.name(), newStatus.name());
//        }
//        ticket.setStatus(newStatus);
//        System.out.println("Ticket " + ticketId + " status: " + current + " -> " + newStatus);
//    }
//
//    public void updatePriority(String ticketId, Priority priority) {
//        Ticket ticket = getTicket(ticketId);
//        ticket.setPriority(priority);
//        System.out.println("Ticket " + ticketId + " priority updated to " + priority);
//    }
//
//    public void addComment(String ticketId, User user, String comment) {
//        Ticket ticket = getTicket(ticketId);
//        String formatted = "[" + user.getName() + "]: " + comment;
//        ticket.addComment(formatted);
//        System.out.println("Comment added to " + ticketId + " by " + user.getName());
//    }
//
//    public List<Ticket> getTicketsByStatus(TicketStatus status) {
//        return tickets.values().stream()
//            .filter(t -> t.getStatus() == status)
//            .collect(Collectors.toList());
//    }
//
//    public List<Ticket> getTicketsByPriority(Priority priority) {
//        return tickets.values().stream()
//            .filter(t -> t.getPriority() == priority)
//            .collect(Collectors.toList());
//    }
//
//    public List<Ticket> getTicketsByUser(String userId) {
//        return tickets.values().stream()
//            .filter(t -> t.getAssignedTo() != null && t.getAssignedTo().getUserId().equals(userId))
//            .collect(Collectors.toList());
//    }
//
//    public List<Ticket> getTicketsCreatedBy(String userId) {
//        return tickets.values().stream()
//            .filter(t -> t.getCreatedBy().getUserId().equals(userId))
//            .collect(Collectors.toList());
//    }
//
//    public Map<TicketStatus, Long> getStatusSummary() {
//        return tickets.values().stream()
//            .collect(Collectors.groupingBy(Ticket::getStatus, Collectors.counting()));
//    }
//
//    public Map<Priority, Long> getPrioritySummary() {
//        return tickets.values().stream()
//            .collect(Collectors.groupingBy(Ticket::getPriority, Collectors.counting()));
//    }
//
//    public List<Ticket> getAllTickets() {
//        return new ArrayList<>(tickets.values());
//    }
//}
