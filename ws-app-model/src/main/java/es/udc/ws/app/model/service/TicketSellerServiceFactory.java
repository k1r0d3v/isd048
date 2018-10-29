package es.udc.ws.app.model.service;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class TicketSellerServiceFactory {

    private final static String CLASS_NAME_PARAMETER = "TicketSellerServiceFactory.className";
    private static TicketSellerService service = null;

    private TicketSellerServiceFactory() { }

    @SuppressWarnings("rawtypes")
    private static TicketSellerService getInstance() {
        try {
            String serviceClassName = ConfigurationParametersManager
                    .getParameter(CLASS_NAME_PARAMETER);
            Class serviceClass = Class.forName(serviceClassName);
            return (TicketSellerService)serviceClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public synchronized static TicketSellerService getService() {

        if (service == null) {
            service = getInstance();
        }
        return service;

    }
}
