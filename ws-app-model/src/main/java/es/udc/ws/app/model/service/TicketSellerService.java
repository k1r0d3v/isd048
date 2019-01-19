package es.udc.ws.app.model.service;

import es.udc.ws.app.model.reservation.Reservation;
import es.udc.ws.app.model.service.exceptions.*;
import es.udc.ws.app.model.show.Show;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.util.Calendar;
import java.util.List;


public interface TicketSellerService
{
    Show createShow(Show show) throws InputValidationException;

    void updateShow(Show show)
            throws InstanceNotFoundException, InputValidationException, ShowHasReservationsException, NotEnoughAvailableTicketsException;

    Show findShow(long id) throws InstanceNotFoundException;

    List<Show> findShows(String keywords, Calendar start, Calendar end) throws InputValidationException;

    Reservation buyTickets(long showId, String email, String creditCard, int count) throws InstanceNotFoundException, InputValidationException, NotEnoughAvailableTicketsException, LimitDateExceededException;

    List<Reservation> getUserReservations(String email) throws InputValidationException;

    void checkReservation(String code, String creditCard) throws InstanceNotFoundException, InputValidationException, CreditCardNotCoincidentException, ReservationAlreadyCheckedException;
}
