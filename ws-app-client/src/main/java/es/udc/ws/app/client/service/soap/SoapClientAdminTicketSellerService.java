package es.udc.ws.app.client.service.soap;

import es.udc.ws.app.client.service.ClientAdminTicketSellerService;
import es.udc.ws.app.client.service.dto.ClientAdminShowDto;
import es.udc.ws.app.client.service.dto.ClientShowDto;
import es.udc.ws.app.client.service.exceptions.ClientCreditCardNotCoincident;
import es.udc.ws.app.client.service.exceptions.ClientNotEnoughAvailableTickets;
import es.udc.ws.app.client.service.exceptions.ClientReservationAlreadyChecked;
import es.udc.ws.app.client.service.exceptions.ClientShowHasReservations;
import es.udc.ws.app.client.service.soap.wsdl.*;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import javax.xml.ws.BindingProvider;


public class SoapClientAdminTicketSellerService implements ClientAdminTicketSellerService
{
    private final static String ENDPOINT_ADDRESS_PARAMETER
            = "SoapTicketSellerService.endpointAddress";

    private String endpointAddress;

    private TicketSellerProvider provider;

    public SoapClientAdminTicketSellerService() {
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
    public ClientAdminShowDto createShow(ClientAdminShowDto show)
            throws InputValidationException
    {
        try {
            return ShowDtoToSoap.toClientAdminShowDto(provider.createShow(
                    ShowDtoToSoap.toSoapShowDto(show) ));
        } catch (SoapInputValidationException e) {
            throw new InputValidationException(e.getFaultInfo());
        }
    }


    @Override
    public void updateShow(ClientAdminShowDto show)
            throws InstanceNotFoundException, InputValidationException,
            ClientShowHasReservations, ClientNotEnoughAvailableTickets
    {
        try {
            provider.updateShow(ShowDtoToSoap.toSoapShowDto(show));
        } catch (SoapInputValidationException e) {
            throw new InputValidationException(e.getFaultInfo());
        } catch (SoapInstanceNotFoundException e) {
            throw new InstanceNotFoundException(e.getFaultInfo().getInstanceId(), e.getFaultInfo().getInstanceType());
        } catch (SoapNotEnoughAvailableTicketsException e) {
            throw new ClientNotEnoughAvailableTickets(e.getFaultInfo());
        } catch (SoapShowHasReservationsException e) {
            throw new ClientShowHasReservations(e.getFaultInfo());
        }
    }

    @Override
    public ClientAdminShowDto findShow(long id)
            throws InstanceNotFoundException
    {
        try {
            return ShowDtoToSoap.toClientAdminShowDto(provider.findShow(id));
        } catch (SoapInstanceNotFoundException e) {
            throw new InstanceNotFoundException(e.getFaultInfo().getInstanceId(), e.getFaultInfo().getInstanceType());
        }
    }

    @Override
    public void checkReservation(String code, String creditCard)
            throws InstanceNotFoundException, InputValidationException,
            ClientCreditCardNotCoincident, ClientReservationAlreadyChecked
    {
        try {
            provider.checkReservation(code, creditCard);
        } catch (SoapCreditCardNotCoincidentException e) {
            throw new ClientCreditCardNotCoincident(e.getFaultInfo());
        } catch (SoapInputValidationException e) {
            throw new InputValidationException(e.getFaultInfo());
        } catch (SoapInstanceNotFoundException e) {
            throw new InstanceNotFoundException(e.getFaultInfo().getInstanceId(), e.getFaultInfo().getInstanceType());
        } catch (SoapReservationAlreadyCheckedException e) {
            throw new ClientReservationAlreadyChecked(e.getFaultInfo());
        }
    }
}
