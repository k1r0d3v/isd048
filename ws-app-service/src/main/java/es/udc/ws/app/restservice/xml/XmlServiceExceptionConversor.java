package es.udc.ws.app.restservice.xml;

import es.udc.ws.app.model.service.exceptions.LimitDateExceededException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.util.Calendar;


public class XmlServiceExceptionConversor {
    public final static Namespace XML_NS = Namespace.getNamespace("http://ws.udc.es/shows/xml");


    public static Document toInstanceNotFoundExceptionXml(InstanceNotFoundException ex) throws IOException {

        Element exceptionElement = new Element("InstanceNotFoundException", XML_NS);

        if (ex.getInstanceId() != null) {
            Element instanceIdElement = new Element("instanceId", XML_NS);
            instanceIdElement.setText(ex.getInstanceId().toString());

            exceptionElement.addContent(instanceIdElement);
        }

        if (ex.getInstanceType() != null) {
            Element instanceTypeElement = new Element("instanceType", XML_NS);
            instanceTypeElement.setText(ex.getInstanceType());

            exceptionElement.addContent(instanceTypeElement);
        }
        return new Document(exceptionElement);
    }

    public static Document toLimitDateExceededExceptionXml(LimitDateExceededException ex) throws IOException {

        Element exceptionElement = new Element("LimitDateExceededException", XML_NS);

        if (ex.getLimitDate() != null) {
            Element instanceIdElement = new Element("limitDate", XML_NS);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(ex.getLimitDate());
            instanceIdElement.setText(DatatypeConverter.printDateTime(calendar));

            exceptionElement.addContent(instanceIdElement);
        }

        return new Document(exceptionElement);
    }

    public static <T extends Exception> Document toExceptionXml(T t) throws IOException {
        Element exceptionElement = new Element(t.getClass().getSimpleName(), XML_NS);
        Element messageElement = new Element("message", XML_NS);
        messageElement.setText(t.getMessage());
        exceptionElement.addContent(messageElement);
        return new Document(exceptionElement);
    }
}
