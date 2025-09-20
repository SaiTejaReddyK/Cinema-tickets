**Overview**
This project implements a TicketService for purchasing cinema tickets according to the specified business rules:

-	Maximum 25 tickets per transaction.


**Ticket types:**

 **ADULT** – £25, reserved seat

 **CHILD** – £15, reserved seat (requires at least one Adult)
 
 **INFANT**– £0, no seat (requires at least one Adult)
 
•	Correctly calculates total payment and number of seats.

•	Rejects invalid purchase requests (e.g.,children without adults, >25 tickets, invalid account).

The solution is implemented using Java 21, with a testable code. 

- Testing Framework: JUnit 5

**Key Components**

**Domain**

•	TicketTypeRequest – immutable record representing a ticket request.

**Validation**

•	TicketPurchaseValidator – handles all validation logic (SRP).

**Service**

•	TicketServiceImpl – orchestrates validation, payment, and seat reservation.

•	Integrates with TicketPaymentService and SeatReservationService (third-party stubs).

**Tests**

•	TicketPurchaseValidatorTest – unit tests covering valid and invalid scenarios.

•	Tests ensure all business rules are enforced.

•	Tests are implemented using JUnit 5.
