package es.udc.ws.app.soapservice;

import java.util.Date;

public class SoapLimitDateExceededExceptionInfo {

    private Date limitDate;

    public SoapLimitDateExceededExceptionInfo() {
    }

    public SoapLimitDateExceededExceptionInfo(Date limitDate) {
        this.limitDate = limitDate;
    }

    public Date getLimitDate() {
        return limitDate;
    }

    public void setLimitDate(Date limitDate) {
        this.limitDate = limitDate;
    }
}
