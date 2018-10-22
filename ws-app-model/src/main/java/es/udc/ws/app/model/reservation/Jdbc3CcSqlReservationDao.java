package es.udc.ws.app.model.reservation;

import java.sql.*;
import java.util.Calendar;

public class Jdbc3CcSqlReservationDao extends AbstractSqlReservationDao
{
    @Override
    public Reservation create(Connection c, Reservation reservation)
    {
        String query =
                "INSERT INTO " +
                "Reservation(id, showId, email, cardNumber, tickets, isValid, code, reservationDate, price) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = c.prepareStatement(query, Statement.RETURN_GENERATED_KEYS))
        {
            int i = 1;

            ps.setLong(i++, reservation.getId());
            ps.setLong(i++, reservation.getShowId());
            ps.setString(i++, reservation.getEmail());
            ps.setString(i++, reservation.getCardNumber());
            ps.setInt(i++, reservation.getTickets());
            ps.setBoolean(i++, reservation.isValid());
            ps.setString(i++, reservation.getCode());
            ps.setTimestamp(i++, new Timestamp(reservation.getReservationDate().getTimeInMillis()));
            ps.setFloat(i, reservation.getPrice());
            ps.executeUpdate();

            ResultSet keysResultSet = ps.getGeneratedKeys();

            if (!keysResultSet.next())
                throw new SQLException("JDBC driver did not return generated key.");

            long id = keysResultSet.getLong(1);

            reservation.setId(id);
            
            return reservation;            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
