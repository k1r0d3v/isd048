package es.udc.ws.app.restservice.servlets;

import es.udc.ws.app.dto.ServiceShowAdminDto;
import es.udc.ws.app.dto.ServiceShowDto;
import es.udc.ws.app.model.service.exceptions.NotEnoughAvailableTicketsException;
import es.udc.ws.app.model.service.exceptions.ShowHasReservationsException;
import es.udc.ws.app.model.show.Show;
import es.udc.ws.app.service.TicketSellerProxyServiceFactory;
import es.udc.ws.app.serviceutil.ShowToDto;
import es.udc.ws.app.restservice.xml.XmlServiceExceptionConversor;
import es.udc.ws.app.restservice.xml.XmlServiceShowAdminDtoConversor;
import es.udc.ws.app.restservice.xml.XmlServiceShowDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.servlet.ServletUtils;
import es.udc.ws.util.xml.exceptions.ParsingException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ShowsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = ServletUtils.normalizePath(req.getPathInfo());

        if (path == null)
        {
            String keywordsParam = req.getParameter("keywords");
            if (keywordsParam == null) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                        XmlServiceExceptionConversor.toExceptionXml(
                                new InputValidationException("Invalid Request: " + "parameter 'keywords' is mandatory")),
                        null);
                return;
            }


            List<ServiceShowDto> shows;
            try {
                shows = TicketSellerProxyServiceFactory.getService().findShows(keywordsParam);
            } catch (InputValidationException ex) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                        XmlServiceExceptionConversor.toExceptionXml(ex), null);
                return;
            }

            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
                    XmlServiceShowDtoConversor.toXml(shows), null);
        }
        else
        {
            if (path.length() == 0) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                        XmlServiceExceptionConversor.toExceptionXml(
                                new InputValidationException("Invalid Request: " + "invalid path " + path)),
                        null);
                return;
            }

            String showIdAsString = path.substring(1);
            Long showId;
            try {
                showId = Long.valueOf(showIdAsString);
            } catch (NumberFormatException ex) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                        XmlServiceExceptionConversor.toExceptionXml(
                                new InputValidationException("Invalid Request: " + "invalid show id '" + showIdAsString)),
                        null);
                return;
            }

            ServiceShowAdminDto show;
            try {
                show = TicketSellerProxyServiceFactory.getService().findShow(showId);
            } catch (InstanceNotFoundException ex) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
                        XmlServiceExceptionConversor.toInstanceNotFoundExceptionXml(ex), null);
                return;
            }

            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
                    XmlServiceShowAdminDtoConversor.toXml(show), null);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = ServletUtils.normalizePath(req.getPathInfo());
        if (path != null && path.length() > 0) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    XmlServiceExceptionConversor.toExceptionXml(
                            new InputValidationException("Invalid Request: " + "invalid path " + path)),
                    null);
            return;
        }
        ServiceShowAdminDto showAdminDto;
        try {
            showAdminDto = XmlServiceShowAdminDtoConversor.toServiceShowAdminDto(req.getInputStream());
        } catch (ParsingException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST, XmlServiceExceptionConversor
                    .toExceptionXml(new InputValidationException(ex.getMessage())), null);

            return;
        }

        try {
            showAdminDto = TicketSellerProxyServiceFactory.getService().createShow(showAdminDto);
        } catch (InputValidationException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    XmlServiceExceptionConversor.toExceptionXml(ex), null);
            return;
        }

        String showLocation = ServletUtils.normalizePath(req.getRequestURL().toString()) + "/" + showAdminDto.getId().toString();

        Map<String, String> headers = new HashMap<>(1);
        headers.put("Location", showLocation);

        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CREATED,
                XmlServiceShowAdminDtoConversor.toXml(showAdminDto), headers);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = ServletUtils.normalizePath(req.getPathInfo());

        if (path == null || path.length() == 0) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    XmlServiceExceptionConversor.toExceptionXml(
                            new InputValidationException("Invalid Request: " + "invalid show id")),
                    null);
            return;
        }

        String showIdAsString = path.substring(1);
        Long showId;
        try {
            showId = Long.valueOf(showIdAsString);
        } catch (NumberFormatException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    XmlServiceExceptionConversor.toExceptionXml(new InputValidationException(
                            "Invalid Request: " + "invalid show id '" + showIdAsString + "'")),
                    null);
            return;
        }

        ServiceShowAdminDto showAdminDto;
        try {
            showAdminDto = XmlServiceShowAdminDtoConversor.toServiceShowAdminDto(req.getInputStream());
        } catch (ParsingException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST, XmlServiceExceptionConversor
                    .toExceptionXml(new InputValidationException(ex.getMessage())), null);
            return;

        }
        if (!showId.equals(showAdminDto.getId())) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    XmlServiceExceptionConversor.toExceptionXml(
                            new InputValidationException("Invalid Request: " + "invalid show id")),
                    null);
            return;
        }

        try {
            // Notes: The model only updates the necessary fields
            TicketSellerProxyServiceFactory.getService().updateShow(showAdminDto);
            showAdminDto = TicketSellerProxyServiceFactory.getService().findShow(showAdminDto.getId());
        } catch (InputValidationException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    XmlServiceExceptionConversor.toExceptionXml(ex), null);
            return;
        } catch (NotEnoughAvailableTicketsException ex) {
            // Temporal error, no other error code matches
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
                    XmlServiceExceptionConversor.toExceptionXml(ex), null);
            return;
        } catch (ShowHasReservationsException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN,
                    XmlServiceExceptionConversor.toExceptionXml(ex), null);
            return;
        } catch (InstanceNotFoundException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
                    XmlServiceExceptionConversor.toInstanceNotFoundExceptionXml(ex), null);
            return;
        }

        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NO_CONTENT, null, null);
    }
}
