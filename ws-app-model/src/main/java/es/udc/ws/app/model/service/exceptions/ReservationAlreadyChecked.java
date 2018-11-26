package es.udc.ws.app.model.service.exceptions;


public class ReservationAlreadyChecked extends Exception
{
    public ReservationAlreadyChecked() {
        super("Reservation already checked");
    }
}
