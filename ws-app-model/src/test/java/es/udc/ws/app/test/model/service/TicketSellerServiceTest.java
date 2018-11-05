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

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;

public class TicketSellerServiceTest
{
	private static TicketSellerService ticketService;
	private static SqlShowDao showDao;

	private final long id = 123456789;
	private final String showName = "A star is born";
	private final Calendar showStartDate = Calendar.getInstance();
	private final long showDuration = (long) 135;
	private Calendar showLimitDate = Calendar.getInstance();
	private long showMaxTickets = (long) 150;
	private long showSoldTickets = (long) 25;
	private float showRealPrice = (float) 7.00;
	private float showDiscountedPrice = (float) 5.90;
	private float showSalesCommission = (float) 0.20;

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

	private Show getValidShow() {
		Show validShow = new Show();
		validShow.setId(id);
		validShow.setName(showName);
		validShow.setStartDate(showStartDate);
		validShow.setDuration(showDuration);
		validShow.setLimitDate(showLimitDate);
		validShow.setMaxTickets(showMaxTickets);
		validShow.setSoldTickets(showSoldTickets);
		validShow.setRealPrice(showRealPrice);
		validShow.setDiscountedPrice(showDiscountedPrice);
		validShow.setSalesCommission(showSalesCommission);

		return validShow;

	}

	@Test
	public void findAndAddShowTest() throws InputValidationException, InstanceNotFoundException {
		Show show = getValidShow();
		Show addedShow = null;

		try {

			addedShow = ticketService.createShow(show);
			Show foundShow = ticketService.findShow(addedShow.getId());

			assertEquals(addedShow, foundShow);

		} finally {
			/*Clear Database.*/
			if (addedShow!=null) {
				removeShow(addedShow.getId());
			}
		}

	}
}