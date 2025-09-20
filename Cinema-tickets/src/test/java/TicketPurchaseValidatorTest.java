package uk.gov.dwp.uc.pairtest.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import static org.junit.jupiter.api.Assertions.*;

class TicketPurchaseValidatorTest {

    private TicketPurchaseValidator validator;

    @BeforeEach
    void setUp() {
        validator = new TicketPurchaseValidator();
    }

    @Test
    void shouldPassWhenAdultTicketOnly() {
        TicketTypeRequest request = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 2);
        assertDoesNotThrow(() -> validator.validate(1L, request));
    }

    @Test
    void shouldPassWhenAdultWithChildrenAndInfants() {
        TicketTypeRequest adult = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1);
        TicketTypeRequest child = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 2);
        TicketTypeRequest infant = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1);

        assertDoesNotThrow(() -> validator.validate(1L, adult, child, infant));
    }

    @Test
    void shouldFailWhenAccountIdIsInvalid() {
        TicketTypeRequest request = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1);
        assertThrows(InvalidPurchaseException.class,
                () -> validator.validate(0L, request));
    }

    @Test
    void shouldFailWhenNoTicketsRequested() {
        assertThrows(InvalidPurchaseException.class,
                () -> validator.validate(1L));
    }

    @Test
    void shouldFailWhenOnlyChildrenWithoutAdult() {
        TicketTypeRequest child = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 2);
        assertThrows(InvalidPurchaseException.class,
                () -> validator.validate(1L, child));
    }

    @Test
    void shouldFailWhenOnlyInfantsWithoutAdult() {
        TicketTypeRequest infant = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1);
        assertThrows(InvalidPurchaseException.class,
                () -> validator.validate(1L, infant));
    }

    @Test
    void shouldFailWhenTotalTicketsExceedLimit() {
        TicketTypeRequest adult = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 26);
        assertThrows(InvalidPurchaseException.class,
                () -> validator.validate(1L, adult));
    }

    @Test
    void shouldAllowExactlyMaxTickets() {
        TicketTypeRequest adult = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 25);
        assertDoesNotThrow(() -> validator.validate(1L, adult));
    }
}
