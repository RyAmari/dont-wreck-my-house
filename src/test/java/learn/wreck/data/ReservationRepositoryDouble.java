package learn.wreck.data;

import learn.wreck.models.Host;
import learn.wreck.models.Reservation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReservationRepositoryDouble implements ReservationRepository {

    private final ArrayList<Reservation> reservations = new ArrayList<>();

    public ReservationRepositoryDouble(){
        Reservation reservation = new Reservation();
        reservation.setId(13);
        reservation.setStartDate(LocalDate.of(2023,01,14));
        reservation.setEndDate(LocalDate.of(2023,01,21));
        reservation.setTotal(new BigDecimal(600));
        reservation.setGuest(GuestRepositoryDouble.GUEST);
        reservation.setHost(HostRepositoryDouble.HOST);
        reservations.add(reservation);
    }
    @Override
    public List<Reservation> findByHost(Host host) {
        return reservations.stream().filter(i->i.getHost().equals(host)).collect(Collectors.toList());
    }

    @Override
    public Reservation add(Reservation reservation) throws DataException {
        int nextId = reservations.stream()
                .mapToInt(Reservation::getId)
                .max()
                .orElse(0) + 1;
        reservation.setId(nextId);
        reservations.add(reservation);
        return reservation;
    }

    @Override
    public boolean edit(Reservation reservation) throws DataException {
        return reservation.getId()==13;
    }

    @Override
    public boolean cancel(Reservation reservation) throws DataException, DataException {
        return reservation.getId()==13 ;
    }
}
