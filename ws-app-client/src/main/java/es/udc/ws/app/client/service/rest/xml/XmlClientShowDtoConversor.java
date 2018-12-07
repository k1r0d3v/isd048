package es.udc.ws.app.client.service.rest.xml;

import es.udc.ws.app.client.service.dto.ClientShowDto;
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
import java.util.concurrent.TimeUnit;

public class XmlClientShowDtoConversor {

    public final static Namespace XML_NS = Namespace.getNamespace("http://ws.udc.es/shows/xml");

    public static Document toXml(ClientShowDto show) throws IOException {
        Element movieElement = toJDOMElement(show);
        return new Document(movieElement);
    }

    public static Document toXml(List<ClientShowDto> shows) throws IOException {

        Element showElements = new Element("shows", XML_NS);
        for (ClientShowDto i : shows)
            showElements.addContent(toJDOMElement(i));

        return new Document(showElements);
    }

    public static Element toJDOMElement(ClientShowDto show) {

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
        long diffInMillis = show.getEndDate().getTime().getTime() - show.getStartDate().getTime().getTime();
        priceElement.setText(Long.toString(TimeUnit.MINUTES.convert(diffInMillis, TimeUnit.MILLISECONDS)));
        showElement.addContent(priceElement);

        Element limitDateElement = new Element("limitDate", XML_NS);
        limitDateElement.setText(DatatypeConverter.printDateTime(show.getLimitDate()));
        showElement.addContent(limitDateElement);

        Element avalilableTicketsElement = new Element("tickets", XML_NS);
        avalilableTicketsElement.setText(Long.toString(show.getTickets()));
        showElement.addContent(avalilableTicketsElement);

        Element realPriceElement = new Element("price", XML_NS);
        realPriceElement.setText(Float.toString(show.getPrice()));
        showElement.addContent(realPriceElement);

        Element discountedPriceElement = new Element("discountedPrice", XML_NS);
        discountedPriceElement.setText(Float.toString(show.getDiscountedPrice()));
        showElement.addContent(discountedPriceElement);

        return showElement;
    }

    public static List<ClientShowDto> toClientShowDtos(InputStream movieXml)
            throws ParsingException {
        try {

            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(movieXml);
            Element rootElement = document.getRootElement();

            if (!"shows".equalsIgnoreCase(rootElement.getName())) {
                throw new ParsingException("Unrecognized element '"
                        + rootElement.getName() + "' ('shows' expected)");
            }
            List<Element> children = rootElement.getChildren();
            List<ClientShowDto> movieDtos = new ArrayList<>(children.size());
            for (Element element : children)
                movieDtos.add(toClientShowDto(element));

            return movieDtos;
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    public static ClientShowDto toClientShowDto(InputStream movieXml) throws ParsingException {
        try {

            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(movieXml);
            Element rootElement = document.getRootElement();

            return toClientShowDto(rootElement);
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static ClientShowDto toClientShowDto(Element showElement)
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

        String availableTickets = showElement.getChildTextNormalize("tickets", XML_NS);

        String realPrice = showElement.getChildTextNormalize("price", XML_NS);

        String discountedPrice = showElement.getChildTextNormalize("discountedPrice", XML_NS);


        Calendar startCalendar = DatatypeConverter.parseDateTime(startDate);
        Calendar limitCalendar = DatatypeConverter.parseDateTime(limitDate);

        Calendar endCalendar = (Calendar)startCalendar.clone();
        endCalendar.add(Calendar.MINUTE, (int)Long.parseLong(duration));

        return new ClientShowDto(identifier, name, description, startCalendar, endCalendar, limitCalendar, Integer.parseInt(availableTickets), Float.parseFloat(realPrice), Float.parseFloat(discountedPrice));
    }
}
