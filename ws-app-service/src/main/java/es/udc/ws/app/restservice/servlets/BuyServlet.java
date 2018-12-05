package es.udc.ws.app.restservice.servlets;

import es.udc.ws.app.dto.ServiceReservationDto;
import es.udc.ws.app.model.reservation.Reservation;
import es.udc.ws.app.model.service.TicketSellerServiceFactory;
import es.udc.ws.app.model.service.exceptions.LimitDateExceeded;
import es.udc.ws.app.model.service.exceptions.NotEnoughAvailableTickets;
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

/*
Reservation buyTickets(long showId, String email, String cardNumber, int count) throws InstanceNotFoundException, InputValidationException, NotEnoughAvailableTickets, LimitDateExceeded;
*/

public class BuyServlet extends HttpServlet
{
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = ServletUtils.normalizePath(req.getPathInfo());

        if (path != null && path.length() != 0)
        {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    XmlServiceExceptionConversor.convertException(
                            new InputValidationException("Invalid Request: " + "invalid path " + path)),
                    null);
            return;
        }

        String showId = req.getParameter("show");
        String email = req.getParameter("email");
        String creditCard = req.getParameter("creditCard");
        String count = req.getParameter("count");

        if (showId == null) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    XmlServiceExceptionConversor.convertException(
                            new InputValidationException("Invalid Request: " + "parameter 'show' is mandatory")),
                    null);
            return;
        }

        if (email == null) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    XmlServiceExceptionConversor.convertException(
                            new InputValidationException("Invalid Request: " + "parameter 'email' is mandatory")),
                    null);
            return;
        }

        if (creditCard == null) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    XmlServiceExceptionConversor.convertException(
                            new InputValidationException("Invalid Request: " + "parameter 'creditCard' is mandatory")),
                    null);
            return;
        }

        if (count == null) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    XmlServiceExceptionConversor.convertException(
                            new InputValidationException("Invalid Request: " + "parameter 'count' is mandatory")),
                    null);
            return;
        }

        Reservation reservation;
        try {
            reservation = TicketSellerServiceFactory.getService().buyTickets(Long.parseLong(showId), email, creditCard, Integer.parseInt(count));
        } catch (InputValidationException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    XmlServiceExceptionConversor.convertException(ex), null);
            return;
        } catch (NotEnoughAvailableTickets ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
                    XmlServiceExceptionConversor.convertException(ex), null);
            return;
        } catch (InstanceNotFoundException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
                    XmlServiceExceptionConversor.convertInstanceNotFoundException(ex), null);
            return;
        } catch (LimitDateExceeded ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
                    XmlServiceExceptionConversor.convertException(ex), null);
            return;
        }

        ServiceReservationDto reservationDto = ReservationToDto.toReservationDto(reservation);
        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
                XmlServiceReservationDtoConversor.toXml(reservationDto), null);
    }
}
