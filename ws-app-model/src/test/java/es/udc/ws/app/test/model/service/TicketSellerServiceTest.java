package es.udc.ws.app.test.model.service;


import es.udc.ws.app.model.reservation.Reservation;
import es.udc.ws.app.model.service.TicketSellerService;

import es.udc.ws.app.model.service.TicketSellerServiceFactory;
import es.udc.ws.app.model.service.exceptions.ShowHasReservations;
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

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

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

    private Show getValidShow(String name, String description) {
        Show s = new Show();

        s.setName(name);
        s.setDescription(description);

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

    private Show getValidShow() {
        return getValidShow("Test", "Test description");
    }

    @Test
    public void testCreateShow()
    {
        boolean exceptionCatched = false;
        Show show = new Show();
        Calendar start = Calendar.getInstance();
        Calendar limit = Calendar.getInstance();

        // Validate each field

        try
        {
            show.setName(null);
            show = ticketService.createShow(show);
        } catch (InputValidationException e) {
            exceptionCatched = true;
        }
        assertTrue(exceptionCatched);
        try { removeShow(show.getId()); } catch (Exception e) { }
        exceptionCatched = false;

        try
        {
            show.setDescription(null);
            show = ticketService.createShow(show);
        } catch (InputValidationException e) {
            exceptionCatched = true;
        }
        assertTrue(exceptionCatched);
        try { removeShow(show.getId()); } catch (Exception e) { }
        exceptionCatched = false;

        try
        {
            show.setDuration(-1);
            show = ticketService.createShow(show);
        } catch (InputValidationException e) {
            exceptionCatched = true;
        }
        assertTrue(exceptionCatched);
        try { removeShow(show.getId()); } catch (Exception e) { }
        exceptionCatched = false;

        try
        {
            show.setDuration(0);
            show = ticketService.createShow(show);
        } catch (InputValidationException e) {
            exceptionCatched = true;
        }
        assertTrue(exceptionCatched);
        try { removeShow(show.getId()); } catch (Exception e) { }
        exceptionCatched = false;

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
        exceptionCatched = false;

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
        exceptionCatched = false;

        try
        {
            show.setMaxTickets(0);
            show = ticketService.createShow(show);
        } catch (InputValidationException e) {
            exceptionCatched = true;
        }
        assertTrue(exceptionCatched);
        try { removeShow(show.getId()); } catch (Exception e) { }
        exceptionCatched = false;

        try
        {
            show.setRealPrice(-1.0f);
            show = ticketService.createShow(show);
        } catch (InputValidationException e) {
            exceptionCatched = true;
        }
        assertTrue(exceptionCatched);
        try { removeShow(show.getId()); } catch (Exception e) { }
        exceptionCatched = false;
        
        try
        {
            show.setDiscountedPrice(-1.0f);
            show = ticketService.createShow(show);
        } catch (InputValidationException e) {
            exceptionCatched = true;
        }
        assertTrue(exceptionCatched);
        try { removeShow(show.getId()); } catch (Exception e) { }
        exceptionCatched = false;

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
        Show show = getValidShow();

        // Create test show
        try {
            show = ticketService.createShow(show);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected exception");
        }

        long tmpId = show.getId();
        float tmpRealPrice = show.getRealPrice();

        // Validate id
        try {
            show.setId(100);
            ticketService.updateShow(show);
        } catch (InstanceNotFoundException e) {
            exceptionCatched = true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertTrue(exceptionCatched);
        exceptionCatched = false;
        show.setId(tmpId);

        // Validate setRealPrice
        try {
            show.setSoldTickets(1);
            show.setRealPrice(10.0f);
            ticketService.updateShow(show);
        } catch (ShowHasReservations e) {
            exceptionCatched = true;
        } catch (InputValidationException e) {
            throw new RuntimeException("Unexpected exception");
        } catch (InstanceNotFoundException e) {
            throw new RuntimeException("Unexpected exception");
        }
        assertTrue(exceptionCatched);
        show.setRealPrice(tmpRealPrice);
        exceptionCatched = false;

        try {
            show.setSoldTickets(1);
            ticketService.updateShow(show);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected exception");
        }

        try {
            show.setRealPrice(10.0f);
            ticketService.updateShow(show);
        } catch (ShowHasReservations e) {
            exceptionCatched = true;
        } catch (InputValidationException e) {
            throw new RuntimeException("Unexpected exception");
        } catch (InstanceNotFoundException e) {
            throw new RuntimeException("Unexpected exception");
        }
        assertTrue(exceptionCatched);
        exceptionCatched = false;

        // TODO: Validate rest of fields

        try { removeShow(show.getId()); } catch (Exception e) { }
    }

    // TODO: TODO and clean
    @Test
    public void testFindShow()
    {
        boolean exceptionCatched = false;
        Show show = getValidShow();

        // Validate code field
        try {
            ticketService.findShow(null);
        } catch (InputValidationException e) {
            exceptionCatched = true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Unexpected exception");
        }
        assertTrue(exceptionCatched);
        exceptionCatched = false;

        // Validate findShow in-existent id
        try {
            ticketService.findShow(12357L);
        } catch (InstanceNotFoundException e) {
            exceptionCatched = true;
        } catch (Exception e) {
            throw new RuntimeException("Unexpected exception");
        }
        assertTrue(exceptionCatched);
        exceptionCatched = false;

        // Validate findShow existent id
        try {
            show = ticketService.createShow(show);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected exception");
        }

        try {
            ticketService.findShow(show.getId());
        } catch (InstanceNotFoundException e) {
            e.printStackTrace();
            exceptionCatched = true;
        } catch (Exception e) {
            throw new RuntimeException("Unexpected exception");
        }
        assertFalse(exceptionCatched);
        exceptionCatched = false;

        // Validate findShow dirty id
        try { removeShow(show.getId()); } catch (Exception e) { }

        try {
            ticketService.findShow(show.getId());
        } catch (InstanceNotFoundException e) {
            exceptionCatched = true;
        } catch (Exception e) {
            throw new RuntimeException("Unexpected exception");
        }
        assertTrue(exceptionCatched);
    }

    // TODO: Clean, please
    @Test
    public void testFindShows()
    {
        boolean exceptionCatched = false;
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        end.add(Calendar.DAY_OF_WEEK, 8);

        // Validate words field
        try {
            ticketService.findShows(null, start, end);
        } catch (InputValidationException e) {
            exceptionCatched = true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Unexpected exception");
        }
        assertTrue(exceptionCatched);
        exceptionCatched = false;

        // Validate dates
        try {
            ticketService.findShows("hello", null, end);
        } catch (InputValidationException e) {
            exceptionCatched = true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Unexpected exception");
        }
        assertTrue(exceptionCatched);
        exceptionCatched = false;

        try {
            start = (Calendar)end.clone();
            start.add(Calendar.DAY_OF_WEEK, 1);
            ticketService.findShows("hello", start, end);
        } catch (InputValidationException e) {
            exceptionCatched = true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Unexpected exception");
        }
        assertTrue(exceptionCatched);
        exceptionCatched = false;

        // Validate end field
        start = Calendar.getInstance();

        try {
            ticketService.findShows("hello", start, null);
        } catch (InputValidationException e) {
            exceptionCatched = true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Unexpected exception");
        }
        assertTrue(exceptionCatched);
        exceptionCatched = false;

        // Validate results
        Show showOne = getValidShow("One", "One - The show");
        Show showTwo = getValidShow("Two", "Two - The show, but better");
        start.add(Calendar.DAY_OF_WEEK, 1);
        showOne.setStartDate((Calendar)start.clone());

        Calendar limit = (Calendar)start.clone();
        limit.add(Calendar.DAY_OF_WEEK, -4);
        showOne.setLimitDate((Calendar)limit.clone());

        start.add(Calendar.DAY_OF_WEEK, 2);
        showTwo.setStartDate((Calendar)start.clone());
        showTwo.setLimitDate((Calendar)limit.clone());

        try {
            showOne = ticketService.createShow(showOne);
            showTwo = ticketService.createShow(showTwo);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Unexpected exception");
        }

        List<Show> shows;
        try {
            start = Calendar.getInstance();
            start.add(Calendar.DAY_OF_WEEK, -1);
            shows = ticketService.findShows("One better", start, end);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Unexpected exception");
        }

        assertEquals(2, shows.size());
        assertNotEquals(shows.get(0), shows.get(1));
        assertTrue(shows.get(0).equals(showOne) || shows.get(1).equals(showOne));
        assertTrue(shows.get(0).equals(showTwo) || shows.get(1).equals(showTwo));
    }
    
    @Test(expected = InstanceNotFoundException.class)
	public void buyTicketShowDoesntExist() throws InputValidationException, InstanceNotFoundException {

		Reservation res = ticketService.buyTickets((long) -1, "email@gmail.com", "5489162716279483", 1);
		// Clear database. 
	
    }
    

	@Test(expected = InputValidationException.class)
	public void testBuyMovieWithInvalidCreditCard() throws InputValidationException, InstanceNotFoundException {

		Show show = getValidShow();
		try { 
			Reservation res = ticketService.buyTickets((long)12, "email@gmail.com", "", 4);
			//removeSale(sale.getSaleId());
		} finally {
			/* Clear database. */
			//removeMovie(show.getId());
		}

	}
}