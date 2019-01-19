package es.udc.ws.app.model.service.exceptions;

public class NotEnoughAvailableTicketsException extends Exception {
    public NotEnoughAvailableTicketsException(String msg) {
        super(msg);
    }
}
