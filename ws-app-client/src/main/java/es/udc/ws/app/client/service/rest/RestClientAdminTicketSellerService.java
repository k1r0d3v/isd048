package es.udc.ws.app.client.service.rest;

import es.udc.ws.app.client.service.ClientAdminTicketSellerService;
import es.udc.ws.app.client.service.ClientTicketSellerService;
import es.udc.ws.app.client.service.dto.ClientReservationDto;
import es.udc.ws.app.client.service.dto.ClientShowDto;
import es.udc.ws.app.client.service.exceptions.ClientLimitDateExceeded;
import es.udc.ws.app.client.service.exceptions.ClientNotEnoughAvailableTickets;
import es.udc.ws.app.client.service.rest.xml.XmlClientExceptionConversor;
import es.udc.ws.app.client.service.rest.xml.XmlClientReservationDtoConversor;
import es.udc.ws.app.client.service.rest.xml.XmlClientShowDtoConversor;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.xml.exceptions.ParsingException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Request;

import java.net.URLEncoder;
import java.util.List;

public class RestClientAdminTicketSellerService implements ClientAdminTicketSellerService
{
    private final static String ENDPOINT_ADDRESS_PARAMETER = "RestClientTicketSellerService.endpointAddress";
    private String endpointAddress;


    private synchronized String getEndpointAddress() {
        if (endpointAddress == null)
            endpointAddress = ConfigurationParametersManager.getParameter(ENDPOINT_ADDRESS_PARAMETER);
        return endpointAddress;
    }


    private void validateStatusCode(int successCode, HttpResponse response)
            throws ParsingException {

        try
        {
            int statusCode = response.getStatusLine().getStatusCode();

            // Success?
            if (statusCode == successCode)
                return;

            // Handler error.
            switch (statusCode) {

                case HttpStatus.SC_NOT_FOUND:
                    throw XmlClientExceptionConversor.fromInstanceNotFoundExceptionXml(
                            response.getEntity().getContent());

                case HttpStatus.SC_BAD_REQUEST:
                case HttpStatus.SC_GONE:
                case HttpStatus.SC_FORBIDDEN:
                    throw XmlClientExceptionConversor.fromExceptionXml(
                            response.getEntity().getContent());

                default:
                    throw new RuntimeException("HTTP error; status code = " + statusCode);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
