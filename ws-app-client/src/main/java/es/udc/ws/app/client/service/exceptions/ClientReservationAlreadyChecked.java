package es.udc.ws.app.client.service.exceptions;


public class ClientReservationAlreadyChecked extends Exception
{
    public ClientReservationAlreadyChecked() {
        super("Reservation already checked");
    }
}
