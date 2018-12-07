package es.udc.ws.app.model.reservation;

import java.sql.*;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

public class Jdbc3CcSqlReservationDao extends AbstractSqlReservationDao
{
    @Override
    public Reservation create(Connection c, Reservation reservation)
    {
        String query =
                "INSERT INTO " +
                "ReservationTable(" +
                        "showId, " +
                        "email, " +
                        "creditCard, " +
                        "tickets, " +
                        "isValid, " +
                        "code, " +
                        "reservationDate, " +
                        "price) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = c.prepareStatement(query, Statement.RETURN_GENERATED_KEYS))
        {
            int index = 1;

            ps.setLong(index++, reservation.getShowId());
            ps.setString(index++, reservation.getEmail());
            ps.setString(index++, reservation.getCreditCard());
            ps.setInt(index++, reservation.getTickets());
            ps.setBoolean(index++, reservation.isValid());
            ps.setString(index++, reservation.getCode());
            ps.setTimestamp(index++, new Timestamp(reservation.getReservationDate().getTimeInMillis()));
            ps.setFloat(index, reservation.getPrice());
            ps.executeUpdate();

            ResultSet keysResultSet = ps.getGeneratedKeys();

            if (!keysResultSet.next())
                throw new SQLException("JDBC driver did not return generated key.");

            reservation.setId(keysResultSet.getLong(1));
            
            return reservation;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

	@Override
	public void remove(Connection c, Long id) throws InstanceNotFoundException {
		// TODO Auto-generated method stub
		
	}
}
