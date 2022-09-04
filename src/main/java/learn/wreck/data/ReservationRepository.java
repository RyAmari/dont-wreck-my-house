package learn.wreck.data;

import learn.wreck.models.Host;
import learn.wreck.models.Reservation;

import java.util.List;

public interface ReservationRepository {
    List<Reservation> findByHost(Host host);

    Reservation add(Reservation reservation) throws DataException;

    boolean edit(Reservation reservation) throws DataException;

    boolean cancel(Reservation reservation) throws DataAccessException, DataException;
}
