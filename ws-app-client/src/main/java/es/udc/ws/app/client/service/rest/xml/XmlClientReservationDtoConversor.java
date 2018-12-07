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

    public static Document toXml(ClientReservationDto reservation) throws IOException {
        Element movieElement = toJDOMElement(reservation);
        return new Document(movieElement);
    }

    public static Document toXml(List<ClientReservationDto> reservations) throws IOException {

        Element showElements = new Element("reservations", XML_NS);
        for (ClientReservationDto i : reservations)
            showElements.addContent(toJDOMElement(i));

        return new Document(showElements);
    }

    public static Element toJDOMElement(ClientReservationDto reservation) {

        Element showElement = new Element("reservation", XML_NS);

        if (reservation.getId() != null) {
            Element identifierElement = new Element("id", XML_NS);
            identifierElement.setText(reservation.getId().toString());
            showElement.addContent(identifierElement);
        }

        Element runtimeElement = new Element("show", XML_NS);
        runtimeElement.setText(Long.toString(reservation.getShowId()));
        showElement.addContent(runtimeElement);

        Element descriptionElement = new Element("email", XML_NS);
        descriptionElement.setText(reservation.getEmail());
        showElement.addContent(descriptionElement);

        Element startDateElement = new Element("creditCard", XML_NS);
        startDateElement.setText(reservation.getCreditCard());
        showElement.addContent(startDateElement);

        Element priceElement = new Element("tickets", XML_NS);
        priceElement.setText(Integer.toString(reservation.getTickets()));
        showElement.addContent(priceElement);

        Element limitDateElement = new Element("valid", XML_NS);
        limitDateElement.setText(Boolean.toString(reservation.isValid()));
        showElement.addContent(limitDateElement);

        Element avalilableTicketsElement = new Element("code", XML_NS);
        avalilableTicketsElement.setText(reservation.getCode());
        showElement.addContent(avalilableTicketsElement);

        Element realPriceElement = new Element("reservationDate", XML_NS);
        realPriceElement.setText(DatatypeConverter.printDateTime(reservation.getReservationDate()));
        showElement.addContent(realPriceElement);

        Element discountedPriceElement = new Element("price", XML_NS);
        discountedPriceElement.setText(Float.toString(reservation.getPrice()));
        showElement.addContent(discountedPriceElement);

        return showElement;
    }

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
