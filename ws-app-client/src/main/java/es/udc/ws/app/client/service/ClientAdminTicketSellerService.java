package es.udc.ws.app.client.service;

import es.udc.ws.app.client.service.dto.ClientAdminShowDto;
import es.udc.ws.app.client.service.dto.ClientShowDto;
import es.udc.ws.app.client.service.exceptions.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;


public interface ClientAdminTicketSellerService
{
    ClientShowDto createShow(ClientAdminShowDto show)
            throws InputValidationException;

    void updateShow(ClientAdminShowDto show)
            throws InstanceNotFoundException, InputValidationException,
            ClientShowHasReservations, ClientNotEnoughAvailableTickets;

    // TODO: Ask to the teacher
    // Using the client dto here makes not sense :(
    ClientShowDto findShow(long id)
            throws InstanceNotFoundException;

    void checkReservation(String code, String cardNumber)
            throws InstanceNotFoundException, InputValidationException,
            ClientCreditCardNotCoincident, ClientReservationAlreadyChecked;
}
