package uk.gov.dwp.uc.pairtest.validation;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import java.util.Arrays;
import java.util.Objects;

public class TicketPurchaseValidator {

    private static final int MAX_TICKETS = 25;

    public void validate(Long accountId, TicketTypeRequest... requests) {
        validateAccount(accountId);
        validateRequests(requests);
        validateTicketLimit(requests);
    }

    private void validateAccount(Long accountId) {
        if (accountId == null || accountId <= 0) {
            throw new InvalidPurchaseException("Account ID must be a positive number.");
        }
    }

    private void validateRequests(TicketTypeRequest... requests) {
        if (requests == null || requests.length == 0) {
            throw new InvalidPurchaseException("At least one ticket must be requested.");
        }

        boolean hasAdult = Arrays.stream(requests)
                .filter(Objects::nonNull)
                .anyMatch(r -> r.ticketType() == TicketTypeRequest.Type.ADULT && r.noOfTickets() > 0);

        boolean hasChildOrInfant = Arrays.stream(requests)
                .filter(Objects::nonNull)
                .anyMatch(r -> (r.ticketType() == TicketTypeRequest.Type.CHILD
                             || r.ticketType() == TicketTypeRequest.Type.INFANT)
                             && r.noOfTickets() > 0);

        if (hasChildOrInfant && !hasAdult) {
            throw new InvalidPurchaseException("Child or Infant tickets require at least one Adult ticket.");
        }
    }

    private void validateTicketLimit(TicketTypeRequest... requests) {
        int totalTickets = Arrays.stream(requests)
                .filter(Objects::nonNull)
                .mapToInt(TicketTypeRequest::noOfTickets)
                .sum();

        if (totalTickets > MAX_TICKETS) {
            throw new InvalidPurchaseException("Cannot purchase more than " + MAX_TICKETS + " tickets.");
        }
    }
}
