package es.udc.ws.app.model.service.exceptions;


import java.util.Date;

public class LimitDateExceeded extends Exception
{
    private Date limitDate;

    public LimitDateExceeded(Date limitDate) {
        super("Limit date (" + limitDate + ") exceeded");
        this.limitDate = limitDate;
    }

    public Date getLimitDate() {
        return limitDate;
    }
}
