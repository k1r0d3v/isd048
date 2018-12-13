package es.udc.ws.app.client.service.rest.xml;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import es.udc.ws.app.client.service.exceptions.*;
import es.udc.ws.util.exceptions.InputValidationException;
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

    public static InstanceNotFoundException fromInstanceNotFoundExceptionXml(InputStream ex) 
            throws ParsingException
    {
        try {

            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(ex);
            Element rootElement = document.getRootElement();

            Element instanceId = rootElement.getChild("instanceId", XML_NS);
            Element instanceType = rootElement.getChild("instanceType", XML_NS);

            return new InstanceNotFoundException(instanceId.getText(), instanceType.getText());
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    public static void throwFromExceptionXml(InputStream ex)
            throws ClientCreditCardNotCoincident, ClientLimitDateExceeded,
            ClientNotEnoughAvailableTickets, ClientReservationAlreadyChecked,
            ClientShowHasReservations, ClientUnknownException, InputValidationException, ParsingException
    {
        SAXBuilder builder = new SAXBuilder();
        Document document;
        try { document = builder.build(ex); } catch (Exception e) { throw new ParsingException(e); }

        Element rootElement = document.getRootElement();
        Element message = rootElement.getChild("message", XML_NS);

        switch (rootElement.getName()) {
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
        }

        throw new ClientUnknownException(message.getText());
    }
}
