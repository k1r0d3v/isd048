package es.udc.ws.app.model.reservation;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        String query = "update Reservation set isValid=? where id=?";

        try (PreparedStatement ps = c.prepareStatement(query))
        {
            int i = 1;
            ps.setBoolean(i++, reservation.isValid());
            ps.setLong(i, reservation.getId());
            int updatedRows = ps.executeUpdate();

            if (updatedRows == 0)
                throw new InstanceNotFoundException(reservation.getId(),
                                                    Reservation.class.getName());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Reservation> findByEmail(Connection c, String email)
    {
        String query = "select * from Reservation where email=?";

        try (PreparedStatement ps = c.prepareStatement(query))
        {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            List<Reservation> reservations = new ArrayList<>();

            while (rs.next()) {
                Reservation r = new Reservation();
                int i = 1;

                r.setId(rs.getLong(i++));
                r.setShowId(rs.getLong(i++));
                r.setEmail(rs.getString(i++));
                r.setCardNumber(rs.getString(i++));
                r.setTickets(rs.getInt(i++));
                r.setValid(rs.getBoolean(i++));
                r.setCode(rs.getString(i++));

                Calendar rd = Calendar.getInstance();
                rd.setTime(rs.getTimestamp(i++));
                r.setReservationDate(rd);

                r.setPrice(rs.getFloat(i));

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
        String query = "select * from Reservation where code=?";

        try (PreparedStatement ps = c.prepareStatement(query))
        {
            ps.setString(1, code);
            ResultSet rs = ps.executeQuery();


            if (!rs.next())
                throw new InstanceNotFoundException(code, Reservation.class.getName());

            Reservation r = new Reservation();
            int i = 1;

            r.setId(rs.getLong(i++));
            r.setShowId(rs.getLong(i++));
            r.setEmail(rs.getString(i++));
            r.setCardNumber(rs.getString(i++));
            r.setTickets(rs.getInt(i++));
            r.setValid(rs.getBoolean(i++));
            r.setCode(rs.getString(i++));

            Calendar rd = Calendar.getInstance();
            rd.setTime(rs.getTimestamp(i++));
            r.setReservationDate(rd);

            r.setPrice(rs.getFloat(i));

            return r;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
