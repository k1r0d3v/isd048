package es.udc.ws.app.soapservice;

import javax.xml.ws.WebFault;

@SuppressWarnings("serial")
@WebFault(
    name="SoapCreditCardNotCoincidentException",
    targetNamespace="http://soap.ws.udc.es/"
)
public class SoapCreditCardNotCoincidentException extends Exception {

    public SoapCreditCardNotCoincidentException(String message) {
        super(message);
    }
    
    public String getFaultInfo() {
        return getMessage();
    }    
}
