package es.udc.ws.app.soapservice;

import javax.xml.ws.WebFault;

@SuppressWarnings("serial")
@WebFault(
    name="SoapReservationAlreadyCheckedException",
    targetNamespace="http://soap.ws.udc.es/"
)
public class SoapReservationAlreadyCheckedException extends Exception {

    public SoapReservationAlreadyCheckedException(String message) {
        super(message);
    }
    
    public String getFaultInfo() {
        return getMessage();
    }    
}
