package es.udc.ws.app.model.service.exceptions;


import java.util.Date;

public class LimitDateExceededException extends Exception
{
    private Date limitDate;

    public LimitDateExceededException(Date limitDate) {
        super("Limit date (" + limitDate + ") exceeded");
        this.limitDate = limitDate;
    }

    public Date getLimitDate() {
        return limitDate;
    }
}
