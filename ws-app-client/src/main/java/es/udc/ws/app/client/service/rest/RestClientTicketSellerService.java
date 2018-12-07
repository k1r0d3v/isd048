package es.udc.ws.app.client.service.rest;

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

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

public class RestClientTicketSellerService implements ClientTicketSellerService
{
    private final static String ENDPOINT_ADDRESS_PARAMETER = "RestClientMovieService.endpointAddress";
    private String endpointAddress;

    @Override
    public List<ClientShowDto> findShows(String keywords) throws InputValidationException {
        try {

            HttpResponse response = Request.Get(getEndpointAddress() + "shows?keywords="
                    + URLEncoder.encode(keywords, "UTF-8")).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_OK, response);

            return XmlClientShowDtoConversor.toClientShowDtos(response.getEntity().getContent());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ClientReservationDto buyTickets(long showId, String email, String cardNumber, int count) throws InstanceNotFoundException, InputValidationException, ClientNotEnoughAvailableTickets, ClientLimitDateExceeded {
        try {
            HttpResponse response = Request.Post(getEndpointAddress() + "buy" +
                    "?show=" + URLEncoder.encode(Long.toString(showId), "UTF-8") +
                    "&email=" + URLEncoder.encode(email, "UTF-8") +
                    "&creditCard=" + URLEncoder.encode(cardNumber, "UTF-8") +
                    "&count=" + URLEncoder.encode(Integer.toString(count), "UTF-8")).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_OK, response);

            return XmlClientReservationDtoConversor.toClientReservationDto(response.getEntity().getContent());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ClientReservationDto> getUserReservations(String email) throws InputValidationException {
        try {

            HttpResponse response = Request.Get(getEndpointAddress() + "reservations?email="
                    + URLEncoder.encode(email, "UTF-8")).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_OK, response);

            return XmlClientReservationDtoConversor.toClientReservationDtos(response.getEntity().getContent());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

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
