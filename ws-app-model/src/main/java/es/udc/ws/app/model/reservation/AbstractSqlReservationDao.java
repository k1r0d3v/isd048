package es.udc.ws.app.model.reservation;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A partial implementation of
 * <code>SqlReservationDao</code> that leaves
 * <code>create(Connection, Show)</code> as abstract.
 */
public abstract class AbstractSqlReservationDao implements SqlReservationDao
{
    @Override
    public void update(Connection c, Reservation reservation) throws InstanceNotFoundException
    {
        String query = "UPDATE ReservationTable SET " +
                "showId = ?, " +
                "email = ?, " +
                "cardNumber = ?, " +
                "tickets = ?, " +
                "isValid = ?, " +
                "code = ?, " +
                "reservationDate = ?, " +
                "price = ? " +
                "WHERE id=?";

        try (PreparedStatement ps = c.prepareStatement(query))
        {
            int index = 1;

            ps.setLong(index++, reservation.getShowId());
            ps.setString(index++, reservation.getEmail());
            ps.setString(index++, reservation.getCardNumber());
            ps.setInt(index++, reservation.getTickets());
            ps.setBoolean(index++, reservation.isValid());
            ps.setString(index++, reservation.getCode());
            ps.setTimestamp(index++, new Timestamp(reservation.getReservationDate().getTimeInMillis()));
            ps.setFloat(index++, reservation.getPrice());
            ps.setLong(index, reservation.getId());
            int updatedRows = ps.executeUpdate();

            if (updatedRows == 0)
                throw new InstanceNotFoundException(reservation.getId(),
                                                    Reservation.class.getName());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(Connection c, Long id)
            throws InstanceNotFoundException
    {
        String query = "DELETE FROM ReservationTable WHERE id = ?";

        try (PreparedStatement preparedStatement = c.prepareStatement(query))
        {
            preparedStatement.setLong(1, id);

            if (preparedStatement.executeUpdate() == 0)
                throw new InstanceNotFoundException(id, Reservation.class.getName());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Reservation> findByEmail(Connection c, String email)
    {
        String query = "SELECT " +
                "id," +
                "showId, " +
                "email, " +
                "cardNumber, " +
                "tickets, " +
                "isValid, " +
                "code, " +
                "reservationDate, " +
                "price " +
                "FROM ReservationTable " +                
                "WHERE email=?";

        try (PreparedStatement ps = c.prepareStatement(query))
        {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            List<Reservation> reservations = new ArrayList<>();

            while (rs.next()) {
                Reservation r = new Reservation();
                int index = 1;

                r.setId(rs.getLong(index++));
                r.setShowId(rs.getLong(index++));
                r.setEmail(rs.getString(index++));
                r.setCardNumber(rs.getString(index++));
                r.setTickets(rs.getInt(index++));
                r.setValid(rs.getBoolean(index++));
                r.setCode(rs.getString(index++));

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(rs.getTimestamp(index++));
                r.setReservationDate(calendar);

                r.setPrice(rs.getFloat(index));

                reservations.add(r);
            }

            return reservations;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Reservation findByCode(Connection c, String code) throws InstanceNotFoundException
    {
        String query = "SELECT " +
                "id," +
                "showId, " +
                "email, " +
                "cardNumber, " +
                "tickets, " +
                "isValid, " +
                "code, " +
                "reservationDate, " +
                "price " +
                "FROM ReservationTable " +
                "WHERE code=?";

        try (PreparedStatement ps = c.prepareStatement(query))
        {
            ps.setString(1, code);
            ResultSet rs = ps.executeQuery();


            if (!rs.next())
                throw new InstanceNotFoundException(code, Reservation.class.getName());

            Reservation r = new Reservation();
            int index = 1;

            r.setId(rs.getLong(index++));
            r.setShowId(rs.getLong(index++));
            r.setEmail(rs.getString(index++));
            r.setCardNumber(rs.getString(index++));
            r.setTickets(rs.getInt(index++));
            r.setValid(rs.getBoolean(index++));
            r.setCode(rs.getString(index++));

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(rs.getTimestamp(index++));
            r.setReservationDate(calendar);

            r.setPrice(rs.getFloat(index));


            return r;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
