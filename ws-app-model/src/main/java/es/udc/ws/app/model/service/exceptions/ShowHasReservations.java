package es.udc.ws.app.model.service.exceptions;

import es.udc.ws.util.exceptions.InputValidationException;

public class ShowHasReservations extends Exception
{
    public ShowHasReservations(String message) {
        super(message);
    }
}
