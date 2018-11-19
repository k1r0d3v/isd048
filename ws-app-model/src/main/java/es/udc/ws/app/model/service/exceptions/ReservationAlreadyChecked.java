package es.udc.ws.app.model.service.exceptions;

import es.udc.ws.util.exceptions.InputValidationException;

public class ReservationAlreadyChecked extends Exception
{
    public ReservationAlreadyChecked() {
        super("Reservation already checked");
    }
}
