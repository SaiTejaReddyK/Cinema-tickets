package uk.gov.dwp.uc.pairtest.domain;

public record TicketTypeRequest(Type ticketType, int noOfTickets) {

    public enum Type {
        ADULT, CHILD, INFANT
    }

    public TicketTypeRequest {
        if (noOfTickets < 0) {
            throw new IllegalArgumentException("Number of tickets cannot be negative.");
        }
        if (ticketType == null) {
            throw new IllegalArgumentException("Ticket type cannot be null.");
        }
    }
}
