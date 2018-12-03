package es.udc.ws.app.service.restservice;

import es.udc.ws.app.dto.ServiceShowAdminDto;
import es.udc.ws.app.dto.ServiceShowDto;
import es.udc.ws.app.model.show.Show;

import java.util.ArrayList;
import java.util.List;

public class ShowToDto {
    public static List<ServiceShowDto> toShowDtos(List<Show> shows) {
        List<ServiceShowDto> showList = new ArrayList<>();
        for (Show i : shows)
            showList.add(toShowDto(i));
        return showList;
    }

    public static ServiceShowDto toShowDto(Show show) {
        return new ServiceShowDto(show.getId(), show.getName(), show.getDescription(), show.getStartDate(), show.getDuration(), show.getLimitDate(), show.getAvailableTickets(), show.getRealPrice(), show.getDiscountedPrice());
    }

    public static ServiceShowAdminDto toShowAdminDto(Show show) {
        return new ServiceShowAdminDto(show.getId(), show.getName(), show.getDescription(), show.getStartDate(), show.getDuration(), show.getLimitDate(), show.getMaxTickets(), show.getAvailableTickets(), show.getRealPrice(), show.getDiscountedPrice(), show.getSalesCommission());
    }

    public static Show toShow(ServiceShowAdminDto show) {
        return new Show(show.getId(), show.getName(), show.getDescription(), show.getStartDate(), show.getDuration(), show.getLimitDate(), show.getMaxTickets(), show.getAvailableTickets(), show.getRealPrice(), show.getDiscountedPrice(), show.getSalesCommission());
    }
}
