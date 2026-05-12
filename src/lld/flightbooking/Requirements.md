The airline management system should allow users to search for flights based on source, destination, and date.
Users should be able to book flights, select seats, and make payments.
The system should manage flight schedules, aircraft assignments, and crew assignments.
The system should handle passenger information, including personal details and baggage information.
The system should support different types of users, such as passengers, airline staff, and administrators.
The system should be able to handle cancellations, refunds, and flight changes.
The system should ensure data consistency and handle concurrent access to shared resources.
The system should be scalable and extensible to accommodate future enhancements and new features.

Core Entities
        
    User
        name
        mobile
	
    booking
        -flight
        -passanger
        -seat
        -price
        -bookingStatus  //CONFIRMED,CANCELLED,PENDING,EXPIRED   

    seat
        -seatNo
        -seatType //ECONOMY,PREMIUM_ECONOMY,BUSINESS,FIRST_CLASS
        -status  //AVALIBLE,BOOKED

	payments
	
    flight
		-id
        -source
        -destination
        -departureTime
        -ArrivalTime
        aircraft
            -id
            -name
	    -flightNo
        -Map<Sting,Seat> seats
        -FlightStatus

    Passengers extands user
		List<Flight> fli  ghtHistory;
	Staff extands user
		department
	Administrators
		department
	SearchRequest
		source
		destination
