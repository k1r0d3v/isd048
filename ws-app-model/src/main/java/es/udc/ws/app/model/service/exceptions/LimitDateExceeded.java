package es.udc.ws.app.model.service.exceptions;

import es.udc.ws.util.exceptions.InputValidationException;

public class LimitDateExceeded extends InputValidationException
{
    public LimitDateExceeded(String message) {
        super(message);
    }
}
