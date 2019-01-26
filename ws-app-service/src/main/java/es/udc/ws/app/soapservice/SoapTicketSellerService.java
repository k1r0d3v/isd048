package es.udc.ws.app.soapservice;

import es.udc.ws.app.dto.ServiceReservationDto;
import es.udc.ws.app.dto.ServiceShowAdminDto;
import es.udc.ws.app.dto.ServiceShowDto;
import es.udc.ws.app.model.reservation.Reservation;
import es.udc.ws.app.model.service.exceptions.*;
import es.udc.ws.app.model.show.Show;
import es.udc.ws.app.service.TicketSellerProxyServiceFactory;
import es.udc.ws.app.serviceutil.ReservationToDto;
import es.udc.ws.app.serviceutil.ShowToDto;
import es.udc.ws.app.socialnetwork.SocialNetworkServiceFactory;
import es.udc.ws.app.socialnetwork.SocialNetworkShowPost;
import es.udc.ws.app.socialnetwork.exceptions.AuthenticationException;
import es.udc.ws.app.socialnetwork.exceptions.DuplicatedPostException;
import es.udc.ws.app.socialnetwork.exceptions.PostNotFountException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import javax.jws.WebService;
import java.util.Calendar;
import java.util.List;

@WebService(
        name = "TicketSellerProvider",
        serviceName = "TicketSellerProviderService",
        targetNamespace = "http://soap.ws.udc.es/"
)
public class SoapTicketSellerService
{
    public ServiceShowAdminDto createShow(ServiceShowAdminDto show)
            throws SoapInputValidationException
    {
        try {
            show = TicketSellerProxyServiceFactory.getService().createShow(show);
        } catch (InputValidationException e) {
            throw new SoapInputValidationException(e.getMessage());
        }

        return show;
    }

    public void updateShow(ServiceShowAdminDto show)
            throws SoapInputValidationException, SoapShowHasReservationsException,
            SoapInstanceNotFoundException, SoapNotEnoughAvailableTicketsException
    {
        try {
            TicketSellerProxyServiceFactory.getService().updateShow(show);
        } catch (InputValidationException e) {
            throw new SoapInputValidationException(e.getMessage());
        } catch (NotEnoughAvailableTicketsException e) {
            throw new SoapNotEnoughAvailableTicketsException(e.getMessage());
        } catch (ShowHasReservationsException e) {
            throw new SoapShowHasReservationsException(e.getMessage());
        } catch (InstanceNotFoundException e) {
            throw new SoapInstanceNotFoundException(new SoapInstanceNotFoundExceptionInfo(e.getInstanceId(), e.getInstanceType()));
        }
    }

    public ServiceShowAdminDto findShow(long id)
            throws SoapInstanceNotFoundException
    {
        ServiceShowAdminDto show;
        try {
            show = TicketSellerProxyServiceFactory.getService().findShow(id);
        } catch (InstanceNotFoundException e) {
            throw new SoapInstanceNotFoundException(new SoapInstanceNotFoundExceptionInfo(e.getInstanceId(), e.getInstanceType()));
        }
        return show;
    }

    public List<ServiceShowDto> findShows(String keywords)
            throws SoapInputValidationException
    {
        List<ServiceShowDto> shows;
        try {
            shows = TicketSellerProxyServiceFactory.getService().findShows(keywords);
        } catch (InputValidationException e) {
            throw new SoapInputValidationException(e.getMessage());
        }

        return shows;
    }

    public ServiceReservationDto buyTickets(long showId, String email, String creditCard, int count)
            throws SoapInputValidationException, SoapInstanceNotFoundException,
            SoapLimitDateExceededException, SoapNotEnoughAvailableTicketsException
    {
        ServiceReservationDto reservation;
        try {
            reservation = TicketSellerProxyServiceFactory.getService().buyTickets(showId, email, creditCard, count);
        } catch (InputValidationException | NumberFormatException e) {
            throw new SoapInputValidationException(e.getMessage());
        } catch (NotEnoughAvailableTicketsException e) {
            throw new SoapNotEnoughAvailableTicketsException(e.getMessage());
        } catch (LimitDateExceededException e) {
            throw new SoapLimitDateExceededException(new SoapLimitDateExceededExceptionInfo(e.getLimitDate()));
        } catch (InstanceNotFoundException e) {
            throw new SoapInstanceNotFoundException(new SoapInstanceNotFoundExceptionInfo(e.getInstanceId(), e.getInstanceType()));
        }

        return reservation;
    }

    public List<ServiceReservationDto> getUserReservations(String email)
            throws SoapInputValidationException
    {
        List<ServiceReservationDto> reservations;
        try {
            reservations = TicketSellerProxyServiceFactory.getService().getUserReservations(email);
        } catch (InputValidationException e) {
            throw new SoapInputValidationException(e.getMessage());
        }

        return reservations;
    }

    public void checkReservation(String code, String creditCard)
            throws SoapInstanceNotFoundException, SoapInputValidationException,
            SoapCreditCardNotCoincidentException, SoapReservationAlreadyCheckedException
    {
        try {
            TicketSellerProxyServiceFactory.getService().checkReservation(code, creditCard);
        } catch (InputValidationException e) {
            throw new SoapInputValidationException(e.getMessage());
        } catch (CreditCardNotCoincidentException e) {
            throw new SoapCreditCardNotCoincidentException(e.getMessage());
        } catch (ReservationAlreadyCheckedException e) {
            throw new SoapReservationAlreadyCheckedException(e.getMessage());
        } catch (InstanceNotFoundException e) {
            throw new SoapInstanceNotFoundException(new SoapInstanceNotFoundExceptionInfo(e.getInstanceId(), e.getInstanceType()));
        }
    }
}
