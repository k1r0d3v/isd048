package es.udc.ws.app.serviceutil;

import es.udc.ws.app.dto.ServiceShowAdminDto;
import es.udc.ws.util.xml.exceptions.ParsingException;
import org.jdom2.DataConversionException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class XmlServiceShowAdminDtoConversor {

    public final static Namespace XML_NS = Namespace.getNamespace("http://ws.udc.es/shows/xml");

    public static Document toXml(ServiceShowAdminDto show) throws IOException {
        Element movieElement = toJDOMElement(show);
        return new Document(movieElement);
    }

    public static Document toXml(List<ServiceShowAdminDto> shows) throws IOException {

        Element showElements = new Element("shows", XML_NS);
        for (ServiceShowAdminDto i : shows)
            showElements.addContent(toJDOMElement(i));

        return new Document(showElements);
    }

    public static Element toJDOMElement(ServiceShowAdminDto show) {

        Element showElement = new Element("show", XML_NS);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ"){
            public Date parse(String source, ParsePosition pos) {
                return super.parse(source.replaceFirst(":(?=[0-9]{2}$)",""),pos);
            }
        };

        if (show.getId() != null) {
            Element identifierElement = new Element("showId", XML_NS);
            identifierElement.setText(show.getId().toString());
            showElement.addContent(identifierElement);
        }

        Element runtimeElement = new Element("name", XML_NS);
        runtimeElement.setText(show.getName());
        showElement.addContent(runtimeElement);

        Element descriptionElement = new Element("description", XML_NS);
        descriptionElement.setText(show.getName());
        showElement.addContent(descriptionElement);

        Element startDateElement = new Element("startDate", XML_NS);
        startDateElement.setText(format.format(show.getStartDate().getTime()));
        showElement.addContent(startDateElement);

        Element priceElement = new Element("duration", XML_NS);
        priceElement.setText(Long.toString(show.getDuration()));
        showElement.addContent(priceElement);

        Element limitDateElement = new Element("limitDate", XML_NS);
        limitDateElement.setText(format.format(show.getLimitDate()));
        showElement.addContent(limitDateElement);

        Element maxTicketsElement = new Element("maxTickets", XML_NS);
        maxTicketsElement.setText(format.format(show.getLimitDate()));
        showElement.addContent(maxTicketsElement);

        Element avalilableTicketsElement = new Element("availableTickets", XML_NS);
        avalilableTicketsElement.setText(Long.toString(show.getAvailableTickets()));
        showElement.addContent(avalilableTicketsElement);

        Element realPriceElement = new Element("realPrice", XML_NS);
        realPriceElement.setText(Float.toString(show.getRealPrice()));
        showElement.addContent(realPriceElement);

        Element discountedPriceElement = new Element("discountedPrice", XML_NS);
        discountedPriceElement.setText(Float.toString(show.getDiscountedPrice()));
        showElement.addContent(discountedPriceElement);

        Element salesCommissionElement = new Element("salesCommission", XML_NS);
        salesCommissionElement.setText(Float.toString(show.getDiscountedPrice()));
        showElement.addContent(salesCommissionElement);

        return showElement;
    }

    public static ServiceShowAdminDto toServiceShowDto(InputStream movieXml) throws ParsingException {
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

    private static ServiceShowAdminDto toServiceShowDto(Element showElement)
            throws ParsingException, DataConversionException {

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ"){
            public Date parse(String source, ParsePosition pos) {
                return super.parse(source.replaceFirst(":(?=[0-9]{2}$)",""),pos);
            }
        };

        if (!"show".equals(showElement.getName())) {
            throw new ParsingException("Unrecognized element '" + showElement.getName() + "' ('show' expected)");
        }
        Element identifierElement = showElement.getChild("showId", XML_NS);
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

        String availableTickets = showElement.getChildTextNormalize("availableTickets", XML_NS);

        String realPrice = showElement.getChildTextNormalize("realPrice", XML_NS);

        String discountedPrice = showElement.getChildTextNormalize("discountedPrice", XML_NS);

        String salesCommission = showElement.getChildTextNormalize("salesCommission", XML_NS);


        Calendar startCalendar = Calendar.getInstance();
        Calendar limitCalendar = Calendar.getInstance();
        try {
            startCalendar.setTime(format.parse(startDate));
        } catch (ParseException e) {
            throw new DataConversionException(startDate, "Calendar");
        }

        try {
            limitCalendar.setTime(format.parse(limitDate));
        } catch (ParseException e) {
            throw new DataConversionException(startDate, "Calendar");
        }

        return new ServiceShowAdminDto(identifier, name, description, startCalendar, Long.parseLong(duration), limitCalendar, Integer.parseInt(maxTickets), Integer.parseInt(availableTickets), Float.parseFloat(realPrice), Float.parseFloat(discountedPrice), Float.parseFloat(salesCommission));
    }
}
