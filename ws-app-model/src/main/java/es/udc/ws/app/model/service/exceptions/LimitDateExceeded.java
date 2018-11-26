package es.udc.ws.app.model.service.exceptions;


public class LimitDateExceeded extends Exception
{
    public LimitDateExceeded(String message) {
        super(message);
    }
}
