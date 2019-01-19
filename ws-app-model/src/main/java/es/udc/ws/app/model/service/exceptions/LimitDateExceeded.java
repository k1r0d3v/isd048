package es.udc.ws.app.model.service.exceptions;


import java.util.Date;

public class LimitDateExceeded extends Exception
{
    public LimitDateExceeded(Date date) {
        super("Limit date (" + date + ") exceeded");
    }
}
