package es.udc.ws.app.model.service;

import es.udc.ws.app.model.reservation.Reservation;
import es.udc.ws.app.model.reservation.SqlReservationDao;
import es.udc.ws.app.model.reservation.SqlReservationDaoFactory;
import es.udc.ws.app.model.service.exceptions.LimitDateExceeded;
import es.udc.ws.app.model.service.exceptions.ReservationAlreadyChecked;
import es.udc.ws.app.model.show.Show;
import es.udc.ws.app.model.show.SqlShowDao;
import es.udc.ws.app.model.show.SqlShowDaoFactory;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

public class TicketSellerImpl implements TicketSellerService
{
    private final DataSource dataSource;
    private SqlReservationDao reservationDao;
    private SqlShowDao showDao;

    public TicketSellerImpl() {
        dataSource = DataSourceLocator.getDataSource("ws-javaexamples-ds");
        reservationDao = SqlReservationDaoFactory.getDao();
        showDao = SqlShowDaoFactory.getDao();
    }

    @Override
    public Show createShow(Show show) throws InputValidationException {
        return null;
    }

    @Override/*R*/
    public Show findShow(String code) throws InstanceNotFoundException {
     
    	try (Connection connection = dataSource.getConnection()) {
			return showDao.find(connection, code);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

    }

    @Override/*R*/
    public List<Show> findShows(String words, Calendar start, Calendar end) {
    	
    	try (Connection connection = dataSource.getConnection()) {
			return showDao.find(connection, words, start, end);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
    }

    @Override
    public Reservation bookTickets(Long showId, String email, String cardNumber, int count) throws LimitDateExceeded {
        return null;
    }

    @Override
    public List<Reservation> getUserReservations(String email) {
        return null;
    }

    @Override
    public void checkReservation(String code) throws InstanceNotFoundException, ReservationAlreadyChecked {

    }
}
