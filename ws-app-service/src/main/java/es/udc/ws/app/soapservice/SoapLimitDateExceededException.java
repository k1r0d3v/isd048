package es.udc.ws.app.soapservice;

import javax.xml.ws.WebFault;

@SuppressWarnings("serial")
@WebFault(
    name="SoapLimitDateExceededException",
    targetNamespace="http://soap.ws.udc.es/"
)
public class SoapLimitDateExceededException extends Exception {

    private SoapLimitDateExceededExceptionInfo faultInfo;

    protected SoapLimitDateExceededException(
            SoapLimitDateExceededExceptionInfo faultInfo) {
        this.faultInfo = faultInfo;
    }

    public SoapLimitDateExceededExceptionInfo getFaultInfo() {
        return faultInfo;
    }
}
