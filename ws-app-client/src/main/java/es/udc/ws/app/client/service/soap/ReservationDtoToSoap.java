package es.udc.ws.app.client.service.soap;

import es.udc.ws.app.client.service.dto.ClientReservationDto;
import es.udc.ws.app.client.service.soap.wsdl.ServiceReservationDto;

import java.util.ArrayList;
import java.util.List;

public class ReservationDtoToSoap {
    public static List<ClientReservationDto> toClientReservationDtos(List<ServiceReservationDto> serviceReservations) {
        ArrayList<ClientReservationDto> reservations = new ArrayList<>();
        for (ServiceReservationDto i : serviceReservations)
            reservations.add(toClientReservationDto(i));
        return reservations;
    }

    public static ClientReservationDto toClientReservationDto(ServiceReservationDto serviceReservation) {
        return new ClientReservationDto(serviceReservation.getId(), serviceReservation.getShowId(), serviceReservation.getEmail(), serviceReservation.getCreditCard(), serviceReservation.getTickets(), serviceReservation.isValid(), serviceReservation.getCode(), serviceReservation.getReservationDate().toGregorianCalendar(), serviceReservation.getPrice());
    }
}
