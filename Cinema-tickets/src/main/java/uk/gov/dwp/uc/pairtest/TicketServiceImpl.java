package uk.gov.dwp.uc.pairtest;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.validation.TicketPurchaseValidator;
import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;

import java.util.Arrays;
import java.util.Objects;

public class TicketServiceImpl implements TicketService {

    private static final int ADULT_PRICE = 25;
    private static final int CHILD_PRICE = 15;
    private static final int INFANT_PRICE = 0;

    private final TicketPaymentService paymentService;
    private final SeatReservationService reservationService;
    private final TicketPurchaseValidator validator;

    public TicketServiceImpl(TicketPaymentService paymentService,
                             SeatReservationService reservationService,
                             TicketPurchaseValidator validator) {
        this.paymentService = Objects.requireNonNull(paymentService);
        this.reservationService = Objects.requireNonNull(reservationService);
        this.validator = Objects.requireNonNull(validator);
    }

    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests)
            throws InvalidPurchaseException {

        validator.validate(accountId, ticketTypeRequests);

        int totalAmount = calculateTotalAmount(ticketTypeRequests);
        int totalSeats = calculateTotalSeats(ticketTypeRequests);

        paymentService.makePayment(accountId, totalAmount);
        reservationService.reserveSeat(accountId, totalSeats);
    }

    private int calculateTotalAmount(TicketTypeRequest... requests) {
        return Arrays.stream(requests)
                .filter(Objects::nonNull)
                .mapToInt(r -> switch (r.ticketType()) {
                    case ADULT -> r.noOfTickets() * ADULT_PRICE;
                    case CHILD -> r.noOfTickets() * CHILD_PRICE;
                    case INFANT -> r.noOfTickets() * INFANT_PRICE;
                })
                .sum();
    }

    private int calculateTotalSeats(TicketTypeRequest... requests) {
        return Arrays.stream(requests)
                .filter(Objects::nonNull)
                .filter(r -> r.ticketType() == TicketTypeRequest.Type.ADULT
                          || r.ticketType() == TicketTypeRequest.Type.CHILD)
                .mapToInt(TicketTypeRequest::noOfTickets)
                .sum();
    }
}
