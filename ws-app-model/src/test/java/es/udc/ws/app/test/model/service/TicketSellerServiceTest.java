package es.udc.ws.app.test.model.service;


import es.udc.ws.app.model.service.TicketSellerService;
import es.udc.ws.app.model.service.TicketSellerServiceFactory;
import es.udc.ws.app.model.show.Show;
import es.udc.ws.app.model.show.SqlShowDao;
import es.udc.ws.app.model.show.SqlShowDaoFactory;
import es.udc.ws.app.model.util.Constants;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.sql.SimpleDataSource;
import org.junit.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;


public class TicketSellerServiceTest
{
    private static TicketSellerService ticketService;
    private static SqlShowDao showDao;

    @BeforeClass
    public static void init() {

        /*
         * Create a simple data source and add it to "DataSourceLocator" (this
         * is needed to test "es.udc.ws.movies.model.movieservice.MovieService"
         */
        DataSource dataSource = new SimpleDataSource();

        /* Add "dataSource" to "DataSourceLocator". */
        DataSourceLocator.addDataSource(Constants.DATA_SOURCE, dataSource);

        ticketService = TicketSellerServiceFactory.getService();
        showDao = SqlShowDaoFactory.getDao();
    }

    private void updateShow(Show show)
    {
        DataSource dataSource = DataSourceLocator.getDataSource(Constants.DATA_SOURCE);

        try (Connection connection = dataSource.getConnection()) {

            try {

                /* Prepare connection. */
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                /* Do work. */
                showDao.update(connection, show);

                /* Commit. */
                connection.commit();

            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw new RuntimeException(e);
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void removeShow(Long id)
    {
        DataSource dataSource = DataSourceLocator.getDataSource(Constants.DATA_SOURCE);

        try (Connection connection = dataSource.getConnection()) {

            try {

                /* Prepare connection. */
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                /* Do work. */
                showDao.remove(connection, id);

                /* Commit. */
                connection.commit();

            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw new RuntimeException(e);
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testCreateShow()
            throws InputValidationException
    {
        Show show = new Show();
        show.setName("Foo");
        show.setDescription("Foo");
        show.setDuration(10);
        show.setStartDate(Calendar.getInstance());
        show.setLimitDate(Calendar.getInstance());
        show.setMaxTickets(100);
        show.setRealPrice(100.0f);
        show.setDiscountedPrice(80.0f);
        show.setSalesCommission(20.0f);
        show = ticketService.createShow(show);

        show.setName("Hello world update!!");
        updateShow(show);
        removeShow(show.getId());
    }
}