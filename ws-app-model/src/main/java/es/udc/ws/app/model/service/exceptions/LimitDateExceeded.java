package es.udc.ws.app.model.service.exceptions;

import es.udc.ws.util.exceptions.InputValidationException;

public class LimitDateExceeded extends Exception
{
    public LimitDateExceeded(String message) {
        super(message);
    }
}
