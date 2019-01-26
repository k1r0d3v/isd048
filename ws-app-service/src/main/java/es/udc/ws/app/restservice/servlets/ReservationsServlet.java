package es.udc.ws.app.restservice.servlets;

import es.udc.ws.app.dto.ServiceReservationDto;
import es.udc.ws.app.model.reservation.Reservation;
import es.udc.ws.app.model.service.exceptions.CreditCardNotCoincidentException;
import es.udc.ws.app.model.service.exceptions.LimitDateExceededException;
import es.udc.ws.app.model.service.exceptions.NotEnoughAvailableTicketsException;
import es.udc.ws.app.model.service.exceptions.ReservationAlreadyCheckedException;
import es.udc.ws.app.model.show.Show;
import es.udc.ws.app.service.TicketSellerProxyServiceFactory;
import es.udc.ws.app.serviceutil.ReservationToDto;
import es.udc.ws.app.restservice.xml.XmlServiceExceptionConversor;
import es.udc.ws.app.restservice.xml.XmlServiceReservationDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.servlet.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


public class ReservationsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
    {
        String path = ServletUtils.normalizePath(req.getPathInfo());

        if (path != null && path.length() != 0) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    XmlServiceExceptionConversor.toExceptionXml(
                            new InputValidationException("Invalid Request: " + "invalid path " + path)),
                    null);
            return;
        }

        String emailParam = req.getParameter("email");
        if (emailParam == null) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    XmlServiceExceptionConversor.toExceptionXml(
                            new InputValidationException("Invalid Request: " + "parameter 'email' is mandatory")),
                    null);
            return;
        }

        List<ServiceReservationDto> reservations;
        try {
            reservations = TicketSellerProxyServiceFactory.getService().getUserReservations(emailParam);
        } catch (InputValidationException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    XmlServiceExceptionConversor.toExceptionXml(ex), null);
            return;
        }

        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
                XmlServiceReservationDtoConversor.toXml(reservations), null);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String path = ServletUtils.normalizePath(req.getPathInfo());

        if (path != null)
        {
            if (path.equals("/check"))
            {
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
                    TicketSellerProxyServiceFactory.getService().checkReservation(code, creditCard);
                } catch (InputValidationException ex) {
                    ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                            XmlServiceExceptionConversor.toExceptionXml(ex), null);
                    return;
                } catch (CreditCardNotCoincidentException | ReservationAlreadyCheckedException ex) {
                    ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN,
                            XmlServiceExceptionConversor.toExceptionXml(ex), null);
                    return;
                } catch (InstanceNotFoundException ex) {
                    ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
                            XmlServiceExceptionConversor.toInstanceNotFoundExceptionXml(ex), null);
                    return;
                }

                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NO_CONTENT, null, null);
            } else {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                        XmlServiceExceptionConversor.toExceptionXml(
                                new InputValidationException("Invalid Request: " + "invalid path " + path)),
                        null);
            }
            return;
        }

        String showId = req.getParameter("show");
        String email = req.getParameter("email");
        String creditCard = req.getParameter("creditCard");
        String count = req.getParameter("count");

        if (showId == null) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    XmlServiceExceptionConversor.toExceptionXml(
                            new InputValidationException("Invalid Request: " + "parameter 'show' is mandatory")),
                    null);
            return;
        }

        if (email == null) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    XmlServiceExceptionConversor.toExceptionXml(
                            new InputValidationException("Invalid Request: " + "parameter 'email' is mandatory")),
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

        if (count == null) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    XmlServiceExceptionConversor.toExceptionXml(
                            new InputValidationException("Invalid Request: " + "parameter 'count' is mandatory")),
                    null);
            return;
        }

        ServiceReservationDto reservationDto;
        try {
            reservationDto = TicketSellerProxyServiceFactory.getService().buyTickets(Long.parseLong(showId), email, creditCard, Integer.parseInt(count));
        } catch (InputValidationException | NumberFormatException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    XmlServiceExceptionConversor.toExceptionXml(ex), null);
            return;
        } catch (NotEnoughAvailableTicketsException ex) {
            // Available tickets can grow, then can not be a permanent error
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
                    XmlServiceExceptionConversor.toExceptionXml(ex), null);
            return;
        }
        catch (LimitDateExceededException ex) {
            // Limit date can change then can not be a permanent error
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
                    XmlServiceExceptionConversor.toLimitDateExceededExceptionXml(ex), null);
            return;
        } catch (InstanceNotFoundException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
                    XmlServiceExceptionConversor.toInstanceNotFoundExceptionXml(ex), null);
            return;
        }

        // Location not needed here? reservations can not be accessed by id
        /*
        String reservationLocation = ServletUtils.normalizePath(req.getRequestURL().toString()) + "/" + reservation.getId().toString();

        Map<String, String> headers = new HashMap<>(1);
        headers.put("Location", reservationLocation);
        */

        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CREATED,
                XmlServiceReservationDtoConversor.toXml(reservationDto), null/*headers*/);
    }
}
