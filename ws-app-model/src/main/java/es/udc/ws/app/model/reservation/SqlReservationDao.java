package es.udc.ws.app.model.reservation;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.Connection;
import java.util.List;

public interface SqlReservationDao
{
    Reservation create(Connection c, Reservation reservation);

    void update(Connection c, Reservation reservation) throws InstanceNotFoundException;

    void remove(Connection c, Long id) throws InstanceNotFoundException;
    
    List<Reservation> findByEmail(Connection c, String email);

    Reservation findByCode(Connection c, String code) throws InstanceNotFoundException;
}
