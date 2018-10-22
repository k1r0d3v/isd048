package es.udc.ws.app.model.show;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
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
	public void update(Connection c, Show show) throws InstanceNotFoundException
	{
		String query = "UPDATE Show" + " SET name = ?, description = ?, startDate = ?, startDate = ?, duration = ?, limitDate = ?, maxTickets = ?, soldTickets = ? " + "realPrice = ?, discountedPrice = ? WHERE id = ?";

		try (PreparedStatement preparedStatement = c.prepareStatement(query)) {

			int index = 1;

			preparedStatement.setString(index++, show.getName());
			preparedStatement.setString(index++, show.getDescription());
			preparedStatement.setTimestamp(index++, new Timestamp(show.getStartDate().getTimeInMillis()));////
			preparedStatement.setLong(index++, show.getDuration());
			preparedStatement.setTimestamp(index++, new Timestamp(show.getLimitDate().getTimeInMillis()));////
			preparedStatement.setLong(index++, show.getMaxTickets());           
			preparedStatement.setLong(index++, show.getSoldTickets());
			preparedStatement.setFloat(index++, show.getRealPrice());
			preparedStatement.setFloat(index++, show.getDiscountedPrice());
			preparedStatement.setFloat(index++, show.getSalesCommission());

			int updatedRows = preparedStatement.executeUpdate();

			if (updatedRows == 0) {
				throw new InstanceNotFoundException(show.getId(),
						Show.class.getName());
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public Show find(Connection c, Long id) throws InstanceNotFoundException
	{
		String query = "SELECT name, description, startDate, duration, limitDate, maxTickets, soldTickets"
				+ " realPrice, discountedPrice, salesCommission, creationDate FROM Show WHERE id = ?";

		try (PreparedStatement preparedStatement = c.prepareStatement(query))
		{
			int index = 1;
			preparedStatement.setLong(index, id.longValue());
			ResultSet resultSet = preparedStatement.executeQuery();

			if (!resultSet.next()) {
				throw new InstanceNotFoundException(id,
						Show.class.getName());
			}

			index = 1;
			Show s = new Show();

			s.setName(resultSet.getString(index++));
			s.setDescription(resultSet.getString(index++));
			s.setStartDate(Calendar.getInstance());
			s.setDuration(resultSet.getLong(index++));
			s.setLimitDate(Calendar.getInstance());
			s.setMaxTickets(resultSet.getLong(index++));           
			s.setSoldTickets(resultSet.getLong(index++));
			s.setRealPrice(resultSet.getFloat(index++));
			s.setDiscountedPrice(resultSet.getFloat(index++));
			s.setSalesCommission(resultSet.getFloat(index++));

			return new Show();

		} catch (SQLException e) {

			throw new RuntimeException(e);

		}

	}

	@Override
	public List<Show> find(Connection c, String words, Calendar startDate, Calendar endDate)
	{
		String[] keyWords = words != null ? words.split(" ") : null;
		String query = "SELECT id, name, duration, " + " description, realPrice, startDate, endDate FROM Show";

		if (words != null && keyWords.length > 0) {
			query += " WHERE";
			for (int i = 0; i < keyWords.length; i++) {
				if (i > 0) {
					query += " AND";
				}
				query += " LOWER(title) LIKE LOWER(?)";
			}
		}
		query += " ORDER BY name";

		try (PreparedStatement preparedStatement = c.prepareStatement(query)) {

			if (words != null) {

				for (int i = 0; i < keyWords.length; i++) {
					preparedStatement.setString(i + 1, "%" + keyWords[i] + "%");
				}
			}

			ResultSet resultSet = preparedStatement.executeQuery();
			List<Show> shows = new ArrayList<Show>();

			while (resultSet.next()) {

				int i = 1;
				Long showId = new Long(resultSet.getLong(i++));
				String title = resultSet.getString(i++);
				short runtime = resultSet.getShort(i++);
				String description = resultSet.getString(i++);
				float price = resultSet.getFloat(i++);
				Calendar creationDate = Calendar.getInstance();
				creationDate.setTime(resultSet.getTimestamp(i++));

				shows.add(new Show());

			}

			return shows;

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

}
