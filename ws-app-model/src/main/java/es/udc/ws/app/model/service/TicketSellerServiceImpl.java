package es.udc.ws.app.model.service;

import es.udc.ws.app.model.service.exceptions.ReservationAlreadyChecked;
import es.udc.ws.app.model.service.exceptions.ShowHasReservations;
import es.udc.ws.app.model.show.Show;
import es.udc.ws.app.model.show.SqlShowDao;
import es.udc.ws.app.model.show.SqlShowDaoFactory;
import es.udc.ws.app.model.util.Constants;
import es.udc.ws.app.model.util.validation.PropertyValidator;
import es.udc.ws.app.model.reservation.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class TicketSellerServiceImpl implements TicketSellerService
{
	private final DataSource dataSource;
	private SqlReservationDao reservationDao;
	private SqlShowDao showDao;

	public TicketSellerServiceImpl() {
		dataSource = DataSourceLocator.getDataSource(Constants.DATA_SOURCE);
		reservationDao = SqlReservationDaoFactory.getDao();
		showDao = SqlShowDaoFactory.getDao();
	}

    @Override
    public Show createShow(Show show)
            throws InputValidationException
    {
        PropertyValidator.validateMandatoryString("name", show.getName());
        PropertyValidator.validateMandatoryString("description", show.getDescription());
        PropertyValidator.validateFutureDate("start date", show.getStartDate());
        PropertyValidator.validateLong("duration", show.getDuration(), 0, Integer.MAX_VALUE);
        PropertyValidator.validateBeforeDate("limit date", show.getLimitDate(), show.getStartDate());
        PropertyValidator.validateLong("max number of tickets", show.getMaxTickets(), 1, Integer.MAX_VALUE);
        PropertyValidator.validateFloat("real price", show.getRealPrice(), 0.0f, Float.MAX_VALUE);
        PropertyValidator.validateFloat("discounted price", show.getDiscountedPrice(), 0.0f, Float.MAX_VALUE);
        PropertyValidator.validateFloat("commission", show.getSalesCommission(), 0.0f, Float.MAX_VALUE);


        try (Connection connection = dataSource.getConnection())
        {
            try
            {
                /* Prepare connection. */
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

				/* Do work. */
				Show createdShow = showDao.create(connection, show);

				/* Commit. */
				connection.commit();

				return createdShow;

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

    @Override
    public void updateShow(Show show)
        throws InstanceNotFoundException, InputValidationException
    {
        PropertyValidator.validateMandatoryString("name", show.getName());
        PropertyValidator.validateMandatoryString("description", show.getDescription());
        PropertyValidator.validateFutureDate("start date", show.getStartDate());
        PropertyValidator.validateLong("duration", show.getDuration(), 0, Integer.MAX_VALUE);
        PropertyValidator.validateBeforeDate("limit date", show.getLimitDate(), show.getStartDate());
        PropertyValidator.validateLong("max number of tickets", show.getMaxTickets(), 1, Integer.MAX_VALUE);
        PropertyValidator.validateFloat("real price", show.getRealPrice(), 0.0f, Float.MAX_VALUE);
        PropertyValidator.validateFloat("discounted price", show.getDiscountedPrice(), 0.0f, Float.MAX_VALUE);
        PropertyValidator.validateFloat("commission", show.getSalesCommission(), 0.0f, Float.MAX_VALUE);


        try (Connection connection = dataSource.getConnection()) {

            try {

                /* Prepare connection. */
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                /* Do work. */
                Show current = showDao.find(connection, show.getId());

                boolean isLessPrice = show.getRealPrice() < current.getRealPrice();
                if (isLessPrice && current.getSoldTickets() > 0 ||
                    isLessPrice && show.getSoldTickets() > 0) {
                    connection.rollback();
                    throw new ShowHasReservations("Cannot decrease the show price when it has sold tickets");
                }

                showDao.update(connection, show);

                /* Commit. */
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error | InstanceNotFoundException e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Show findShow(Long id)
            throws InstanceNotFoundException, InputValidationException
    {
        if (id == null)
            throw new InputValidationException("code can not be null");

    	try (Connection connection = dataSource.getConnection()) {
			return showDao.find(connection, id);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<Show> findShows(String words, Calendar start, Calendar end)
            throws InputValidationException
    {
	    if (words == null)
	        throw new InputValidationException("words can not be null");

	    if (start == null && end != null)
	        throw new InputValidationException("end must be null if start is null");

	    if (start != null && end == null)
            throw new InputValidationException("end can not be null if start is not null");

	    if (start != null)
        {
            if (!start.equals(end))
                if (start.after(end))
                    throw new InputValidationException("end date must be greater than or equals to start date");
        }

		try (Connection connection = dataSource.getConnection()) {
			return showDao.find(connection, words, start, end);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Reservation buyTickets(Long showId, String email, String cardNumber, int count)
            throws InstanceNotFoundException, InputValidationException
    {

		PropertyValidator.validateCreditCard(cardNumber);
		PropertyValidator.validateEMail(email);
		PropertyValidator.validateLong("showId", showId, Integer.MIN_VALUE, Integer.MAX_VALUE);
		PropertyValidator.validateLong("count", (long)count, 1, Integer.MAX_VALUE);

		try (Connection connection = dataSource.getConnection()) {

			try {
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);

				Show show = showDao.find(connection, showId);
				if (Calendar.getInstance().after(show.getLimitDate()))
				    throw new InputValidationException("Limit date exceeded");

                long restingTickets = show.getMaxTickets() - show.getSoldTickets();
				if (restingTickets <= 0)
				    throw new InputValidationException("There is not resting tickets");

				if (restingTickets < count)
                    throw new InputValidationException("There is not enough tickets");

                show.setSoldTickets(show.getSoldTickets() - count);
                showDao.update(connection, show);

                Reservation r = new Reservation();
                r.setShowId(show.getId());
                r.setEmail(email);
                r.setCardNumber(cardNumber);
                r.setTickets(count);
                r.setValid(true);
                r.setCode(UUID.randomUUID().toString());
                r.setReservationDate(Calendar.getInstance());
                r.setPrice(show.getDiscountedPrice()); // NOTE: Que poner aqui? esa es la cuestión

                r = reservationDao.create(connection, r);

                /* Commit. */
                connection.commit();

                return r;
			} catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error | InstanceNotFoundException e) {
                connection.rollback();
                throw e;
            }

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

    @Override
    public List<Reservation> getUserReservations(String email) throws InputValidationException {
	    PropertyValidator.validateEMail(email);

        try (Connection connection = dataSource.getConnection()) {
            return reservationDao.findByEmail(connection, email);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void checkReservation(String code, String cardNumber)
            throws InstanceNotFoundException, InputValidationException
    {
        try (Connection connection = dataSource.getConnection()) {

            try {

                /* Prepare connection. */
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                /* Do work. */
                Reservation reservation = reservationDao.findByCode(connection, code);

                if (!reservation.getCardNumber().equals(cardNumber)) {
                    connection.rollback();
                    throw new InputValidationException("Credit card not coincident");
                }

                if (!reservation.isValid()) {
                    connection.rollback();
                    throw new ReservationAlreadyChecked();
                }

                reservation.setValid(false);
                reservationDao.update(connection, reservation);

                /* Commit. */
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error | InstanceNotFoundException e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
