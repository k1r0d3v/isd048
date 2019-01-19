package es.udc.ws.app.client.service;


import es.udc.ws.app.client.service.dto.ClientReservationDto;
import es.udc.ws.app.client.service.dto.ClientShowDto;
import es.udc.ws.app.client.service.exceptions.ClientLimitDateExceeded;
import es.udc.ws.app.client.service.exceptions.ClientNotEnoughAvailableTickets;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.util.Calendar;
import java.util.List;

public interface ClientTicketSellerService
{
    List<ClientShowDto> findShows(String keywords) throws InputValidationException;

    ClientReservationDto buyTickets(long showId, String email, String creditCard, int count) throws InstanceNotFoundException, InputValidationException, ClientNotEnoughAvailableTickets, ClientLimitDateExceeded;

    List<ClientReservationDto> getUserReservations(String email) throws InputValidationException;
}
