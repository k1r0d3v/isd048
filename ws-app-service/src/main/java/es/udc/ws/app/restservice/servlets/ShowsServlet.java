package es.udc.ws.app.restservice.servlets;

import es.udc.ws.app.dto.ServiceShowAdminDto;
import es.udc.ws.app.dto.ServiceShowDto;
import es.udc.ws.app.model.service.TicketSellerServiceFactory;
import es.udc.ws.app.model.service.exceptions.NotEnoughAvailableTickets;
import es.udc.ws.app.model.service.exceptions.ShowHasReservations;
import es.udc.ws.app.model.show.Show;
import es.udc.ws.app.service.restservice.ShowToDto;
import es.udc.ws.app.serviceutil.XmlServiceExceptionConversor;
import es.udc.ws.app.serviceutil.XmlServiceShowAdminDtoConversor;
import es.udc.ws.app.serviceutil.XmlServiceShowDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.servlet.ServletUtils;
import es.udc.ws.util.xml.exceptions.ParsingException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;


public class ShowsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = ServletUtils.normalizePath(req.getPathInfo());

        if (path == null)
        {
            String keywordsParam = req.getParameter("keywords");
            if (keywordsParam == null) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                        XmlServiceExceptionConversor.toXml(
                                new InputValidationException("Invalid Request: " + "parameter 'keywords' is mandatory")),
                        null);
                return;
            }

            String startParam = req.getParameter("start");
            String endParam = req.getParameter("end");

            Calendar start = null;
            Calendar end = null;

            if (startParam != null) {
                try {
                    start = DatatypeConverter.parseDateTime(startParam);
                } catch (Exception e) {
                    ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                            XmlServiceExceptionConversor.toXml(new InputValidationException("Invalid start date format")),null);
                    return;
                }
                if (endParam != null) {
                    try {
                        end = DatatypeConverter.parseDateTime(endParam);
                    } catch (Exception e) {
                        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                                XmlServiceExceptionConversor.toXml(new InputValidationException("Invalid start date format")),null);
                        return;
                    }
                }
            }

            List<Show> shows;
            try {
                shows = TicketSellerServiceFactory.getService().findShows(keywordsParam, start, end);
            } catch (InputValidationException ex) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
                        XmlServiceExceptionConversor.toXml(ex), null);
                return;
            }

            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
                    XmlServiceShowDtoConversor.toXml(ShowToDto.toShowDtos(shows)), null);
        }
        else
        {
            if (path.length() == 0) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                        XmlServiceExceptionConversor.toXml(
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
                        XmlServiceExceptionConversor.toXml(
                                new InputValidationException("Invalid Request: " + "invalid show id '" + showIdAsString)),
                        null);
                return;
            }

            Show show;
            try {
                show = TicketSellerServiceFactory.getService().findShow(showId);
            } catch (InstanceNotFoundException ex) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
                        XmlServiceExceptionConversor.toInstanceNotFoundException(ex), null);
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
                    XmlServiceExceptionConversor.toXml(
                            new InputValidationException("Invalid Request: " + "invalid path " + path)),
                    null);
            return;
        }
        ServiceShowAdminDto xmlshow;
        try {
            xmlshow = XmlServiceShowAdminDtoConversor.toServiceShowAdminDto(req.getInputStream());
        } catch (ParsingException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST, XmlServiceExceptionConversor
                    .toXml(new InputValidationException(ex.getMessage())), null);

            return;

        }

        Show show = ShowToDto.toShow(xmlshow);
        try {
            show = TicketSellerServiceFactory.getService().createShow(show);
        } catch (InputValidationException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    XmlServiceExceptionConversor.toXml(ex), null);
            return;
        }
        ServiceShowDto showDto = ShowToDto.toShowDto(show);

        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CREATED,
                XmlServiceShowDtoConversor.toXml(showDto), null);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = ServletUtils.normalizePath(req.getPathInfo());

        if (path == null || path.length() == 0) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    XmlServiceExceptionConversor.toXml(
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
                    XmlServiceExceptionConversor.toXml(new InputValidationException(
                            "Invalid Request: " + "invalid show id '" + showIdAsString + "'")),
                    null);
            return;
        }

        ServiceShowAdminDto showDto;
        try {
            showDto = XmlServiceShowAdminDtoConversor.toServiceShowAdminDto(req.getInputStream());
        } catch (ParsingException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST, XmlServiceExceptionConversor
                    .toXml(new InputValidationException(ex.getMessage())), null);
            return;

        }
        if (!showId.equals(showDto.getId())) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    XmlServiceExceptionConversor.toXml(
                            new InputValidationException("Invalid Request: " + "invalid show id")),
                    null);
            return;
        }

        Show show = ShowToDto.toShow(showDto);
        try {
            TicketSellerServiceFactory.getService().updateShow(show);
        } catch (InputValidationException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    XmlServiceExceptionConversor.toXml(ex), null);
            return;
        } catch (NotEnoughAvailableTickets ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN,
                    XmlServiceExceptionConversor.toXml(ex), null);
            return;
        } catch (ShowHasReservations ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN,
                    XmlServiceExceptionConversor.toXml(ex), null);
            return;
        } catch (InstanceNotFoundException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
                    XmlServiceExceptionConversor.toInstanceNotFoundException(ex), null);
            return;
        }
        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NO_CONTENT, null, null);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = ServletUtils.normalizePath(req.getPathInfo());
        if (path == null || path.length() == 0) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    XmlServiceExceptionConversor.toXml(
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
                    XmlServiceExceptionConversor.toXml(new InputValidationException(
                            "Invalid Request: " + "invalid show id '" + showIdAsString + "'")),
                    null);

            return;
        }
        /*
        TODO: Implement remove Show in service?????
        try {
            TicketSellerServiceFactory.getService().removeShow(showId);
        } catch (InstanceNotFoundException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
                    XmlServiceExceptionConversor.toInstanceNotFoundException(ex), null);
            return;
        }
        */
        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NO_CONTENT, null, null);
    }
}
