package es.udc.ws.app.soapservice;

import javax.xml.ws.WebFault;

@SuppressWarnings("serial")
@WebFault(
    name="SoapShowHasReservationsException",
    targetNamespace="http://soap.ws.udc.es/"
)
public class SoapShowHasReservationsException extends Exception {

    public SoapShowHasReservationsException(String message) {
        super(message);
    }
    
    public String getFaultInfo() {
        return getMessage();
    }    
}
