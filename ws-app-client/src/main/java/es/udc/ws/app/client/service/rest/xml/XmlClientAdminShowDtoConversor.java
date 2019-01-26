package es.udc.ws.app.client.service.rest.xml;

import es.udc.ws.app.client.service.dto.ClientAdminShowDto;
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

public class XmlClientAdminShowDtoConversor {

    public final static Namespace XML_NS = Namespace.getNamespace("http://ws.udc.es/shows/xml");

    public static Document toXml(ClientAdminShowDto show) throws IOException {
        Element movieElement = toJDOMElement(show);
        return new Document(movieElement);
    }

    public static Element toJDOMElement(ClientAdminShowDto show) {

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

        Element maxTicketsElement = new Element("maxTickets", XML_NS);
        maxTicketsElement.setText(Long.toString(show.getMaxTickets()));
        showElement.addContent(maxTicketsElement);

        Element avalilableTicketsElement = new Element("tickets", XML_NS);
        avalilableTicketsElement.setText(Long.toString(show.getTickets()));
        showElement.addContent(avalilableTicketsElement);

        Element realPriceElement = new Element("price", XML_NS);
        realPriceElement.setText(Float.toString(show.getPrice()));
        showElement.addContent(realPriceElement);

        Element discountedPriceElement = new Element("discountedPrice", XML_NS);
        discountedPriceElement.setText(Float.toString(show.getDiscountedPrice()));
        showElement.addContent(discountedPriceElement);

        Element commissionElement = new Element("commission", XML_NS);
        commissionElement.setText(Float.toString(show.getCommission()));
        showElement.addContent(commissionElement);

        return showElement;
    }

    public static ClientAdminShowDto toClientAdminShowDto(InputStream movieXml) throws ParsingException {
        try {

            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(movieXml);
            Element rootElement = document.getRootElement();

            return toClientAdminShowDto(rootElement);
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static ClientAdminShowDto toClientAdminShowDto(Element showElement)
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

        String maxTickets = showElement.getChildTextNormalize("maxTickets", XML_NS);

        String availableTickets = showElement.getChildTextNormalize("tickets", XML_NS);

        String realPrice = showElement.getChildTextNormalize("price", XML_NS);

        String discountedPrice = showElement.getChildTextNormalize("discountedPrice", XML_NS);

        String commission = showElement.getChildTextNormalize("commission", XML_NS);

        String likesStr = showElement.getChildTextNormalize("likes", XML_NS);

        Calendar startCalendar = DatatypeConverter.parseDateTime(startDate);
        Calendar limitCalendar = DatatypeConverter.parseDateTime(limitDate);

        return new ClientAdminShowDto(identifier, name, description, startCalendar, Long.parseLong(duration), limitCalendar, Integer.parseInt(maxTickets), Integer.parseInt(availableTickets), Float.parseFloat(realPrice), Float.parseFloat(discountedPrice), Float.parseFloat(commission), likesStr != null ? Long.parseLong(likesStr) : null);
    }
}
