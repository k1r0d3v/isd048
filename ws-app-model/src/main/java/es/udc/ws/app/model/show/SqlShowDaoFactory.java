package es.udc.ws.app.model.show;


import es.udc.ws.app.util.configuration.ConfigurationParametersManager;

/**
 * A factory to get
 * <code>SqlShowDao</code> objects. <p> Required configuration parameters: <ul>
 * <li><code>SqlShowDaoFactory.className</code>: it must specify the full class
 * name of the class implementing
 * <code>SqlShowDao</code>.</li> </ul>
 */
public class SqlShowDaoFactory
{
    private final static String CLASS_NAME_PARAMETER = "SqlShowDaoFactory.className";
    private static SqlShowDao dao = null;

    private SqlShowDaoFactory() { }

    @SuppressWarnings("rawtypes")
    private static SqlShowDao getInstance() {
        try {
            String daoClassName = ConfigurationParametersManager
                    .getParameter(CLASS_NAME_PARAMETER);
            Class daoClass = Class.forName(daoClassName);
            return (SqlShowDao) daoClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public synchronized static SqlShowDao getDao() {

        if (dao == null) {
            dao = getInstance();
        }
        return dao;

    }
}