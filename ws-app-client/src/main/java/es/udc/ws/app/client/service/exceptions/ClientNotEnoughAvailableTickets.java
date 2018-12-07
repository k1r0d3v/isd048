package es.udc.ws.app.client.service.exceptions;

public class ClientNotEnoughAvailableTickets extends Exception {
    public ClientNotEnoughAvailableTickets(String msg) {
        super(msg);
    }
}
