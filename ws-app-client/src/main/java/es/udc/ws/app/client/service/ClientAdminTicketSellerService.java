package es.udc.ws.app.client.service;

import es.udc.ws.app.client.service.dto.ClientShowDto;
import es.udc.ws.app.client.service.exceptions.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;


public interface ClientAdminTicketSellerService
{
    ClientShowDto createShow(ClientShowDto show)
            throws InputValidationException;

    void updateShow(ClientShowDto show)
            throws InstanceNotFoundException, InputValidationException,
            ClientShowHasReservations, ClientNotEnoughAvailableTickets;

    ClientShowDto findShow(long id)
            throws InstanceNotFoundException;

    void checkReservation(String code, String cardNumber)
            throws InstanceNotFoundException, InputValidationException,
            ClientCreditCardNotCoincident, ClientReservationAlreadyChecked;
}
