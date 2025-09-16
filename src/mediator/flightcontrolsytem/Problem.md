Flight Control System Application Exercise
You are tasked with developing a Flight Control System for an airport. The system should manage communication between different components, such as airplanes and the control tower, to ensure safe takeoffs and landings. To improve interactions and decrease dependencies among components, utilize the Mediator Design Pattern with a centralized communication method that enables effective coordination among the various components within the system.



Requirements:

Airplane Class: Create an Airplane class representing different airplanes, with properties like ID, status (e.g., "waiting," "landing," "taking off"), and methods to request takeoff or landing.

ControlTower Mediator: Implement a ControlTower class that serves as the mediator. It will manage the interactions between airplanes and handle their requests for takeoff and landing.

Request Handling: Airplanes should be able to send requests to the control tower for takeoff or landing. The control tower should validate these requests based on the current air traffic.

Notifications: The control tower should notify airplanes when their request is approved or denied.



Implementation Steps:

Define a Mediator interface with methods for registering airplanes and handling requests.

Implement the ControlTower class that follows this interface.

Implement the Airplane class with methods to request takeoff or landing and receive notifications from the control tower.

Create a simple test case to demonstrate the functionality of the flight control system, including airplane registration and request handling.



Output:

After executing the code, it will simulate the following sequence of operations:

Airplanes are registered with the control tower.

An airplane requests to take off or land.

The control tower processes the request and sends notifications to the airplane regarding its status.



Instructions:

You only need to complete the TODOs mentioned in the code.

Please do not modify any existing code outside of the specified TODO sections.