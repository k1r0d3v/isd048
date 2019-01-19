package es.udc.ws.app.model.service.exceptions;


public class ReservationAlreadyCheckedException extends Exception
{
    public ReservationAlreadyCheckedException() {
        super("Reservation already checked");
    }
}
