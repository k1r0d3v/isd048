package es.udc.ws.app.client.service.rest;

import es.udc.ws.app.client.service.ClientAdminTicketSellerService;
import es.udc.ws.app.client.service.ClientTicketSellerService;
import es.udc.ws.app.client.service.dto.ClientAdminShowDto;
import es.udc.ws.app.client.service.dto.ClientReservationDto;
import es.udc.ws.app.client.service.dto.ClientShowDto;
import es.udc.ws.app.client.service.exceptions.*;
import es.udc.ws.app.client.service.rest.xml.XmlClientAdminShowDtoConversor;
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
import org.apache.http.entity.ContentType;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;


// TODO: Implement correct exception handling
public class RestClientAdminTicketSellerService implements ClientAdminTicketSellerService
{
    private final static String ENDPOINT_ADDRESS_PARAMETER = "RestClientTicketSellerService.endpointAddress";
    private String endpointAddress;

    @Override
    public ClientShowDto createShow(ClientAdminShowDto show) throws InputValidationException {
        try {

            HttpResponse response = Request.Post(getEndpointAddress() + "shows").
                    bodyStream(toInputStream(show), ContentType.create("application/xml")).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_CREATED, response);

            return XmlClientShowDtoConversor.toClientShowDto(response.getEntity().getContent());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateShow(ClientAdminShowDto show)
            throws InstanceNotFoundException, InputValidationException,
            ClientShowHasReservations, ClientNotEnoughAvailableTickets
    {
        try {

            HttpResponse response = Request.Put(getEndpointAddress() + "shows/" + show.getId()).
                    bodyStream(toInputStream(show), ContentType.create("application/xml")).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_NO_CONTENT, response);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ClientShowDto findShow(long id)
            throws InstanceNotFoundException
    {
        try {
            HttpResponse response = Request.Get(getEndpointAddress() +
                    "shows/" + id).execute().returnResponse();

            validateStatusCode(HttpStatus.SC_OK, response);

            return XmlClientShowDtoConversor.toClientShowDto(response.getEntity().getContent());

        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void checkReservation(String code, String cardNumber)
            throws InstanceNotFoundException, InputValidationException,
            ClientCreditCardNotCoincident, ClientReservationAlreadyChecked
    {
        try
        {
            HttpResponse response = Request.Post(getEndpointAddress() + "/reservations/check" +
                    "?code=" + URLEncoder.encode(code, "UTF-8") +
                    "&creditCard=" + URLEncoder.encode(cardNumber, "UTF-8")).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_OK, response);
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

    private InputStream toInputStream(ClientAdminShowDto show) {

        try {

            ByteArrayOutputStream xmlOutputStream = new ByteArrayOutputStream();
            XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());

            outputter.output(XmlClientAdminShowDtoConversor.toXml(show), xmlOutputStream);

            return new ByteArrayInputStream(xmlOutputStream.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
