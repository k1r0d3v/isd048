package es.udc.ws.app.client.service.soap;

import es.udc.ws.app.client.service.dto.ClientAdminShowDto;
import es.udc.ws.app.client.service.dto.ClientShowDto;
import es.udc.ws.app.client.service.soap.wsdl.ServiceShowAdminDto;
import es.udc.ws.app.client.service.soap.wsdl.ServiceShowDto;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class ShowDtoToSoap {
    public static List<ClientShowDto> toClientShowDtos(List<ServiceShowDto> serviceShows) {
        ArrayList<ClientShowDto> shows = new ArrayList<>();
        for (ServiceShowDto i : serviceShows)
            shows.add(toClientShowDto(i));
        return shows;
    }

    public static ClientShowDto toClientShowDto(ServiceShowDto serviceShow) {
        Calendar start = serviceShow.getStartDate().toGregorianCalendar();
        Calendar end = (Calendar)start.clone();
        end.add(Calendar.MINUTE, (int)serviceShow.getDuration());
        return new ClientShowDto(serviceShow.getId(), serviceShow.getName(), serviceShow.getDescription(), start, end, serviceShow.getLimitDate().toGregorianCalendar(), serviceShow.getTickets(), serviceShow.getPrice(), serviceShow.getDiscountedPrice());
    }

    public static ServiceShowAdminDto toSoapShowDto(ClientAdminShowDto show) {
        DatatypeFactory df;
        try {
            df = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException("Unexpected exception");
        }

        GregorianCalendar start = new GregorianCalendar();
        GregorianCalendar limit = new GregorianCalendar();
        start.setTime(show.getStartDate().getTime());
        limit.setTime(show.getLimitDate().getTime());

        ServiceShowAdminDto serviceShow = new ServiceShowAdminDto();
        serviceShow.setId(show.getId());
        serviceShow.setName(show.getName());
        serviceShow.setDescription(show.getDescription());
        serviceShow.setStartDate(df.newXMLGregorianCalendar(start));
        serviceShow.setDuration(show.getDuration());
        serviceShow.setLimitDate(df.newXMLGregorianCalendar(limit));
        serviceShow.setMaxTickets(show.getMaxTickets());
        serviceShow.setTickets(show.getTickets());
        serviceShow.setPrice(show.getPrice());
        serviceShow.setDiscountedPrice(show.getDiscountedPrice());
        serviceShow.setCommission(show.getCommission());
        return serviceShow;
    }
}
