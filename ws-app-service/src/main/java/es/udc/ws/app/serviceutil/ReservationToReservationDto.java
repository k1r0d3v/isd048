package es.udc.ws.app.serviceutil;

import es.udc.ws.app.dto.ServiceReservationDto;
import es.udc.ws.app.model.reservation.Reservation;

import java.util.ArrayList;
import java.util.List;

public class ReservationToReservationDto {
    public static List<ServiceReservationDto> reservationToReservationDtos(List<Reservation> reservations) {
        List<ServiceReservationDto> reservationList = new ArrayList<>();
        for (Reservation i : reservations)
            reservationList.add(toReservationDto(i));
        return reservationList;
    }

    public static ServiceReservationDto toReservationDto(Reservation reservation) {
        return new ServiceReservationDto(reservation.getId(), reservation.getShowId(), reservation.getEmail(), reservation.getCardNumber(), reservation.getTickets(), reservation.isValid(), reservation.getCode(), reservation.getReservationDate(), reservation.getPrice());
    }

    public static Reservation toReservation(ServiceReservationDto sreservation) {
        return new Reservation(sreservation.getId(), sreservation.getShowId(), sreservation.getEmail(), sreservation.getCardNumber(), sreservation.getTickets(), sreservation.isValid(), sreservation.getCode(), sreservation.getReservationDate(), sreservation.getPrice());
    }
}
