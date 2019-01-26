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
