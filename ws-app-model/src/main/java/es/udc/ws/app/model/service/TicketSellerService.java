package es.udc.ws.app.model.service;

import es.udc.ws.app.model.reservation.Reservation;
import es.udc.ws.app.model.service.exceptions.LimitDateExceeded;
import es.udc.ws.app.model.service.exceptions.ReservationAlreadyChecked;
import es.udc.ws.app.model.service.exceptions.ShowHasReservations;
import es.udc.ws.app.model.show.Show;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.util.Calendar;
import java.util.List;


public interface TicketSellerService
{
    Show createShow(Show show) throws InputValidationException;

    void updateShow(Show show)
            throws InstanceNotFoundException, InputValidationException;

    Show findShow(Long code) throws InstanceNotFoundException;

    List<Show> findShows(String words, Calendar start, Calendar end);

    Reservation buyTickets(Long showId, String email, String cardNumber, int count) throws InstanceNotFoundException, InputValidationException;

    List<Reservation> getUserReservations(String email);

    void checkReservation(String code, String cardNumber) throws InstanceNotFoundException, InputValidationException;
}
