package es.udc.ws.app.client.service.rest.xml;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import es.udc.ws.app.client.service.exceptions.ClientReservationAlreadyChecked;
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
            throws ParsingException {
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

    public static Exception fromExceptionXml(InputStream ex)
            throws ParsingException {
        try {

            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(ex);
            Element rootElement = document.getRootElement();
            Element message = rootElement.getChild("message", XML_NS);

            return new Exception(message.getText());
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }
}
