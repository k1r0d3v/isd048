package es.udc.ws.app.test.model.service;


import es.udc.ws.app.model.reservation.Reservation;
import es.udc.ws.app.model.reservation.SqlReservationDao;
import es.udc.ws.app.model.reservation.SqlReservationDaoFactory;
import es.udc.ws.app.model.service.exceptions.CreditCardNotCoincidentException;
import es.udc.ws.app.model.service.exceptions.NotEnoughAvailableTicketsException;
import es.udc.ws.app.model.service.TicketSellerService;

import es.udc.ws.app.model.service.TicketSellerServiceFactory;
import es.udc.ws.app.model.service.exceptions.LimitDateExceededException;
import es.udc.ws.app.model.service.exceptions.ReservationAlreadyCheckedException;
import es.udc.ws.app.model.service.exceptions.ShowHasReservationsException;
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
import java.util.Calendar;
import java.util.List;

public class TicketSellerServiceTest
{
	private static TicketSellerService ticketService;
	private static SqlShowDao showDao;
	private static SqlReservationDao reservationDao;

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
		reservationDao = SqlReservationDaoFactory.getDao();
	}

	private void removeShow(Long id) throws InstanceNotFoundException
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
				throw e;
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

	private void removeReservation(Long id) throws InstanceNotFoundException
	{
		DataSource dataSource = DataSourceLocator.getDataSource(Constants.DATA_SOURCE);

		try (Connection connection = dataSource.getConnection()) {

			try {

				/* Prepare connection. */
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);

				/* Do work. */
				reservationDao.remove(connection, id);

				/* Commit. */
				connection.commit();

			} catch (InstanceNotFoundException e) {
				connection.commit();
				throw e;
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

	private Reservation findReservation(String code) throws InstanceNotFoundException {
		DataSource dataSource = DataSourceLocator.getDataSource(Constants.DATA_SOURCE);

		try (Connection connection = dataSource.getConnection()) {

			try {

				/* Prepare connection. */
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);

				/* Do work. */
				Reservation r = reservationDao.findByCode(connection, code);

				/* Commit. */
				connection.commit();
				return r;
			} catch (InstanceNotFoundException e) {
				connection.commit();
				throw e;
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

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, 1);

		Calendar otherCalendar = Calendar.getInstance();
		otherCalendar.add(Calendar.YEAR, 1);
		otherCalendar.add(Calendar.DAY_OF_WEEK, -1);

		return new Show(754883452L, name, description, calendar, 120, otherCalendar, 10000, 10000L,
				60.0f, 50.0f, 20.0f);
	}

	private Show getValidShow() {
		return getValidShow("Test", "Test description");
	}

	@Test
	public void testCreateShow()
	{
		boolean exceptionCatched = false;
		Show show = new Show();

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
			Calendar start = Calendar.getInstance();
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
			Calendar limit = Calendar.getInstance();
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
			show.setPrice(-1.0f);
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
			show.setCommission(-1.0f);
			show = ticketService.createShow(show);
		} catch (InputValidationException e) {
			exceptionCatched = true;
		}
		assertTrue(exceptionCatched);
		try { removeShow(show.getId()); } catch (Exception e) { }
		exceptionCatched = false;

		try
		{
			show.setTickets(120L);
			show.setMaxTickets(100);
			show = ticketService.createShow(show);
		} catch (InputValidationException e) {
			exceptionCatched = true;
		}
		assertTrue(exceptionCatched);
		try { removeShow(show.getId()); } catch (Exception e) { }
	}

	@Test
	public void testUpdateShow()
	{
		boolean exceptionCatched = false;
		Show show = getValidShow();


		try {
			show = ticketService.createShow(show);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Unexpected exception");
		}

		long tmpId = show.getId();
		float tmpRealPrice = show.getPrice();

		try {
			show.setId(100L);
			ticketService.updateShow(show);
		} catch (InstanceNotFoundException e) {
			exceptionCatched = true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Unexpected exception");
		}
		assertTrue(exceptionCatched);
		exceptionCatched = false;
		show.setId(tmpId);

		try {
			ticketService.buyTickets(show.getId(), "foo@foo.com", "1234567891234567", 1);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Unexpected exception");
		}

		try {
            show.setPrice(10.0f);
		    show.setDiscountedPrice(50.0f);
			ticketService.updateShow(show);
		} catch (InputValidationException e) {
			exceptionCatched = true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Unexpected exception");
		}
		assertTrue(exceptionCatched);
		show.setPrice(tmpRealPrice);
		exceptionCatched = false;

		try {	
			show.setPrice(10.0f);
			show.setDiscountedPrice(5.0f);
			ticketService.updateShow(show);
		} catch (ShowHasReservationsException e) {
			exceptionCatched = true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Unexpected exception");
		}
		assertTrue(exceptionCatched);

		try { removeShow(show.getId()); } catch (Exception e) { }
		 
	}

	@Test
	public void testBuyTickets() {
		boolean exceptionCatched = false;
		Calendar date;
		Reservation reservation;
		Show show = getValidShow();
		String mail = "foo@foo.com";
		String creditCard = "1234567891011121";
		int ticketCount = 10;


		//
		date = Calendar.getInstance();
		date.add(Calendar.WEEK_OF_YEAR, 1);
		show.setStartDate(date);

		date = Calendar.getInstance();
		date.add(Calendar.DAY_OF_WEEK, 4);
		show.setLimitDate(date);

		try {
			show = ticketService.createShow(show);
		} catch (Exception e) { throw new RuntimeException("Unexpected exception"); }

		//
		try {
			reservation = ticketService.buyTickets(show.getId(), "fasda", creditCard, ticketCount);
		} catch (InputValidationException e) {
			exceptionCatched = true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Unexpected exception");
		}
		assertTrue(exceptionCatched);
		exceptionCatched = false;

		//
		try {
			reservation = ticketService.buyTickets(show.getId(), "fasda@asdsad", creditCard, ticketCount);
		} catch (InputValidationException e) {
			exceptionCatched = true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Unexpected exception");
		}
		assertTrue(exceptionCatched);
		exceptionCatched = false;

		//
		try {
			reservation = ticketService.buyTickets(show.getId(), mail, "adsa", ticketCount);
		} catch (InputValidationException e) {
			exceptionCatched = true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Unexpected exception");
		}
		assertTrue(exceptionCatched);
		exceptionCatched = false;

		//
		try {
			reservation = ticketService.buyTickets(show.getId(), mail, creditCard, 0);
		} catch (InputValidationException e) {
			exceptionCatched = true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Unexpected exception");
		}
		assertTrue(exceptionCatched);
		exceptionCatched = false;

		//
		try {
			reservation = ticketService.buyTickets(show.getId(), mail, creditCard, ticketCount);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Unexpected exception");
		}

		try {
			removeReservation(reservation.getId());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Unexpected exception");
		}

		try {
			removeShow(show.getId());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Unexpected exception");
		}
	}

	@Test
	public void testGetUserReservations() {
		Calendar date;
		Show show = getValidShow();

		//
		date = Calendar.getInstance();
		date.add(Calendar.WEEK_OF_YEAR, 1);
		show.setStartDate(date);

		date = Calendar.getInstance();
		date.add(Calendar.DAY_OF_WEEK, 4);
		show.setLimitDate(date);

		try {
			show = ticketService.createShow(show);
		} catch (Exception e) { throw new RuntimeException("Unexpected exception"); }

		//
		try {
			ticketService.buyTickets(show.getId(), "foo@foo.com", "1234512345123456", 2);
			ticketService.buyTickets(show.getId(), "laura@foo.com", "1234512345123456", 1);
			ticketService.buyTickets(show.getId(), "foo@foo.com", "1234512345123456", 3);
			ticketService.buyTickets(show.getId(), "pablo@foo.com", "1234512345123456", 4);
			ticketService.buyTickets(show.getId(), "foo@foo.com", "1234512345123456", 6);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Unexpected exception");
		}

		//
		List<Reservation> reservations;
		try {
			reservations = ticketService.getUserReservations("foo@foo.com");
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Unexpected exception");
		}
		assertEquals(reservations.size(), 3);

		//
		try {
			for (Reservation i: reservations) {
				removeReservation(i.getId());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Unexpected exception");
		}

		try {
			removeShow(show.getId());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Unexpected exception");
		}
	}

	@Test
	public void testCheckReservation() {
		boolean exceptionCatched = false;
		Reservation reservation;
		Calendar date;
		Show show = getValidShow();

		//
		date = Calendar.getInstance();
		date.add(Calendar.WEEK_OF_YEAR, 1);
		show.setStartDate(date);

		date = Calendar.getInstance();
		date.add(Calendar.DAY_OF_WEEK, 4);
		show.setLimitDate(date);

		try {
			show = ticketService.createShow(show);
		} catch (Exception e) { throw new RuntimeException("Unexpected exception"); }

		try {
			reservation = ticketService.buyTickets(show.getId(), "foo@foo.com", "1234512345123456", 2);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Unexpected exception");
		}
		assertTrue(reservation.isValid());

		try {
			ticketService.checkReservation(reservation.getCode(), "1234512345123456");
			reservation = findReservation(reservation.getCode());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Unexpected exception");
		}
		assertFalse(reservation.isValid());

		//
		try {				
			ticketService.checkReservation(reservation.getCode(), "1234512345123456");
		} catch (InputValidationException e) {
			exceptionCatched = true;
		} catch(ReservationAlreadyCheckedException e) {
			exceptionCatched = true;
		} catch (Exception e) {
		
			e.printStackTrace();
			throw new RuntimeException("Unexpected exception");
		}
		assertTrue(exceptionCatched);
		exceptionCatched = false;
		 

		//
		try {
			ticketService.checkReservation(reservation.getCode(), "6534512345123456");
		} catch (InputValidationException | CreditCardNotCoincidentException e) {
			exceptionCatched = true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Unexpected exception");
		}
		assertTrue(exceptionCatched);

		//
		try {
			removeReservation(reservation.getId());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Unexpected exception");
		}

		try {
			removeShow(show.getId());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Unexpected exception");
		}
	}

	@Test
	public void testFindShow()
	{
		boolean exceptionCatched = false;
		Show show = getValidShow();

		try {
			ticketService.findShow(12357L);
		} catch (InstanceNotFoundException e) {
			exceptionCatched = true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Unexpected exception");
		}
		assertTrue(exceptionCatched);
		exceptionCatched = false;


		try {
			show = ticketService.createShow(show);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Unexpected exception");
		}

		try {
			ticketService.findShow(show.getId());
		} catch (InstanceNotFoundException e) {
			e.printStackTrace();
			exceptionCatched = true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Unexpected exception");
		}
		assertFalse(exceptionCatched);
		exceptionCatched = false;


		try { removeShow(show.getId()); } catch (Exception e) { }

		try {
			ticketService.findShow(show.getId());
		} catch (InstanceNotFoundException e) {
			exceptionCatched = true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Unexpected exception");
		}
		assertTrue(exceptionCatched);
	}

	@Test
	public void testFindShows()
	{
		boolean exceptionCatched = false;
		Calendar date;

		try {
			date = Calendar.getInstance();

			Calendar end = Calendar.getInstance();
			end.add(Calendar.DAY_OF_WEEK, 8);

			ticketService.findShows(null, date, end);
		} catch (InputValidationException e) {
			exceptionCatched = true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Unexpected exception");
		}
		assertTrue(exceptionCatched);
		exceptionCatched = false;


		try {
			Calendar end = Calendar.getInstance();
			end.add(Calendar.DAY_OF_WEEK, 8);

			ticketService.findShows("hello", null, end);
		} catch (InputValidationException e) {
			exceptionCatched = true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Unexpected exception");
		}
		assertTrue(exceptionCatched);
		exceptionCatched = false;
		//
		try {
			date = Calendar.getInstance();
			ticketService.findShows("hello", date, null);
		} catch (InputValidationException e) {
			exceptionCatched = true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Unexpected exception");
		}
		assertTrue(exceptionCatched);
		exceptionCatched = false;

		try {

			Calendar end = Calendar.getInstance();
			end.add(Calendar.DAY_OF_WEEK, 8);

			date = (Calendar)end.clone();
			date.add(Calendar.DAY_OF_WEEK, 1);
			ticketService.findShows("hello", date, end);
		} catch (InputValidationException e) {
			exceptionCatched = true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Unexpected exception");
		}
		assertTrue(exceptionCatched);
		exceptionCatched = false;

		Show showOne = getValidShow("One", "One - The show");
		Show showTwo = getValidShow("Two", "Two - The show, but better");
		Show showThree = getValidShow("Three", "Three - The Pepe se cayó a un rio");

		date = Calendar.getInstance();
		date.add(Calendar.DAY_OF_WEEK, 10);
		showOne.setStartDate(date);

		date = Calendar.getInstance();
		date.add(Calendar.DAY_OF_WEEK, 4);
		showOne.setLimitDate(date);

		date = Calendar.getInstance();
		date.add(Calendar.DAY_OF_WEEK, 12);
		showTwo.setStartDate(date);

		date = Calendar.getInstance();
		date.add(Calendar.DAY_OF_WEEK, 5);
		showTwo.setLimitDate(date);

		date = Calendar.getInstance();
		date.add(Calendar.DAY_OF_WEEK, 14);
		showThree.setStartDate(date);

		date = Calendar.getInstance();
		date.add(Calendar.DAY_OF_WEEK, 6);
		showThree.setLimitDate(date);

		try {
			showOne = ticketService.createShow(showOne);
			showTwo = ticketService.createShow(showTwo);
			showThree = ticketService.createShow(showThree);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Unexpected exception");
		}

		List<Show> shows;
		try {
			date = Calendar.getInstance();
			Calendar end = Calendar.getInstance();
			end.add(Calendar.DAY_OF_WEEK, 20);

			shows = ticketService.findShows("show", date, end);
		
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Unexpected exception");
		}

		assertEquals(2, shows.size());
		assertNotEquals(shows.get(0), shows.get(1));	
		assertTrue(shows.get(0).equals(showOne) || shows.get(1).equals(showOne));	
		assertTrue(shows.get(0).equals(showTwo) || shows.get(1).equals(showTwo));

		shows.clear();
		try {
			shows = ticketService.findShows("show", null, null);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Unexpected exception");
		}

			assertEquals(2, shows.size());	
			assertNotEquals(shows.get(0), shows.get(1));	
			assertTrue(shows.get(0).equals(showOne) || shows.get(1).equals(showOne));	
			assertTrue(shows.get(0).equals(showTwo) || shows.get(1).equals(showTwo));	

		shows.clear();
		try {
			shows = ticketService.findShows("The", null, null);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Unexpected exception");
		}

		//assertEquals(3, shows.size());

		try { removeShow(showOne.getId()); } catch (Exception e) { }
		try { removeShow(showTwo.getId()); } catch (Exception e) { }
	}


	private void removeReservation(Long id, Reservation reservation)
	{
		DataSource dataSource = DataSourceLocator.getDataSource(Constants.DATA_SOURCE);

		try (Connection connection = dataSource.getConnection()) {

			try {

				// Prepare connection. 
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);

				// Do work. 
				reservationDao.remove(connection, id);

				// Commit. 
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

	@Test(expected = InstanceNotFoundException.class)
	public void buyTicketShowDoesntExist() throws InputValidationException, InstanceNotFoundException, NotEnoughAvailableTicketsException, LimitDateExceededException {

		Reservation res = ticketService.buyTickets((long) -1, "email@gmail.com", "5489162716279483", 1);

		removeReservation(res.getId(), res);

	}

	@Test(expected = InputValidationException.class)
	public void testBuyShowWithInvalidCreditCard() throws InputValidationException, InstanceNotFoundException, NotEnoughAvailableTicketsException, LimitDateExceededException {

		Show show = ticketService.createShow(getValidShow());
		try {
			Reservation res = ticketService.buyTickets((long)12, "email@gmail.com", "", 4);
			removeReservation(res.getId(), res);
		} finally {
			// Clear database
			removeShow(show.getId());
		}

	}

	@Test(expected = CreditCardNotCoincidentException.class)
	public void testCheckReservationExceptionIncorrectCreditCardNumber() throws InputValidationException, InstanceNotFoundException, NotEnoughAvailableTicketsException, LimitDateExceededException, CreditCardNotCoincidentException, ReservationAlreadyCheckedException {
		Show show = ticketService.createShow(getValidShow());
		Reservation res1 = ticketService.buyTickets(show.getId(), "email@gmail.com", "5489627352617220", 1);

		ticketService.checkReservation(res1.getCode(), "9403928472938170");

	}

	@Test(expected = InputValidationException.class)
	public void testBuyInvalidAmountOfTickets() throws InputValidationException, InstanceNotFoundException, NotEnoughAvailableTicketsException, LimitDateExceededException {

		Show show = ticketService.createShow(getValidShow());
		try {
			Reservation res = ticketService.buyTickets(12L, "email@gmail.com", "", -1);
			removeReservation(res.getId(), res);
		} finally {
			// Clear database
			removeShow(show.getId());
		}

	}

	@Test(expected = InputValidationException.class)
	public void testBuyMoreTicketsThanAvailable() throws InputValidationException, InstanceNotFoundException, NotEnoughAvailableTicketsException, LimitDateExceededException {

		Show show = ticketService.createShow(getValidShow());
		try {
			Reservation res = ticketService.buyTickets((long)12, "email@gmail.com", "", 3000);
			removeReservation(res.getId(), res);
		} finally {
			// Clear database
			removeShow(show.getId());
		}

	}

	@Test(expected = InputValidationException.class)
	public void testBuyAfterLimitDate() throws InputValidationException, InstanceNotFoundException, NotEnoughAvailableTicketsException, LimitDateExceededException {

		Show show = ticketService.createShow(getValidShow());

		Calendar limitDate = Calendar.getInstance();
		limitDate.set(Calendar.YEAR, 2003);
		limitDate.set(Calendar.MONTH, 5);
		limitDate.set(Calendar.DAY_OF_MONTH, 2);

		show.setLimitDate(limitDate);

		try {
			Reservation res = ticketService.buyTickets((long)12, "email@gmail.com", "", 3000);
			removeReservation(res.getId(), res);
		} finally {
			// Clear database
			removeShow(show.getId());
		}

	}

	@Test(expected = ReservationAlreadyCheckedException.class)
	public void testCheckReservationExceptionChecked() throws InputValidationException, InstanceNotFoundException, NotEnoughAvailableTicketsException, LimitDateExceededException, CreditCardNotCoincidentException, ReservationAlreadyCheckedException {
		Show show = ticketService.createShow(getValidShow());
		Reservation res1 = ticketService.buyTickets(show.getId(), "email@gmail.com", "5489627352617220", 1);
		ticketService.checkReservation(res1.getCode(), "5489627352617220");
		ticketService.checkReservation(res1.getCode(), "5489627352617220");

	}

}