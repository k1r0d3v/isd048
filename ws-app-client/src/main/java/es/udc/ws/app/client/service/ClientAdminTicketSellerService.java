package es.udc.ws.app.client.service;

import es.udc.ws.app.client.service.dto.ClientAdminShowDto;
import es.udc.ws.app.client.service.dto.ClientShowDto;
import es.udc.ws.app.client.service.exceptions.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;


public interface ClientAdminTicketSellerService
{
    ClientAdminShowDto createShow(ClientAdminShowDto show)
            throws InputValidationException;

    void updateShow(ClientAdminShowDto show)
            throws InstanceNotFoundException, InputValidationException,
            ClientShowHasReservations, ClientNotEnoughAvailableTickets;

    ClientAdminShowDto findShow(long id)
            throws InstanceNotFoundException;

    void checkReservation(String code, String creditCard)
            throws InstanceNotFoundException, InputValidationException,
            ClientCreditCardNotCoincident, ClientReservationAlreadyChecked;
}
