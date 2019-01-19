package es.udc.ws.app.client.service.soap;

import es.udc.ws.app.client.service.ClientTicketSellerService;
import es.udc.ws.app.client.service.dto.ClientReservationDto;
import es.udc.ws.app.client.service.dto.ClientShowDto;
import es.udc.ws.app.client.service.exceptions.ClientLimitDateExceeded;
import es.udc.ws.app.client.service.exceptions.ClientNotEnoughAvailableTickets;
import es.udc.ws.app.client.service.soap.wsdl.*;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import javax.xml.ws.BindingProvider;
import java.util.List;

public class SoapClientTicketSellerService implements ClientTicketSellerService
{
    private final static String ENDPOINT_ADDRESS_PARAMETER
            = "SoapTicketSellerService.endpointAddress";

    private String endpointAddress;

    private TicketSellerProvider provider;

    public SoapClientTicketSellerService() {
        init(getEndpointAddress());
    }

    private String getEndpointAddress() {
        if (endpointAddress == null)
            endpointAddress = ConfigurationParametersManager.getParameter(ENDPOINT_ADDRESS_PARAMETER);
        return endpointAddress;
    }

    private void init(String providerURL) {
        TicketSellerProviderService ticketSellerProviderService = new TicketSellerProviderService();
        provider = ticketSellerProviderService.getTicketSellerProviderPort();
        ((BindingProvider)provider).getRequestContext().put(
                BindingProvider.ENDPOINT_ADDRESS_PROPERTY,providerURL);
    }

    @Override
    public List<ClientShowDto> findShows(String keywords)
            throws InputValidationException
    {
        try {
            return ShowDtoToSoap.toClientShowDtos(provider.findShows(keywords));
        } catch (SoapInputValidationException e) {
            throw new InputValidationException(e.getFaultInfo());
        }
    }

    @Override
    public ClientReservationDto buyTickets(long showId, String email, String creditCard, int count)
            throws InstanceNotFoundException, InputValidationException,
            ClientNotEnoughAvailableTickets, ClientLimitDateExceeded
    {
        try {
            return ReservationDtoToSoap.toClientReservationDto(provider.buyTickets(showId, email, creditCard, count));
        } catch (SoapInputValidationException e) {
            throw new InputValidationException(e.getFaultInfo());
        } catch (SoapInstanceNotFoundException e) {
            throw new InstanceNotFoundException(e.getFaultInfo().getInstanceId(), e.getFaultInfo().getInstanceType());
        } catch (SoapLimitDateExceededException e) {
            throw new ClientLimitDateExceeded(e.getFaultInfo().getLimitDate().toGregorianCalendar().getTime());
        } catch (SoapNotEnoughAvailableTicketsException e) {
            throw new ClientNotEnoughAvailableTickets(e.getFaultInfo());
        }
    }

    @Override
    public List<ClientReservationDto> getUserReservations(String email)
            throws InputValidationException
    {
        try {
            return ReservationDtoToSoap.toClientReservationDtos(provider.getUserReservations(email));
        } catch (SoapInputValidationException e) {
            throw new InputValidationException(e.getFaultInfo());
        }
    }
}
