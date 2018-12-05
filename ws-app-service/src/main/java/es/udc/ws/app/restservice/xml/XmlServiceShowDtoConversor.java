package es.udc.ws.app.restservice.xml;

import es.udc.ws.app.dto.ServiceShowDto;
import es.udc.ws.util.xml.exceptions.ParsingException;
import org.jdom2.DataConversionException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class XmlServiceShowDtoConversor {

    public final static Namespace XML_NS = Namespace.getNamespace("http://ws.udc.es/shows/xml");

    public static Document toXml(ServiceShowDto show) throws IOException {
        Element movieElement = toJDOMElement(show);
        return new Document(movieElement);
    }

    public static Document toXml(List<ServiceShowDto> shows) throws IOException {

        Element showElements = new Element("shows", XML_NS);
        for (ServiceShowDto i : shows)
            showElements.addContent(toJDOMElement(i));

        return new Document(showElements);
    }

    public static Element toJDOMElement(ServiceShowDto show) {

        Element showElement = new Element("show", XML_NS);

        if (show.getId() != null) {
            Element identifierElement = new Element("id", XML_NS);
            identifierElement.setText(show.getId().toString());
            showElement.addContent(identifierElement);
        }

        Element runtimeElement = new Element("name", XML_NS);
        runtimeElement.setText(show.getName());
        showElement.addContent(runtimeElement);

        Element descriptionElement = new Element("description", XML_NS);
        descriptionElement.setText(show.getDescription());
        showElement.addContent(descriptionElement);

        Element startDateElement = new Element("startDate", XML_NS);
        startDateElement.setText(DatatypeConverter.printDateTime(show.getStartDate()));
        showElement.addContent(startDateElement);

        Element priceElement = new Element("duration", XML_NS);
        priceElement.setText(Long.toString(show.getDuration()));
        showElement.addContent(priceElement);

        Element limitDateElement = new Element("limitDate", XML_NS);
        limitDateElement.setText(DatatypeConverter.printDateTime(show.getLimitDate()));
        showElement.addContent(limitDateElement);

        Element avalilableTicketsElement = new Element("availableTickets", XML_NS);
        avalilableTicketsElement.setText(Long.toString(show.getAvailableTickets()));
        showElement.addContent(avalilableTicketsElement);

        Element realPriceElement = new Element("realPrice", XML_NS);
        realPriceElement.setText(Float.toString(show.getRealPrice()));
        showElement.addContent(realPriceElement);

        Element discountedPriceElement = new Element("discountedPrice", XML_NS);
        discountedPriceElement.setText(Float.toString(show.getDiscountedPrice()));
        showElement.addContent(discountedPriceElement);

        return showElement;
    }

    public static ServiceShowDto toServiceShowDto(InputStream movieXml) throws ParsingException {
        try {

            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(movieXml);
            Element rootElement = document.getRootElement();

            return toServiceShowDto(rootElement);
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static ServiceShowDto toServiceShowDto(Element showElement)
            throws ParsingException, DataConversionException {

        if (!"show".equals(showElement.getName())) {
            throw new ParsingException("Unrecognized element '" + showElement.getName() + "' ('show' expected)");
        }
        Element identifierElement = showElement.getChild("id", XML_NS);
        Long identifier = null;
        if (identifierElement != null) {
            identifier = Long.valueOf(identifierElement.getTextTrim());
        }

        String name = showElement.getChildTextNormalize("name", XML_NS);

        String description = showElement.getChildTextNormalize("description", XML_NS);

        String startDate = showElement.getChildTextNormalize("startDate", XML_NS);

        String duration = showElement.getChildTextNormalize("duration", XML_NS);

        String limitDate = showElement.getChildTextNormalize("limitDate", XML_NS);

        String availableTickets = showElement.getChildTextNormalize("availableTickets", XML_NS);

        String realPrice = showElement.getChildTextNormalize("realPrice", XML_NS);

        String discountedPrice = showElement.getChildTextNormalize("discountedPrice", XML_NS);


        Calendar startCalendar = DatatypeConverter.parseDateTime(startDate);
        Calendar limitCalendar = DatatypeConverter.parseDateTime(limitDate);

        return new ServiceShowDto(identifier, name, description, startCalendar, Long.parseLong(duration), limitCalendar, Integer.parseInt(availableTickets), Float.parseFloat(realPrice), Float.parseFloat(discountedPrice));
    }
}
