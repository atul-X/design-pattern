//package lld.ticketmanager;
//
//import lld.ticketmanager.model.Priority;
//import lld.ticketmanager.model.Ticket;
//import lld.ticketmanager.model.TicketStatus;
//import lld.ticketmanager.model.User;
//import lld.ticketmanager.service.TicketService;
//import lld.ticketmanager.service.UserService;
//
//public class TicketManagerDemo {
//
//    public static void main(String[] args) {
//        UserService userService = new UserService();
//        TicketService ticketService = new TicketService();
//
//        // register users
//        User alice = userService.registerUser("Alice", "alice@example.com");
//        User bob   = userService.registerUser("Bob",   "bob@example.com");
//        User carol = userService.registerUser("Carol", "carol@example.com");
//
//        System.out.println("\n--- Creating Tickets ---");
//        Ticket t1 = ticketService.createTicket("Login page broken",    "Users cannot login",          Priority.CRITICAL, alice);
//        Ticket t2 = ticketService.createTicket("UI alignment issue",   "Button misaligned on mobile", Priority.LOW,      alice);
//        Ticket t3 = ticketService.createTicket("Payment gateway fail", "Payments failing for VISA",   Priority.HIGH,     bob);
//        Ticket t4 = ticketService.createTicket("Slow dashboard load",  "Dashboard takes 10s to load", Priority.MEDIUM,   carol);
//
//        System.out.println("\n--- Assigning Tickets ---");
//        ticketService.assignTicket(t1.getTicketId(), bob);
//        ticketService.assignTicket(t2.getTicketId(), carol);
//        ticketService.assignTicket(t3.getTicketId(), bob);
//        ticketService.assignTicket(t4.getTicketId(), carol);
//
//        System.out.println("\n--- Updating Status ---");
//        ticketService.updateStatus(t1.getTicketId(), TicketStatus.IN_PROGRESS);
//        ticketService.updateStatus(t1.getTicketId(), TicketStatus.RESOLVED);
//        ticketService.updateStatus(t1.getTicketId(), TicketStatus.CLOSED);
//        ticketService.updateStatus(t3.getTicketId(), TicketStatus.IN_PROGRESS);
//
//        System.out.println("\n--- Adding Comments ---");
//        ticketService.addComment(t1.getTicketId(), bob,   "Identified root cause, fixing now");
//        ticketService.addComment(t1.getTicketId(), alice, "Please fix ASAP");
//        ticketService.addComment(t3.getTicketId(), bob,   "Contacted payment gateway team");
//
//        System.out.println("\n--- Update Priority ---");
//        ticketService.updatePriority(t2.getTicketId(), Priority.MEDIUM);
//
//        System.out.println("\n--- Tickets by Status: OPEN ---");
//        ticketService.getTicketsByStatus(TicketStatus.OPEN)
//            .forEach(System.out::println);
//
//        System.out.println("\n--- Tickets by Priority: HIGH ---");
//        ticketService.getTicketsByPriority(Priority.HIGH)
//            .forEach(System.out::println);
//
//        System.out.println("\n--- Tickets assigned to Bob ---");
//        ticketService.getTicketsByUser(bob.getUserId())
//            .forEach(System.out::println);
//
//        System.out.println("\n--- Status Summary ---");
//        ticketService.getStatusSummary()
//            .forEach((status, count) -> System.out.println(status + " : " + count));
//
//        System.out.println("\n--- Priority Summary ---");
//        ticketService.getPrioritySummary()
//            .forEach((priority, count) -> System.out.println(priority + " : " + count));
//
//        System.out.println("\n--- Comments on T1 ---");
//        ticketService.getTicket(t1.getTicketId()).getComments()
//            .forEach(System.out::println);
//
//        System.out.println("\n--- Invalid Transition Test ---");
//        try {
//            ticketService.updateStatus(t1.getTicketId(), TicketStatus.IN_PROGRESS); // CLOSED -> IN_PROGRESS
//        } catch (Exception e) {
//            System.out.println("Caught: " + e.getMessage());
//        }
//    }
//}
