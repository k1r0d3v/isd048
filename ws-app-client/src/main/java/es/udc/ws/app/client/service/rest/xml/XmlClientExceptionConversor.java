package es.udc.ws.app.client.service.rest.xml;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import es.udc.ws.app.client.service.exceptions.*;
import es.udc.ws.util.exceptions.InputValidationException;
import org.apache.http.HttpStatus;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;

import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.xml.exceptions.ParsingException;


public class XmlClientExceptionConversor {
    public final static Namespace XML_NS = 
            Namespace.getNamespace("http://ws.udc.es/shows/xml");

    public static void throwFromExceptionXml(InputStream content, int statusCode)
            throws ClientCreditCardNotCoincident, ClientLimitDateExceeded,
            ClientNotEnoughAvailableTickets, ClientReservationAlreadyChecked,
            ClientShowHasReservations, InputValidationException,
            ParsingException, InstanceNotFoundException
    {
        SAXBuilder builder = new SAXBuilder();
        Document document;
        try { document = builder.build(content); } catch (Exception e) { throw new ParsingException(e); }

        Element root = document.getRootElement();
        Element message = root.getChild("message", XML_NS);

        switch (root.getName()) {
            case "CreditCardNotCoincident":
                throw new ClientCreditCardNotCoincident(message.getText());
            case "LimitExceeded":
                throw new ClientLimitDateExceeded(message.getText());
            case "NotEnoughAvailableTickets":
                throw new ClientNotEnoughAvailableTickets(message.getText());
            case "ReservationAlreadyChecked":
                throw new ClientReservationAlreadyChecked(message.getText());
            case "ShowHasReservations":
                throw new ClientShowHasReservations(message.getText());
            case "InputValidationException":
                throw new InputValidationException(message.getText());
            case "InstanceNotFoundException":
                throw XmlClientExceptionConversor.fromInstanceNotFoundExceptionXml(root);
            default:
                throw new RuntimeException("HTTP error; status code = " + statusCode + ";\n" + message.getText());
        }
    }

    private static InstanceNotFoundException fromInstanceNotFoundExceptionXml(Element root)
            throws ParsingException
    {
        try {
            Element instanceId = root.getChild("instanceId", XML_NS);
            Element instanceType = root.getChild("instanceType", XML_NS);

            return new InstanceNotFoundException(instanceId.getText(), instanceType.getText());
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }
}
