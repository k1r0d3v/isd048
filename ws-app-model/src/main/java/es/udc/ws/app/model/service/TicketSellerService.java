package es.udc.ws.app.model.service;

import es.udc.ws.app.model.reservation.Reservation;
import es.udc.ws.app.model.service.exceptions.LimitDateExceeded;
import es.udc.ws.app.model.service.exceptions.ReservationAlreadyChecked;
import es.udc.ws.app.model.show.Show;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.util.Calendar;
import java.util.List;


public interface TicketSellerService
{
    public Show createShow(Show show) throws InputValidationException;

    public Show findShow(Long code) throws InstanceNotFoundException;

    public List<Show> findShows(String words, Calendar start, Calendar end);

    public Reservation bookTickets(Long showId, String email, String cardNumber, int count) throws LimitDateExceeded;

    public List<Reservation> getUserReservations(String email);

    public void checkReservation(String code) throws InstanceNotFoundException, ReservationAlreadyChecked;
}
