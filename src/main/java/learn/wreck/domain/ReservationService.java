package learn.wreck.domain;

import learn.wreck.data.DataException;
import learn.wreck.data.ReservationRepository;
import learn.wreck.data.HostRepository;
import learn.wreck.data.GuestRepository;
import learn.wreck.models.Reservation;
import learn.wreck.models.Host;
import learn.wreck.models.Guest;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final HostRepository hostRepository;
    private final GuestRepository guestRepository;

    public ReservationService(ReservationRepository reservationRepository, HostRepository hostRepository, GuestRepository guestRepository){
        this.reservationRepository = reservationRepository;
        this.hostRepository=hostRepository;
        this.guestRepository=guestRepository;
    }

    public List<Reservation> findByHost(Host host){
        Map<String, Host> hostMap = hostRepository.findAll().stream().collect(Collectors.toMap(i->i.getId(),i->i));
        Map<Integer,Guest> guestMap = guestRepository.findAll().stream().collect(Collectors.toMap(i->i.getId(),i->i));

        List<Reservation> result = reservationRepository.findByHost(host);
        for (Reservation reservation: result){
            reservation.setHost(hostMap.get(reservation.getHost().getId()));
            reservation.setGuest(guestMap.get(reservation.getGuest().getId()));
        }
        return result;
    }

    public Result<Reservation> add(Reservation reservation) throws DataException{
        Result<Reservation> result=validate(reservation);
        if(!result.isSuccess()){
            return result;
        }

        result.setPayload(reservationRepository.add(reservation));
        return result;
    }
}
