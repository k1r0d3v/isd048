package es.udc.ws.app.model.reservation;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.Connection;
import java.util.List;

public interface SqlReservationDao
{
    public Reservation create(Connection c, Reservation reservation);

    public void update(Connection c, Reservation reservation) throws InstanceNotFoundException;

    public List<Reservation> find(Connection c, String email);

    public Reservation find (Connection c, long code) throws InstanceNotFoundException;
}
