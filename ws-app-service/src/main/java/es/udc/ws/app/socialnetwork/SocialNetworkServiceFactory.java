package es.udc.ws.app.socialnetwork;

import es.udc.ws.app.util.configuration.ConfigurationParametersManager;

public class SocialNetworkServiceFactory {
    private final static String CLASS_NAME_PARAMETER = "SocialNetworkServiceFactory.className";
    private static SocialNetworkService service = null;

    private SocialNetworkServiceFactory() { }

    @SuppressWarnings("rawtypes")
    private static SocialNetworkService getInstance() {
        try {
            String daoClassName = ConfigurationParametersManager.getParameter(CLASS_NAME_PARAMETER);
            Class daoClass = Class.forName(daoClassName);
            return (SocialNetworkService) daoClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized static SocialNetworkService getService() {

        if (service == null) {
            service = getInstance();
        }
        return service;
    }
}
