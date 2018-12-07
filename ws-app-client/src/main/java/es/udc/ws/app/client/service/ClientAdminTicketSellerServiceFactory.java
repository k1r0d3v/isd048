package es.udc.ws.app.client.service;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class ClientAdminTicketSellerServiceFactory {

    private final static String CLASS_NAME_PARAMETER
            = "ClientAdminTicketSellerServiceFactory.className";
    private static Class<ClientAdminTicketSellerService> serviceClass = null;

    private ClientAdminTicketSellerServiceFactory() {
    }

    @SuppressWarnings("unchecked")
    private synchronized static Class<ClientAdminTicketSellerService> getServiceClass() {

        if (serviceClass == null) {
            try {
                String serviceClassName = ConfigurationParametersManager
                        .getParameter(CLASS_NAME_PARAMETER);
                serviceClass = (Class<ClientAdminTicketSellerService>) Class.forName(serviceClassName);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return serviceClass;

    }

    public static ClientAdminTicketSellerService getService() {

        try {
            return (ClientAdminTicketSellerService) getServiceClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }
}
