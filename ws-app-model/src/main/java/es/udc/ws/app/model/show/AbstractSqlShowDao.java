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
	public void update(Connection c, Show show)
            throws InstanceNotFoundException
	{
		String query = "UPDATE ShowTable SET " +
				"name = ?, " +
				"description = ?, " +
				"startDate = ?, " +
				"duration = ?, " +
				"limitDate = ?, " +
				"maxTickets = ?, " +
				"soldTickets = ?, " +
				"realPrice = ?, " +
				"discountedPrice = ?, " +
                "salesCommission = ?" +
				"WHERE id = ?";


		try (PreparedStatement preparedStatement = c.prepareStatement(query)) {

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

            preparedStatement.setLong(index, show.getId());

			int updatedRows = preparedStatement.executeUpdate();

			if (updatedRows == 0)
				throw new InstanceNotFoundException(show.getId(),
						Show.class.getName());

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

    @Override
    public void remove(Connection c, Long id)
            throws InstanceNotFoundException {
        String query = "DELETE FROM ShowTable WHERE id = ?";

        try (PreparedStatement preparedStatement = c.prepareStatement(query))
        {
            preparedStatement.setLong(1, id);

            if (preparedStatement.executeUpdate() == 0)
                throw new InstanceNotFoundException(id, Show.class.getName());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
	public Show find(Connection c, Long id)
            throws InstanceNotFoundException
	{
		String query = "SELECT " +
                "id, " +
                "name, " +
                "description, " +
                "startDate, " +
                "duration, " +
                "limitDate, " +
                "maxTickets, " +
                "soldTickets, " +
                "realPrice, " +
                "discountedPrice, " +
                "salesCommission " +
                "FROM ShowTable " +
                "WHERE id = ?";

		try (PreparedStatement preparedStatement = c.prepareStatement(query))
		{
			int index = 1;
			preparedStatement.setLong(index, id);
			ResultSet rs = preparedStatement.executeQuery();

			if (!rs.next())
				throw new InstanceNotFoundException(id, Show.class.getName());

			index = 1;
			Show s = new Show();


            s.setId(rs.getLong(index++));
			s.setName(rs.getString(index++));
			s.setDescription(rs.getString(index++));

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(rs.getTimestamp(index++).getTime());
			s.setStartDate(calendar);

			s.setDuration(rs.getLong(index++));

            calendar = Calendar.getInstance();
            calendar.setTimeInMillis(rs.getTimestamp(index++).getTime());
			s.setLimitDate(calendar);

			s.setMaxTickets(rs.getLong(index++));
			s.setSoldTickets(rs.getLong(index++));
			s.setRealPrice(rs.getFloat(index++));
			s.setDiscountedPrice(rs.getFloat(index++));
			s.setSalesCommission(rs.getFloat(index));

			return s;

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<Show> find(Connection c, String words, Calendar startDate, Calendar endDate)
	{
		String[] keywords = words.split(" ");

		String query = "SELECT " +
                "id, " +
                "name, " +
                "description, " +
                "startDate, " +
                "duration, " +
                "limitDate, " +
                "maxTickets, " +
                "soldTickets, " +
                "realPrice, " +
                "discountedPrice, " +
                "salesCommission " +
                "FROM ShowTable";

		if (keywords.length > 0)
		{
		    StringBuilder tmp = new StringBuilder();
		    tmp.append(" WHERE");

			for (int i = 0; i < (keywords.length - 1); i++)
				tmp.append(" LOWER(title) LIKE LOWER(?) AND");

            tmp.append(" LOWER(title) LIKE LOWER(?)");
            query += tmp.toString();
        }
		query += " ORDER BY name";

		try (PreparedStatement preparedStatement = c.prepareStatement(query)) {

            for (int i = 0; i < keywords.length; i++)
                preparedStatement.setString(i + 1, String.format("%%%s%%", keywords[i]));

			ResultSet rs = preparedStatement.executeQuery();
			List<Show> shows = new ArrayList<>();

			while (rs.next())
			{
				int index = 1;
                Show s = new Show();

                s.setId(rs.getLong(index++));
                s.setName(rs.getString(index++));
                s.setDescription(rs.getString(index++));

				Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(rs.getTimestamp(index++).getTime());
                s.setStartDate(calendar);

                s.setDuration(rs.getLong(index++));

				calendar = Calendar.getInstance();
                calendar.setTimeInMillis(rs.getTimestamp(index++).getTime());
                s.setLimitDate(calendar);

                s.setMaxTickets(rs.getLong(index++));
                s.setSoldTickets(rs.getLong(index++));
                s.setRealPrice(rs.getFloat(index++));
                s.setDiscountedPrice(rs.getFloat(index++));
                s.setSalesCommission(rs.getFloat(index));

				shows.add(s);
			}

			return shows;

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
