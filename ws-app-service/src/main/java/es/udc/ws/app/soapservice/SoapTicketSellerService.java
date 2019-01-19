package es.udc.ws.app.soapservice;

import es.udc.ws.app.dto.ServiceReservationDto;
import es.udc.ws.app.dto.ServiceShowAdminDto;
import es.udc.ws.app.dto.ServiceShowDto;
import es.udc.ws.app.model.reservation.Reservation;
import es.udc.ws.app.model.service.TicketSellerServiceFactory;
import es.udc.ws.app.model.service.exceptions.*;
import es.udc.ws.app.model.show.Show;
import es.udc.ws.app.serviceutil.ReservationToDto;
import es.udc.ws.app.serviceutil.ShowToDto;
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
    public ServiceShowDto createShow(ServiceShowAdminDto showAdminDto)
            throws SoapInputValidationException
    {
        Show show = ShowToDto.toShow(showAdminDto);
        try {
            show = TicketSellerServiceFactory.getService().createShow(show);
        } catch (InputValidationException ex) {
            throw new SoapInputValidationException(ex.getMessage());
        }

        return ShowToDto.toShowDto(show);
    }

    public void updateShow(ServiceShowAdminDto showAdminDto)
            throws SoapInputValidationException, SoapShowHasReservationsException,
            SoapInstanceNotFoundException, SoapNotEnoughAvailableTicketsException
    {
        Show show = ShowToDto.toShow(showAdminDto);
        try {
            TicketSellerServiceFactory.getService().updateShow(show);
        } catch (InputValidationException ex) {
            throw new SoapInputValidationException(ex.getMessage());
        } catch (NotEnoughAvailableTickets ex) {
            throw new SoapNotEnoughAvailableTicketsException(ex.getMessage());
        } catch (ShowHasReservations ex) {
            throw new SoapShowHasReservationsException(ex.getMessage());
        } catch (InstanceNotFoundException ex) {
            throw new SoapInstanceNotFoundException(new SoapInstanceNotFoundExceptionInfo(ex.getInstanceId(), ex.getInstanceType()));
        }
    }

    ServiceShowDto findShow(long id)
            throws SoapInstanceNotFoundException
    {
        Show show;
        try {
            show = TicketSellerServiceFactory.getService().findShow(id);
        } catch (InstanceNotFoundException ex) {
            throw new SoapInstanceNotFoundException(new SoapInstanceNotFoundExceptionInfo(ex.getInstanceId(), ex.getInstanceType()));
        }
        return ShowToDto.toShowDto(show);
    }

    List<ServiceShowDto> findShows(String keywords)
            throws SoapInputValidationException
    {
        Calendar start = Calendar.getInstance();
        start.set(Calendar.MONTH, Calendar.DECEMBER);
        start.set(Calendar.DAY_OF_MONTH, 17);
        Calendar end = (Calendar)start.clone();
        end.add(Calendar.DAY_OF_YEAR, 30);

        List<Show> shows;
        try {
            shows = TicketSellerServiceFactory.getService().findShows(keywords, start, end);
        } catch (InputValidationException ex) {
            throw new SoapInputValidationException(ex.getMessage());
        }

        return ShowToDto.toShowDtos(shows);
    }

    ServiceReservationDto buyTickets(long showId, String email, String creditCard, int count)
            throws SoapInputValidationException, SoapInstanceNotFoundException,
            SoapLimitDateExceededException, SoapNotEnoughAvailableTicketsException
    {
        Reservation reservation;
        try {
            reservation = TicketSellerServiceFactory.getService().buyTickets(showId, email, creditCard, count);
        } catch (InputValidationException | NumberFormatException ex) {
            throw new SoapInputValidationException(ex.getMessage());
        } catch (NotEnoughAvailableTickets ex) {
            throw new SoapNotEnoughAvailableTicketsException(ex.getMessage());
        } catch (LimitDateExceeded ex) {
            throw new SoapLimitDateExceededException(new SoapLimitDateExceededExceptionInfo(ex.getLimitDate()));
        } catch (InstanceNotFoundException ex) {
            throw new SoapInstanceNotFoundException(new SoapInstanceNotFoundExceptionInfo(ex.getInstanceId(), ex.getInstanceType()));
        }

        return ReservationToDto.toReservationDto(reservation);
    }

    List<ServiceReservationDto> getUserReservations(String email)
            throws SoapInputValidationException
    {
        List<Reservation> reservations;
        try {
            reservations = TicketSellerServiceFactory.getService().getUserReservations(email);
        } catch (InputValidationException ex) {
            throw new SoapInputValidationException(ex.getMessage());
        }

        return ReservationToDto.toReservationDtos(reservations);
    }

    void checkReservation(String code, String creditCard)
            throws SoapInstanceNotFoundException, SoapInputValidationException,
            SoapCreditCardNotCoincidentException, SoapReservationAlreadyCheckedException
    {
        try {
            TicketSellerServiceFactory.getService().checkReservation(code, creditCard);
        } catch (InputValidationException ex) {
            throw new SoapInputValidationException(ex.getMessage());
        } catch (CreditCardNotCoincident ex) {
            throw new SoapCreditCardNotCoincidentException(ex.getMessage());
        } catch (ReservationAlreadyChecked ex) {
            throw new SoapReservationAlreadyCheckedException(ex.getMessage());
        } catch (InstanceNotFoundException ex) {
            throw new SoapInstanceNotFoundException(new SoapInstanceNotFoundExceptionInfo(ex.getInstanceId(), ex.getInstanceType()));
        }
    }
}
