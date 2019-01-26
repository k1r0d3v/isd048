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
import es.udc.ws.app.socialnetwork.SocialNetworkServiceFactory;
import es.udc.ws.app.socialnetwork.SocialNetworkShowPost;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.util.Calendar;
import java.util.List;

public class TicketSellerSocialNetworkProxyService implements TicketSellerProxyService {
    @Override
    public ServiceShowAdminDto createShow(ServiceShowAdminDto show) throws InputValidationException {
        Show modelShow = TicketSellerServiceFactory.getService().createShow(ShowToDto.toShow(show));

        try {
            SocialNetworkServiceFactory.getService().publishShow(modelShow);
        } catch (InputValidationException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ShowToDto.toShowAdminDto(modelShow);
    }

    @Override
    public void updateShow(ServiceShowAdminDto show) throws InstanceNotFoundException, InputValidationException, ShowHasReservationsException, NotEnoughAvailableTicketsException {
        Show modelShow = ShowToDto.toShow(show);
        TicketSellerServiceFactory.getService().updateShow(modelShow);

        try {
            SocialNetworkServiceFactory.getService().updateShow(modelShow);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ServiceShowAdminDto findShow(long id) throws InstanceNotFoundException {
        Show modelShow = TicketSellerServiceFactory.getService().findShow(id);
        ServiceShowAdminDto showDto = ShowToDto.toShowAdminDto(modelShow);

        try {
            SocialNetworkShowPost showPost = SocialNetworkServiceFactory.getService().getShow(modelShow);
            showDto.setLikes(showPost.getLikes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return showDto;
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

        try
        {
            Show show = TicketSellerServiceFactory.getService().findShow(showId);

            SocialNetworkServiceFactory.getService().updateShow(show);

            if (show.getTickets() == 0)
                SocialNetworkServiceFactory.getService().commentShow(show, "No more tickets sorry :( luck other time");
            else if (show.getTickets() <= 10)
                SocialNetworkServiceFactory.getService().commentShow(show, "There are only " + show.getTickets() + " resting tickets, run for them!!");

        } catch (InstanceNotFoundException | InputValidationException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }

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
