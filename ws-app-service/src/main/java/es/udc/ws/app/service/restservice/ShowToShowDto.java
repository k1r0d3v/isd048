package es.udc.ws.app.service.restservice;

import es.udc.ws.app.dto.ServiceShowDto;
import es.udc.ws.app.model.show.Show;

import java.util.ArrayList;
import java.util.List;

public class ShowToShowDto {
    public static List<ServiceShowDto> toShowDtos(List<Show> shows) {
        List<ServiceShowDto> showList = new ArrayList<>();
        for (Show i : shows)
            showList.add(toShowDto(i));
        return showList;
    }

    public static ServiceShowDto toShowDto(Show show) {
        return new ServiceShowDto(show.getId(), show.getName(), show.getDescription(), show.getStartDate(), show.getDuration(), show.getLimitDate(), show.getAvailableTickets(), show.getRealPrice(), show.getDiscountedPrice());
    }

    public static Show toShow(ServiceShowDto sshow) {
        return null;// new Show(sshow.getId(), sshow.getName(), sshow.getDescription(), sshow.getStartDate(), sshow.getDuration(), sshow.getLimitDate(), null, sshow.getAvailableTickets(), sshow.getRealPrice(), sshow.getDiscountedPrice(), null);
    }
}
