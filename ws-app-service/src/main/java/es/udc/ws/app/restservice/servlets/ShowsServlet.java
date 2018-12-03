package es.udc.ws.app.restservice.servlets;

import es.udc.ws.app.dto.ServiceShowDto;
import es.udc.ws.app.model.service.TicketSellerServiceFactory;
import es.udc.ws.app.model.show.Show;
import es.udc.ws.app.service.restservice.ShowToShowDto;
import es.udc.ws.app.serviceutil.XmlServiceExceptionConversor;
import es.udc.ws.app.serviceutil.XmlServiceShowDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.servlet.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ShowsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = ServletUtils.normalizePath(req.getPathInfo());
        if (path == null || path.length() == 0) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    XmlServiceExceptionConversor.toInputValidationExceptionXml(
                            new InputValidationException("Invalid Request: " + "invalid show id")),
                    null);
            return;
        }
        String showIdAsString = path.substring(1);
        Long showId = null;
        try {
            showId = Long.valueOf(showIdAsString);
        } catch (NumberFormatException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    XmlServiceExceptionConversor.toInputValidationExceptionXml(
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

        ServiceShowDto saleDto = ShowToShowDto.toShowDto(show);

        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
                XmlServiceShowDtoConversor.toXml(saleDto), null);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }
}
