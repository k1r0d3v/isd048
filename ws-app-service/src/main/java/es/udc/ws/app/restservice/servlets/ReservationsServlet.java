package es.udc.ws.app.restservice.servlets;

import es.udc.ws.app.model.reservation.Reservation;
import es.udc.ws.app.model.service.TicketSellerServiceFactory;
import es.udc.ws.app.serviceutil.ReservationToDto;
import es.udc.ws.app.restservice.xml.XmlServiceExceptionConversor;
import es.udc.ws.app.restservice.xml.XmlServiceReservationDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.servlet.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


public class ReservationsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = ServletUtils.normalizePath(req.getPathInfo());

        if (path == null)
        {
            String emailParam = req.getParameter("email");
            if (emailParam == null) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                        XmlServiceExceptionConversor.toExceptionXml(
                                new InputValidationException("Invalid Request: " + "parameter 'email' is mandatory")),
                        null);
                return;
            }

            List<Reservation> reservations;
            try {
                reservations = TicketSellerServiceFactory.getService().getUserReservations(emailParam);
            } catch (InputValidationException ex) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
                        XmlServiceExceptionConversor.toExceptionXml(ex), null);
                return;
            }

            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
                    XmlServiceReservationDtoConversor.toXml(ReservationToDto.toReservationDtos(reservations)), null);
        }
    }
}
