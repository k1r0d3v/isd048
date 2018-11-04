package es.udc.ws.app.model.show;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.Connection;
import java.util.Calendar;
import java.util.List;


public interface SqlShowDao
{
    public Show create(Connection c, Show show);

    public void update(Connection c, Show show) throws InstanceNotFoundException;

    public void remove(Connection c, Long id) throws InstanceNotFoundException;

    public Show find(Connection c, Long id) throws InstanceNotFoundException;

    public List<Show> find(Connection c, String words, Calendar startDate, Calendar endDate);
}
