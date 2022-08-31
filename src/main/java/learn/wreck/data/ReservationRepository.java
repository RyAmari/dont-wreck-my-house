package learn.wreck.data;

import learn.wreck.models.Host;
import learn.wreck.models.Reservation;

import java.util.List;

public interface ReservationRepository {
    List<Reservation> findByHost(Host host);

    boolean edit(Reservation reservation) throws DataException;
}
