package es.udc.ws.app.model.show;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

/**
 * A partial implementation of
 * <code>SqlShowDao</code> that leaves
 * <code>create(Connection, Show)</code> as abstract.
 */
public abstract class AbstractSqlShowDao implements SqlShowDao
{
    @Override
    public void update(Connection c, Show show)
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
    public Show find(Connection c, Long id)
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
    public List<Show> find(Connection c, String words, Calendar startDate, Calendar endDate)
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
