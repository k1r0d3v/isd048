package es.udc.ws.app.model.service;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class ServiceFactory {

    private final static String CLASS_NAME_PARAMETER = "ServiceFactory.className";
    private static Service service = null;

    private ServiceFactory() {
    }

    @SuppressWarnings("rawtypes")
    private static Service getInstance() {
        try {
            String serviceClassName = ConfigurationParametersManager
                    .getParameter(CLASS_NAME_PARAMETER);
            Class serviceClass = Class.forName(serviceClassName);
            return (Service)serviceClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public synchronized static Service getService() {

        if (service == null) {
            service = getInstance();
        }
        return service;

    }
}
