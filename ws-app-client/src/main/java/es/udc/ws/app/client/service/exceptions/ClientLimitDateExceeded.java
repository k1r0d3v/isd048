package es.udc.ws.app.client.service.exceptions;


import java.util.Date;

public class ClientLimitDateExceeded extends Exception {
    private Date limitDate;

    public ClientLimitDateExceeded(Date limitDate) {
        super("Limit date (" + limitDate + ") exceeded");
        this.limitDate = limitDate;
    }

    public Date getLimitDate() {
        return limitDate;
    }
}
