package es.udc.ws.app.model.service;

import es.udc.ws.app.model.service.exceptions.LimitDateExceeded;
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
        PropertyValidator.validateAfterDate("limit date", show.getStartDate(), show.getLimitDate());
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
        PropertyValidator.validateAfterDate("limit date", show.getLimitDate(), show.getStartDate());
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

                if (current.getSoldTickets() > 0 && show.getRealPrice() > current.getRealPrice()) {
                    connection.rollback();
                    throw new ShowHasReservations("Cannot decrease the show price when it has sold tickets");
                }

                showDao.update(connection, show);

                /* Commit. */
                connection.commit();
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

        throw new ShowHasReservations("Cannot decrease the price");
    }

    @Override
    public Show findShow(Long code) throws InstanceNotFoundException {
     
    	try (Connection connection = dataSource.getConnection()) {
			return showDao.find(connection, code);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	@Override/*Probar*/
	public List<Show> findShows(String words, Calendar start, Calendar end) {

		try (Connection connection = dataSource.getConnection()) {
			return showDao.find(connection, words, start, end);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override/*Probar*/
	public Reservation buyTickets(Long showId, String email, String cardNumber, int count) throws InstanceNotFoundException, InputValidationException {

		PropertyValidator.validateCreditCard(cardNumber);

		try (Connection c = dataSource.getConnection()) {

			try {
				c.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				c.setAutoCommit(false);

				/* Do work. */
				Show show = showDao.find(c, showId);
				Calendar expirationDate = Calendar.getInstance();

				Reservation res = new Reservation();
				res.setShowId(showId);
				res.setEmail(email);
				res.setCardNumber(cardNumber);
				res.setTickets(count);

				long availableTickets = show.getMaxTickets() - show.getSoldTickets();

				if( (show.getLimitDate().before(expirationDate)) && (availableTickets <= count) ) {
					reservationDao.create(c, res);

					Random codeGenerated = new Random();
					
					showDao.update(c, show);
					reservationDao.update(c, res);
					
					
					
					/* Commit. */ 
					c.commit();
					
					return res;
					
				} else {
					throw new InputValidationException("Show cannot be bought."); 
				}


			} catch (InstanceNotFoundException e) {
				c.commit();
				throw e;
			} catch (SQLException e) {
				c.rollback();
				throw new RuntimeException(e);
			} catch (RuntimeException | Error e) {
				c.rollback();
				throw e;
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

    @Override
    public List<Reservation> getUserReservations(String email) {
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
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
