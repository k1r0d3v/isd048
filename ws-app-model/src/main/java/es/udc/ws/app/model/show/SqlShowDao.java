package es.udc.ws.app.model.show;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.Connection;
import java.util.Calendar;
import java.util.List;


public interface SqlShowDao
{
    Show create(Connection c, Show show);

    void update(Connection c, Show show) throws InstanceNotFoundException;

    void remove(Connection c, Long id) throws InstanceNotFoundException;

    Show find(Connection c, Long id) throws InstanceNotFoundException;

    List<Show> find(Connection c, String words, Calendar startDate, Calendar endDate);
}
