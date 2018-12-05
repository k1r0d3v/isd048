package es.udc.ws.app.service.restservice;

import es.udc.ws.app.dto.ServiceShowAdminDto;
import es.udc.ws.app.dto.ServiceReservationDto;
import es.udc.ws.app.model.reservation.Reservation;
import es.udc.ws.app.model.show.Show;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReservationToDto {
    public static List<ServiceReservationDto> toReservationDtos(List<Reservation> shows) {
        List<ServiceReservationDto> showList = new ArrayList<>();
        for (Reservation i : shows)
            showList.add(toReservationDto(i));
        return showList;
    }

    public static ServiceReservationDto toReservationDto(Reservation reservation) {
        return new ServiceReservationDto(reservation.getId(), reservation.getShowId(), reservation.getEmail(), reservation.getCardNumber(), reservation.getTickets(), reservation.isValid(), reservation.getCode(), reservation.getReservationDate(), reservation.getPrice());
    }

    public static Reservation toReservation(ServiceReservationDto reservation) {
        return new Reservation(reservation.getId(), reservation.getShowId(), reservation.getEmail(), reservation.getCardNumber(), reservation.getTickets(), reservation.isValid(), reservation.getCode(), reservation.getReservationDate(), reservation.getPrice());
    }
}
