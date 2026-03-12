1. Clarifying Requirements
Before starting the design, it's important to ask thoughtful questions to uncover hidden 
assumptions and better define the scope of the system.

Here is an example of how a conversation between the candidate and the interviewer might unfold:

Discussion

Candidate: Is this system for the end-user (customer) to book tickets, or is it an internal 
system for cinema administrators to manage shows and screens?

Interviewer: Let's focus on the customer-facing booking workflow. The primary user story is a 
customer finding a movie and booking seats for a specific show.

Candidate: How should users find movies? Should they be able to search by movie title, city, or cinema?

Interviewer: Users should be able to find all shows for a specific movie in a given city.

Candidate: Should the system support different seat types such as ‘Standard’, ‘Premium’, and ‘Recliner’, with variable pricing?

Interviewer: Yes, each screen can have multiple seat types with different pricing.

Candidate: Should users be able to select specific seats, or should the system assign them automatically?

Interviewer: Users must be able to select specific seats from a seat map during the booking process.

Candidate: A critical issue in booking systems is concurrency. 
What should happen if two users try to book the same seat at the 
same time?

Interviewer: Excellent point. The system must prevent double-booking. When a user selects seats, those seats should be 
temporarily locked for a short duration while they complete the payment. If the payment is not completed within the timeout, 
the seats should be released.

Candidate: How is payment handled? Do we need to integrate with a real payment gateway?

Interviewer: We can assume an external payment gateway. Our system should be able to initiate a payment process 
with a specific amount and handle success or failure responses. The design should allow for different payment methods, 
like credit cards or PayPal.

Candidate: Should there be a notification feature? For example, notifying users when booking opens for a new, 
highly anticipated movie.

Interviewer: Yes, that's a great feature to include. Let's add a mechanism for users to subscribe to a movie and 
receive a notification when it becomes available for booking.

After gathering the details, we can summarize the key system requirements.

1.1 Functional Requirements
Users can search for shows based on a movie title and a city.
The system should support multiple cities, cinemas, screens, and shows.
Each screen has a defined layout of seats with different types (e.g., REGULAR, PREMIUM).
A user can book one or more available seats for a specific show.
Double booking should be prevented.
The ticket price should be calculated dynamically based on configurable rules (e.g., seat types)
Users can subscribe to movies and receive notifications when booking opens for them.
The system must be flexible to support different payment methods.
1.2 Non-Functional Requirements
Concurrency: The system must be designed to handle concurrent booking requests gracefully, ensuring 
data integrity and preventing race conditions like double-booking.
Extensibility: The design should be modular. It should be easy to add new pricing strategies 
(e.g., holiday pricing) or new payment methods without significant changes to the core system.
Modularity: The system should follow good object-oriented principles with a clear separation of concerns.
Simplified Interface: The system should expose a simple API for clients to interact with, 
hiding the underlying complexity of the booking, locking, and payment processes.
After the requirements are clear, lets identify the core entities/objects we will have in our system.

//core entities/objects


city:
    -cityname
    List<cinema>
    List<movie>

cinema
    cinemaName
    List<screen>

screen
    screenName
    List<seat>
    List<show>

seat
    seatName
    seatType
    price
movie
    -movieId
    -duration
    -price
    -List<show>
show
    -movie
    -showId
    -date
    -time
    -List<seat>
booking
payment

bookingService //this responsible for booking 
paymentService //this is responsible for payments
TicketService   //this is responsible for ticket generation

MovieTicketManager-> this will facade all the services


//state patten for movie system management
//observer pattern for notification
//for payment stategy pattern
//also use command pattern for reservation.
