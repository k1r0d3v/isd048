package es.udc.ws.app.model.show;

import java.sql.*;

public class Jdbc3CcSqlShowDao extends AbstractSqlShowDao
{
    @Override
    public Show create(Connection c, Show show)
    {
        String query =
                "INSERT INTO " +
                "TABLE(field1, field2, field3, ...) " +
                "VALUES (?, ?, ?, ...)";

        try (PreparedStatement ps = c.prepareStatement(query, Statement.RETURN_GENERATED_KEYS))
        {
            ps.executeUpdate();

            ResultSet keysResultSet = ps.getGeneratedKeys();

            if (!keysResultSet.next())
                throw new SQLException("JDBC driver did not return generated key.");

            long id = keysResultSet.getLong(1);

            //
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}
