package es.udc.ws.app.service;

import es.udc.ws.app.model.service.TicketSellerService;
import es.udc.ws.app.util.configuration.ConfigurationParametersManager;

public class TicketSellerProxyServiceFactory {

    private final static String CLASS_NAME_PARAMETER = "TicketSellerProxyServiceFactory.className";
    private static TicketSellerProxyService service = null;

    private TicketSellerProxyServiceFactory() { }

    @SuppressWarnings("rawtypes")
    private static TicketSellerProxyService getInstance() {
        try {
            String serviceClassName = ConfigurationParametersManager
                    .getParameter(CLASS_NAME_PARAMETER);
            Class serviceClass = Class.forName(serviceClassName);
            return (TicketSellerProxyService)serviceClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public synchronized static TicketSellerProxyService getService() {

        if (service == null) {
            service = getInstance();
        }
        return service;

    }
}
