package es.udc.ws.app.service;

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

import java.util.Calendar;
import java.util.List;

public class TicketSellerDefaultProxyService implements TicketSellerProxyService {
    @Override
    public ServiceShowAdminDto createShow(ServiceShowAdminDto show) throws InputValidationException {
        Show modelShow = TicketSellerServiceFactory.getService().createShow(ShowToDto.toShow(show));
        return ShowToDto.toShowAdminDto(modelShow);
    }

    @Override
    public void updateShow(ServiceShowAdminDto show) throws InstanceNotFoundException, InputValidationException, ShowHasReservationsException, NotEnoughAvailableTicketsException {
        Show modelShow = ShowToDto.toShow(show);
        TicketSellerServiceFactory.getService().updateShow(modelShow);
    }

    @Override
    public ServiceShowAdminDto findShow(long id) throws InstanceNotFoundException {
        Show modelShow = TicketSellerServiceFactory.getService().findShow(id);
        return ShowToDto.toShowAdminDto(modelShow);
    }

    @Override
    public List<ServiceShowDto> findShows(String keywords) throws InputValidationException {
        Calendar start = Calendar.getInstance();
        Calendar end = (Calendar)start.clone();
        end.add(Calendar.DAY_OF_YEAR, 30);
        List<Show> modelShows = TicketSellerServiceFactory.getService().findShows(keywords, start, end);

        return ShowToDto.toShowDtos(modelShows);
    }

    @Override
    public ServiceReservationDto buyTickets(long showId, String email, String creditCard, int count) throws InstanceNotFoundException, InputValidationException, NotEnoughAvailableTicketsException, LimitDateExceededException {
        Reservation modelReservation = TicketSellerServiceFactory.getService().buyTickets(showId, email, creditCard, count);
        return ReservationToDto.toReservationDto(modelReservation);
    }

    @Override
    public List<ServiceReservationDto> getUserReservations(String email) throws InputValidationException {
        List<Reservation> modelReservations = TicketSellerServiceFactory.getService().getUserReservations(email);
        return ReservationToDto.toReservationDtos(modelReservations);
    }

    @Override
    public void checkReservation(String code, String creditCard) throws InstanceNotFoundException, InputValidationException, CreditCardNotCoincidentException, ReservationAlreadyCheckedException {
        TicketSellerServiceFactory.getService().checkReservation(code, creditCard);
    }
}
