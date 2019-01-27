package es.udc.ws.app.service;

import es.udc.ws.app.dto.ServiceReservationDto;
import es.udc.ws.app.dto.ServiceShowAdminDto;
import es.udc.ws.app.dto.ServiceShowDto;
import es.udc.ws.app.model.service.exceptions.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.util.List;

public interface TicketSellerProxyService {

    /*
     * Admin
     */

    ServiceShowAdminDto createShow(ServiceShowAdminDto show) throws InputValidationException;

    void updateShow(ServiceShowAdminDto show) throws InstanceNotFoundException, InputValidationException, ShowHasReservationsException, NotEnoughAvailableTicketsException;

    ServiceShowAdminDto findShow(long id) throws InstanceNotFoundException;

    void checkReservation(String code, String creditCard) throws InstanceNotFoundException, InputValidationException, CreditCardNotCoincidentException, ReservationAlreadyCheckedException;

    /*
     * Client
     */

    List<ServiceShowDto> findShows(String keywords) throws InputValidationException;

    ServiceReservationDto buyTickets(long showId, String email, String creditCard, int count) throws InstanceNotFoundException, InputValidationException, NotEnoughAvailableTicketsException, LimitDateExceededException;

    List<ServiceReservationDto> getUserReservations(String email) throws InputValidationException;
}