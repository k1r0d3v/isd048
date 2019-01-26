package es.udc.ws.app.util.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

public final class ConfigurationParametersManager {

    private static final String CONFIGURATION_FILE = "ConfigurationParameters.properties";
    private static Map<String, String> parameters;

    private ConfigurationParametersManager() {
    }
    
    private static List<URL> loadAllResources(final String name, final ClassLoader classLoader)
    {
        try {
            final List<URL> list = new ArrayList<>();
            final Enumeration<URL> systemResources =
                    (classLoader == null ? ClassLoader.getSystemClassLoader() : classLoader)
                            .getResources(name);
            while (systemResources.hasMoreElements()) {
                URL url = systemResources.nextElement();
                list.add(url);
            }
            Collections.reverse(list); // Needed to apply the correct order of properties
            return list;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @SuppressWarnings({"unchecked", "rawtypes"})
    private static synchronized Map<String, String> getParameters() {

        if (parameters == null) {
            Class<ConfigurationParametersManager> 
                    configurationParametersManagerClass = 
                    ConfigurationParametersManager.class;
            ClassLoader classLoader = 
                    configurationParametersManagerClass.getClassLoader();
            List<URL> inputStreams = loadAllResources(CONFIGURATION_FILE, classLoader);
            
            Properties properties = new Properties();
            
            for (URL url : inputStreams)
            {
                System.out.println("Loading properties: " + url);

                try(InputStream inputStream = url.openStream()) {
                    properties.load(inputStream);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            /*
             * We use a "HashMap" instead of a "HashTable" because HashMap's
             * methods are *not* synchronized (so they are faster), and the
             * parameters are only read.
             */
            parameters = (Map<String, String>) new HashMap(properties);
        }
        return parameters;

    }

    public static String getParameter(String name) {
        return getParameters().get(name);
    }
}
