package es.udc.ws.app.client.service;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class ClientTicketSellerServiceFactory {

    private final static String CLASS_NAME_PARAMETER
            = "ClientTicketSellerServiceFactory.className";
    private static Class<ClientTicketSellerService> serviceClass = null;

    private ClientTicketSellerServiceFactory() {
    }

    @SuppressWarnings("unchecked")
    private synchronized static Class<ClientTicketSellerService> getServiceClass() {

        if (serviceClass == null) {
            try {
                String serviceClassName = ConfigurationParametersManager
                        .getParameter(CLASS_NAME_PARAMETER);
                serviceClass = (Class<ClientTicketSellerService>) Class.forName(serviceClassName);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return serviceClass;

    }

    public static ClientTicketSellerService getService() {

        try {
            return (ClientTicketSellerService) getServiceClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }
}
