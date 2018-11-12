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
import static org.junit.Assert.*;

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
    
    private Show createValidShow() {
        Show s = new Show();

        s.setName("Test");
        s.setDescription("Test description");

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 1);
        s.setStartDate(calendar);

        s.setDuration(120);

        calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 1);
        calendar.add(Calendar.DAY_OF_WEEK, -1);
        s.setLimitDate(calendar);

        s.setMaxTickets(10000);
        s.setSoldTickets(0);
        s.setRealPrice(60.0f);
        s.setDiscountedPrice(50.0f);
        s.setSalesCommission(20.0f);
        
        return s;
    }

    @Test
    public void testCreateShow()
    {
        boolean exceptionCatched = false;
        Show show = new Show();
        Calendar start = Calendar.getInstance();
        Calendar limit = Calendar.getInstance();

        try
        {
            show.setName(null);
            show = ticketService.createShow(show);
        } catch (InputValidationException e) {
            exceptionCatched = true;
        }
        assertTrue(exceptionCatched);
        try { removeShow(show.getId()); } catch (Exception e) { }

        try
        {
            show.setDescription(null);
            show = ticketService.createShow(show);
        } catch (InputValidationException e) {
            exceptionCatched = true;
        }
        assertTrue(exceptionCatched);
        try { removeShow(show.getId()); } catch (Exception e) { }

        try
        {
            show.setDuration(-1);
            show = ticketService.createShow(show);
        } catch (InputValidationException e) {
            exceptionCatched = true;
        }
        assertTrue(exceptionCatched);
        try { removeShow(show.getId()); } catch (Exception e) { }

        try
        {
            show.setDuration(0);
            show = ticketService.createShow(show);
        } catch (InputValidationException e) {
            exceptionCatched = true;
        }
        assertTrue(exceptionCatched);
        try { removeShow(show.getId()); } catch (Exception e) { }

        try
        {
            start.add(Calendar.DAY_OF_WEEK, -1);
            show.setStartDate(Calendar.getInstance());
            show = ticketService.createShow(show);
        } catch (InputValidationException e) {
            exceptionCatched = true;
        }
        assertTrue(exceptionCatched);
        try { removeShow(show.getId()); } catch (Exception e) { }

        try
        {
            limit.add(Calendar.DAY_OF_WEEK, -2);
            show.setLimitDate(limit);
            show = ticketService.createShow(show);
        } catch (InputValidationException e) {
            exceptionCatched = true;
        }
        assertTrue(exceptionCatched);
        try { removeShow(show.getId()); } catch (Exception e) { }

        try
        {
            show.setMaxTickets(0);
            show = ticketService.createShow(show);
        } catch (InputValidationException e) {
            exceptionCatched = true;
        }
        assertTrue(exceptionCatched);
        try { removeShow(show.getId()); } catch (Exception e) { }

        try
        {
            show.setRealPrice(-1.0f);
            show = ticketService.createShow(show);
        } catch (InputValidationException e) {
            exceptionCatched = true;
        }
        assertTrue(exceptionCatched);
        try { removeShow(show.getId()); } catch (Exception e) { }
        
        try
        {
            show.setDiscountedPrice(-1.0f);
            show = ticketService.createShow(show);
        } catch (InputValidationException e) {
            exceptionCatched = true;
        }
        assertTrue(exceptionCatched);
        try { removeShow(show.getId()); } catch (Exception e) { }

        try
        {
            show.setSalesCommission(-1.0f);
            show = ticketService.createShow(show);
        } catch (InputValidationException e) {
            exceptionCatched = true;
        }
        assertTrue(exceptionCatched);
        try { removeShow(show.getId()); } catch (Exception e) { }
    }
    
    @Test
    public void testUpdateShow() {
        boolean exceptionCatched = false;
        Show show = createValidShow();
        try {
            ticketService.createShow(show);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected exception");
        }
    }
}