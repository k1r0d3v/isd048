package es.udc.ws.app.model.service.exceptions;

public class NotEnoughAvailableTickets extends Exception {
    public NotEnoughAvailableTickets(String msg) {
        super(msg);
    }
}
