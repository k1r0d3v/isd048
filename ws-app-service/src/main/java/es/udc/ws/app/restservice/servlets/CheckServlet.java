package es.udc.ws.app.restservice.servlets;

import es.udc.ws.app.model.service.TicketSellerServiceFactory;
import es.udc.ws.app.model.service.exceptions.CreditCardNotCoincident;
import es.udc.ws.app.model.service.exceptions.ReservationAlreadyChecked;
import es.udc.ws.app.restservice.xml.XmlServiceExceptionConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.servlet.ServletUtils;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class CheckServlet extends HttpServlet
{
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = ServletUtils.normalizePath(req.getPathInfo());

        if (path != null && path.length() != 0)
        {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    XmlServiceExceptionConversor.toExceptionXml(
                            new InputValidationException("Invalid Request: " + "invalid path " + path)),
                    null);
            return;
        }

        String code = req.getParameter("code");
        String creditCard = req.getParameter("creditCard");

        if (code == null) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    XmlServiceExceptionConversor.toExceptionXml(
                            new InputValidationException("Invalid Request: " + "parameter 'code' is mandatory")),
                    null);
            return;
        }

        if (creditCard == null) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
            XmlServiceExceptionConversor.toExceptionXml(
            new InputValidationException("Invalid Request: " + "parameter 'creditCard' is mandatory")),
            null);
                return;
        }

        try {
            TicketSellerServiceFactory.getService().checkReservation(code, creditCard);
        } catch (InputValidationException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    XmlServiceExceptionConversor.toExceptionXml(ex), null);
            return;
        } catch (CreditCardNotCoincident | ReservationAlreadyChecked ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN,
                    XmlServiceExceptionConversor.toExceptionXml(ex), null);
            return;
        } catch (InstanceNotFoundException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
                    XmlServiceExceptionConversor.toInstanceNotFoundExceptionXml(ex), null);
            return;
        }
    }
}