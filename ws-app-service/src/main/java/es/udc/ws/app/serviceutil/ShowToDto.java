package es.udc.ws.app.serviceutil;

import es.udc.ws.app.dto.ServiceShowAdminDto;
import es.udc.ws.app.dto.ServiceShowDto;
import es.udc.ws.app.model.show.Show;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ShowToDto {
    public static List<ServiceShowDto> toShowDtos(List<Show> shows) {
        List<ServiceShowDto> showList = new ArrayList<>();
        for (Show i : shows)
            showList.add(toShowDto(i));
        return showList;
    }

    public static ServiceShowDto toShowDto(Show show) {
        return new ServiceShowDto(show.getId(), show.getName(), show.getDescription(), (Calendar)show.getStartDate().clone(), show.getDuration(), (Calendar)show.getLimitDate().clone(), show.getTickets(), show.getPrice(), show.getDiscountedPrice());
    }

    public static ServiceShowAdminDto toShowAdminDto(Show show) {
        return new ServiceShowAdminDto(show.getId(), show.getName(), show.getDescription(), (Calendar)show.getStartDate().clone(), show.getDuration(), (Calendar)show.getLimitDate().clone(), show.getMaxTickets(), show.getTickets(), show.getPrice(), show.getDiscountedPrice(), show.getCommission());
    }

    public static Show toShow(ServiceShowAdminDto show) {
        return new Show(show.getId(), show.getName(), show.getDescription(), (Calendar)show.getStartDate().clone(), show.getDuration(), (Calendar)show.getLimitDate().clone(), show.getMaxTickets(), show.getTickets(), show.getPrice(), show.getDiscountedPrice(), show.getCommission());
    }
}
