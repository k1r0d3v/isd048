package es.udc.ws.app.model.reservation;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.Connection;
import java.util.List;

public interface SqlReservationDao
{
    public Reservation create(Connection c, Reservation reservation);

    public void update(Connection c, Reservation reservation) throws InstanceNotFoundException;

    public List<Reservation> findByEmail(Connection c, String email);

    public Reservation findByCode(Connection c, String code) throws InstanceNotFoundException;
}
