package es.udc.ws.app.client.service.exceptions;


public class ClientLimitDateExceeded extends Exception {
    public ClientLimitDateExceeded(String message) {
        super(message);
    }
}
