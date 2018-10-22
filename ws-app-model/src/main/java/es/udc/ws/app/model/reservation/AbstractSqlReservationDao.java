package es.udc.ws.app.model.reservation;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


/**
 * A partial implementation of
 * <code>SqlReservationDao</code> that leaves
 * <code>create(Connection, Show)</code> as abstract.
 */
public abstract class AbstractSqlReservationDao implements SqlReservationDao
{
    @Override
    public void update(Connection c, Reservation reservation)
    {
        String query = "";

        try (PreparedStatement preparedStatement = c.prepareStatement(query))
        {
            int updatedRows = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Reservation> find(Connection c, String email)
    {
        String query = "";

        try (PreparedStatement preparedStatement = c.prepareStatement(query))
        {
            ResultSet resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public Reservation find(Connection c, long code)
    {
        String query = "";

        try (PreparedStatement preparedStatement = c.prepareStatement(query))
        {
            ResultSet resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}
