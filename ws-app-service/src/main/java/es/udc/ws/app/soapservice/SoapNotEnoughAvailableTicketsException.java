package es.udc.ws.app.soapservice;

import javax.xml.ws.WebFault;

@SuppressWarnings("serial")
@WebFault(
    name="SoapNotEnoughAvailableTicketsException",
    targetNamespace="http://soap.ws.udc.es/"
)
public class SoapNotEnoughAvailableTicketsException extends Exception {

    public SoapNotEnoughAvailableTicketsException(String message) {
        super(message);
    }
    
    public String getFaultInfo() {
        return getMessage();
    }    
}
