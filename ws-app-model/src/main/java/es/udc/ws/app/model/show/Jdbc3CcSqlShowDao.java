package es.udc.ws.app.model.show;

import java.sql.*;

public class Jdbc3CcSqlShowDao extends AbstractSqlShowDao
{
	@Override
	public Show create(Connection c, Show show)
	{
		String query = "INSERT INTO Show" + " (name, description, startDate, startDate, duration, limitDate, maxTickets, soldTickets, realPrice, discountedPrice)" + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";		
		try (PreparedStatement preparedStatement = c.prepareStatement(
				query, Statement.RETURN_GENERATED_KEYS)) {


			int index = 1;
			preparedStatement.setString(index++, show.getName());
			preparedStatement.setString(index++, show.getDescription());
			preparedStatement.setTimestamp(index++, new Timestamp(show.getStartDate().getTimeInMillis()));
			preparedStatement.setLong(index++, show.getDuration());
			preparedStatement.setTimestamp(index++, new Timestamp(show.getLimitDate().getTimeInMillis()));
			preparedStatement.setLong(index++, show.getMaxTickets());           
			preparedStatement.setLong(index++, show.getSoldTickets());
			preparedStatement.setFloat(index++, show.getRealPrice());
			preparedStatement.setFloat(index++, show.getDiscountedPrice());
			preparedStatement.setFloat(index++, show.getSalesCommission());

			Timestamp date = show.getStartDate() != null ? new Timestamp(show.getStartDate().getTime().getTime()) : null;
			preparedStatement.setTimestamp(index++, date);

			preparedStatement.executeUpdate();


			ResultSet resultSet = preparedStatement.getGeneratedKeys();

			if (!resultSet.next()) {
				throw new SQLException(
						"JDBC driver did not return generated key.");
			}
			Long showId = resultSet.getLong(1);

			return new Show();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
