package es.udc.ws.app.model.service;

import es.udc.ws.app.model.service.exceptions.*;
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
        PropertyValidator.validateLong("duration", show.getDuration(), 1, Integer.MAX_VALUE);
        PropertyValidator.validateBeforeDate("limit date", show.getLimitDate(), show.getStartDate());
        PropertyValidator.validateLong("max number of tickets", show.getMaxTickets(), 1, Integer.MAX_VALUE);
        PropertyValidator.validateFloat("real price", show.getRealPrice(), 0.0f, Float.MAX_VALUE);
        PropertyValidator.validateFloat("discounted price", show.getDiscountedPrice(), 0.0f, Float.MAX_VALUE);
        PropertyValidator.validateFloat("commission", show.getSalesCommission(), 0.0f, Float.MAX_VALUE);
        PropertyValidator.validateMajorEqualsThan("real price", "discounted price", show.getRealPrice(), show.getDiscountedPrice());


        try (Connection connection = dataSource.getConnection())
        {
            try
            {
                /* Prepare connection. */
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

				show.setAvailableTickets(show.getMaxTickets());
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
        throws InstanceNotFoundException, InputValidationException, ShowHasReservations, NotEnoughAvailableTickets
    {
        PropertyValidator.validateMandatoryString("name", show.getName());
        PropertyValidator.validateMandatoryString("description", show.getDescription());
        PropertyValidator.validateFutureDate("start date", show.getStartDate());
        PropertyValidator.validateLong("duration", show.getDuration(), 1, Integer.MAX_VALUE);
        PropertyValidator.validateBeforeDate("limit date", show.getLimitDate(), show.getStartDate());
        PropertyValidator.validateLong("max number of tickets", show.getMaxTickets(), 1, Integer.MAX_VALUE);
        PropertyValidator.validateFloat("real price", show.getRealPrice(), 0.0f, Float.MAX_VALUE);
        PropertyValidator.validateFloat("discounted price", show.getDiscountedPrice(), 0.0f, Float.MAX_VALUE);
        PropertyValidator.validateFloat("commission", show.getSalesCommission(), 0.0f, Float.MAX_VALUE);
        PropertyValidator.validateMajorEqualsThan("real price", "discounted price", show.getRealPrice(), show.getDiscountedPrice());


        try (Connection connection = dataSource.getConnection()) {

            try {

                /* Prepare connection. */
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                Show current = showDao.find(connection, show.getId());

                long ticketDifference = show.getMaxTickets() - current.getMaxTickets();

                if (ticketDifference < 0 && ticketDifference + current.getAvailableTickets() < 0) {
                    connection.commit();
                    throw new NotEnoughAvailableTickets("Cannot decrease max tickets when there is not enough available tickets");
                }

                long soldTicketsCurrent = current.getMaxTickets() - current.getAvailableTickets();
                long soldTicketsShow = show.getMaxTickets() - show.getAvailableTickets();
                boolean isLessPrice = show.getRealPrice() < current.getRealPrice();

                if ((isLessPrice && soldTicketsCurrent > 0) || (isLessPrice && soldTicketsShow > 0) ) {
                    connection.commit();
                    throw new ShowHasReservations("Cannot decrease the show price when it has sold tickets");
                }

                current.setName(show.getName());
                current.setDescription(show.getDescription());
                current.setStartDate(show.getStartDate());
                current.setDuration(show.getDuration());
                current.setLimitDate(show.getLimitDate());
                current.setMaxTickets(show.getMaxTickets());
                current.setAvailableTickets(show.getAvailableTickets() + ticketDifference);
                current.setRealPrice(show.getRealPrice());
                current.setDiscountedPrice(show.getDiscountedPrice());
                current.setSalesCommission(show.getSalesCommission());


                showDao.update(connection, current);

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
    public Show findShow(long id)
            throws InstanceNotFoundException
    {
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
        PropertyValidator.validateMandatoryString("words", words);

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
	public Reservation buyTickets(long showId, String email, String cardNumber, int count)
            throws InstanceNotFoundException, InputValidationException, NotEnoughAvailableTickets, LimitDateExceeded
    {

		PropertyValidator.validateCreditCard(cardNumber);
		PropertyValidator.validateEMail(email);
		PropertyValidator.validateLong("count", (long)count, 1, Integer.MAX_VALUE);

		try (Connection connection = dataSource.getConnection()) {

			try
            {
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);

				Show show = showDao.find(connection, showId);

				if (Calendar.getInstance().after(show.getLimitDate())) {
				    connection.commit();
                    throw new LimitDateExceeded("Can not buy tickets after limit date");
                }

				if (show.getAvailableTickets() <= 0) {
                    connection.commit();
                    throw new NotEnoughAvailableTickets("There is not resting tickets");
                }

				if (show.getAvailableTickets() < count) {
				    connection.commit();
                    throw new NotEnoughAvailableTickets("There is not enough tickets");
                }

                show.setAvailableTickets(show.getAvailableTickets() + count);
                showDao.update(connection, show);

                Reservation reservation = new Reservation();
                reservation.setShowId(show.getId());
                reservation.setEmail(email);
                reservation.setCardNumber(cardNumber);
                reservation.setTickets(count);
                reservation.setValid(true);
                reservation.setCode(UUID.randomUUID().toString());
                reservation.setReservationDate(Calendar.getInstance());
                reservation.setPrice(show.getDiscountedPrice());

                reservation = reservationDao.create(connection, reservation);

                connection.commit();

                return reservation;

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
    public List<Reservation> getUserReservations(String email)
            throws InputValidationException {
	    PropertyValidator.validateEMail(email);

        try (Connection connection = dataSource.getConnection()) {
            return reservationDao.findByEmail(connection, email);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void checkReservation(String code, String cardNumber)
            throws InstanceNotFoundException, InputValidationException, CreditCardNotCoincident, ReservationAlreadyChecked
    {
        PropertyValidator.validateCreditCard(cardNumber);

        try (Connection connection = dataSource.getConnection()) {

            try {

                /* Prepare connection. */
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                /* Do work. */
                Reservation reservation = reservationDao.findByCode(connection, code);

                if (!reservation.getCardNumber().equals(cardNumber)) {
                    connection.commit();
                    throw new CreditCardNotCoincident();
                }

               if (!reservation.isValid()) {
                   connection.commit();
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
