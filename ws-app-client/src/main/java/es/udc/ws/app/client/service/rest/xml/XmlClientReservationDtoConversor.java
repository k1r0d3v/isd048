package es.udc.ws.app.client.service.rest.xml;

import es.udc.ws.app.client.service.dto.ClientReservationDto;
import es.udc.ws.util.xml.exceptions.ParsingException;
import org.jdom2.DataConversionException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class XmlClientReservationDtoConversor
{
    public final static Namespace XML_NS = Namespace.getNamespace("http://ws.udc.es/reservations/xml");

    public static List<ClientReservationDto> toClientReservationDtos(InputStream movieXml)
            throws ParsingException {
        try {

            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(movieXml);
            Element rootElement = document.getRootElement();

            if (!"reservations".equalsIgnoreCase(rootElement.getName())) {
                throw new ParsingException("Unrecognized element '"
                        + rootElement.getName() + "' ('reservations' expected)");
            }
            List<Element> children = rootElement.getChildren();
            List<ClientReservationDto> movieDtos = new ArrayList<>(children.size());
            for (Element element : children)
                movieDtos.add(toClientReservationDto(element));

            return movieDtos;
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    public static ClientReservationDto toClientReservationDto(InputStream movieXml) throws ParsingException {
        try {

            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(movieXml);
            Element rootElement = document.getRootElement();

            return toClientReservationDto(rootElement);
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static ClientReservationDto toClientReservationDto(Element reservationElement)
            throws ParsingException, DataConversionException {

        if (!"reservation".equals(reservationElement.getName())) {
            throw new ParsingException("Unrecognized element '" + reservationElement.getName() + "' ('reservation' expected)");
        }
        Element identifierElement = reservationElement.getChild("id", XML_NS);
        Long identifier = null;
        if (identifierElement != null) {
            identifier = Long.valueOf(identifierElement.getTextTrim());
        }

        String showId = reservationElement.getChildTextNormalize("show", XML_NS);

        String email = reservationElement.getChildTextNormalize("email", XML_NS);

        String cardNumber = reservationElement.getChildTextNormalize("creditCard", XML_NS);

        String tickets = reservationElement.getChildTextNormalize("tickets", XML_NS);

        String isValid = reservationElement.getChildTextNormalize("valid", XML_NS);

        String code = reservationElement.getChildTextNormalize("code", XML_NS);

        String date = reservationElement.getChildTextNormalize("reservationDate", XML_NS);

        String price = reservationElement.getChildTextNormalize("price", XML_NS);

        Calendar dateCalendar = DatatypeConverter.parseDateTime(date);

        return new ClientReservationDto(identifier, Long.parseLong(showId), email, cardNumber,
                Integer.parseInt(tickets), Boolean.parseBoolean(isValid), code, dateCalendar, Float.parseFloat(price));
    }
}
