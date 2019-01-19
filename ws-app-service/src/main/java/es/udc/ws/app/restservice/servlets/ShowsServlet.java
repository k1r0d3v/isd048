package es.udc.ws.app.restservice.servlets;

import es.udc.ws.app.dto.ServiceShowAdminDto;
import es.udc.ws.app.dto.ServiceShowDto;
import es.udc.ws.app.model.service.TicketSellerServiceFactory;
import es.udc.ws.app.model.service.exceptions.NotEnoughAvailableTickets;
import es.udc.ws.app.model.service.exceptions.ShowHasReservations;
import es.udc.ws.app.model.show.Show;
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


            Calendar start = Calendar.getInstance();
            start.set(Calendar.MONTH, Calendar.DECEMBER);
            start.set(Calendar.DAY_OF_MONTH, 17);
            Calendar end = (Calendar)start.clone();
            end.add(Calendar.DAY_OF_YEAR, 30);


            List<Show> shows;
            try {
                shows = TicketSellerServiceFactory.getService().findShows(keywordsParam, start, end);
            } catch (InputValidationException ex) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                        XmlServiceExceptionConversor.toExceptionXml(ex), null);
                return;
            }

            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
                    XmlServiceShowDtoConversor.toXml(ShowToDto.toShowDtos(shows)), null);
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

            Show show;
            try {
                show = TicketSellerServiceFactory.getService().findShow(showId);
            } catch (InstanceNotFoundException ex) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
                        XmlServiceExceptionConversor.toInstanceNotFoundExceptionXml(ex), null);
                return;
            }

            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
                    XmlServiceShowDtoConversor.toXml(ShowToDto.toShowDto(show)), null);
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
        ServiceShowAdminDto xmlshow;
        try {
            xmlshow = XmlServiceShowAdminDtoConversor.toServiceShowAdminDto(req.getInputStream());
        } catch (ParsingException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST, XmlServiceExceptionConversor
                    .toExceptionXml(new InputValidationException(ex.getMessage())), null);

            return;
        }

        Show show = ShowToDto.toShow(xmlshow);
        try {
            show = TicketSellerServiceFactory.getService().createShow(show);
        } catch (InputValidationException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    XmlServiceExceptionConversor.toExceptionXml(ex), null);
            return;
        }
        ServiceShowDto showDto = ShowToDto.toShowDto(show);

        String showLocation = ServletUtils.normalizePath(req.getRequestURL().toString()) + "/" + show.getId().toString();

        Map<String, String> headers = new HashMap<>(1);
        headers.put("Location", showLocation);

        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CREATED,
                XmlServiceShowDtoConversor.toXml(showDto), headers);
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

        ServiceShowAdminDto showDto;
        try {
            showDto = XmlServiceShowAdminDtoConversor.toServiceShowAdminDto(req.getInputStream());
        } catch (ParsingException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST, XmlServiceExceptionConversor
                    .toExceptionXml(new InputValidationException(ex.getMessage())), null);
            return;

        }
        if (!showId.equals(showDto.getId())) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    XmlServiceExceptionConversor.toExceptionXml(
                            new InputValidationException("Invalid Request: " + "invalid show id")),
                    null);
            return;
        }

        Show show = ShowToDto.toShow(showDto);
        try {
            TicketSellerServiceFactory.getService().updateShow(show);
        } catch (InputValidationException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    XmlServiceExceptionConversor.toExceptionXml(ex), null);
            return;
        } catch (NotEnoughAvailableTickets ex) {
            // Temporal error, no other error code matches
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
                    XmlServiceExceptionConversor.toExceptionXml(ex), null);
            return;
        } catch (ShowHasReservations ex) {
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
