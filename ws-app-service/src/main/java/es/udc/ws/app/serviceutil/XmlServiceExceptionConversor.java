package es.udc.ws.app.serviceutil;

import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;

import java.io.IOException;
import java.io.PrintWriter;


public class XmlServiceExceptionConversor {
    public final static String CONVERSION_PATTERN = "EEE, d MMM yyyy HH:mm:ss Z";

    public final static Namespace XML_NS = Namespace.getNamespace("http://ws.udc.es/shows/xml");


    public static Document toInstanceNotFoundException(InstanceNotFoundException ex) throws IOException {

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

    public static <T extends Exception> Document toXml(T t) throws IOException {
        Element exceptionElement = new Element(t.getClass().getName(), XML_NS);
        Element messageElement = new Element("message", XML_NS);
        messageElement.setText(t.getMessage());
        exceptionElement.addContent(messageElement);
        return new Document(exceptionElement);
    }
}
