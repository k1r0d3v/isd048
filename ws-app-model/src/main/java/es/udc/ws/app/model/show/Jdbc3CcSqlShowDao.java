package es.udc.ws.app.model.show;

import java.sql.*;

public class Jdbc3CcSqlShowDao extends AbstractSqlShowDao
{
	@Override
	public Show create(Connection c, Show show)
	{
		String query = "INSERT INTO ShowTable " +
				"(name, " +
				"description, " +
				"startDate, " +
				"duration, " +
				"limitDate, " +
				"maxTickets, " +
				"soldTickets, " +
				"realPrice, " +
				"discountedPrice, " +
				"salesCommission) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (PreparedStatement preparedStatement = c.prepareStatement(
				query, Statement.RETURN_GENERATED_KEYS)) {

			int i = 1;
			preparedStatement.setString(i++, show.getName());
			preparedStatement.setString(i++, show.getDescription());
			preparedStatement.setTimestamp(i++, new Timestamp(show.getStartDate().getTimeInMillis()));
			preparedStatement.setLong(i++, show.getDuration());
			preparedStatement.setTimestamp(i++, new Timestamp(show.getLimitDate().getTimeInMillis()));
			preparedStatement.setLong(i++, show.getMaxTickets());
			preparedStatement.setLong(i++, show.getSoldTickets());
			preparedStatement.setFloat(i++, show.getRealPrice());
			preparedStatement.setFloat(i++, show.getDiscountedPrice());
			preparedStatement.setFloat(i, show.getSalesCommission());

			preparedStatement.executeUpdate();


			ResultSet resultSet = preparedStatement.getGeneratedKeys();

			if (!resultSet.next())
				throw new SQLException("JDBC driver did not return generated key.");

			show.setId(resultSet.getLong(1));
			return show;

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
