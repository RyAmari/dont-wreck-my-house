package learn.wreck.domain;

import learn.wreck.data.DataException;
import learn.wreck.data.ReservationRepository;
import learn.wreck.data.HostRepository;
import learn.wreck.data.GuestRepository;
import learn.wreck.models.Reservation;
import learn.wreck.models.Host;
import learn.wreck.models.Guest;
import org.springframework.stereotype.Service;


import javax.xml.crypto.Data;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.Set.*;
@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final HostRepository hostRepository;
    private final GuestRepository guestRepository;

    public ReservationService(ReservationRepository reservationRepository, HostRepository hostRepository, GuestRepository guestRepository) {
        this.reservationRepository = reservationRepository;
        this.hostRepository = hostRepository;
        this.guestRepository = guestRepository;
    }

    public List<Reservation> findByHost(Host host) {
        Map<String, Host> hostMap = hostRepository.findAll()
                .stream()
                .collect(Collectors.toMap(i -> i.getId(), i -> i));
        Map<Integer, Guest> guestMap = guestRepository.findAll()
                .stream()
                .collect(Collectors.toMap(i -> i.getId(), i -> i));

        List<Reservation> result = reservationRepository.findByHost(host);
        for (Reservation reservation : result) {
            reservation.setHost(hostMap.get(reservation.getHost()
                    .getId()));
            reservation.setGuest(guestMap.get(reservation.getGuest()
                    .getId()));
        }
        return result;
    }

    public Result<Reservation> add(Reservation reservation) throws DataException {
        Result<Reservation> result = validate(reservation);
        if (!result.isSuccess()) {
            return result;
        }

        result.setPayload(reservationRepository.add(reservation));
        return result;
    }

    private Result<Reservation> validate(Reservation reservation) {
        List<Reservation> existingReservations = reservationRepository.findByHost(reservation.getHost());
        Result<Reservation> result = validateReservationDates(reservation, existingReservations);
        if (!result.isSuccess()) {
            return result;
        }

        validateChildrenExist(reservation, result);

        return result;
    }

    Result<Reservation> validateReservationDates(Reservation newReservation, List<Reservation> existingReservations) {
        Result<Reservation> result = new Result();
        if (newReservation == null) {
            result.addErrorMessage("Reservation cannot be null");
        }
        if (!result.isSuccess()) {
            return result;
        }
        if (newReservation.getStartDate() == null) {
            result.addErrorMessage("Start date cannot be null");
        } else if (newReservation.getStartDate()
                .isBefore(LocalDate.now())) {
            result.addErrorMessage("Start date cannot be in the past.");
        }

        if (newReservation.getEndDate() == null) {
            result.addErrorMessage("End date cannot be null");
        }
        if (!result.isSuccess()) {
            return result;
        }
        if (newReservation.getStartDate()
                .isAfter(newReservation.getEndDate())) {
            result.addErrorMessage("Start date cannot be after end date.");
        }
        if (newReservation.getStartDate()
                .equals(newReservation.getEndDate())) {
            result.addErrorMessage("Start date cannot be equal to end date.");
        }
        for (Reservation existingReservation : existingReservations) {
            if (newReservation.getStartDate()
                    .isBefore(existingReservation.getStartDate())) {
                if (newReservation.getEndDate()
                        .equals(existingReservation.getStartDate()) ||
                        newReservation.getEndDate()
                                .isBefore(existingReservation.getStartDate())) {

                } else {
                    result.addErrorMessage("Reservation cannot overlap an existing reservation");
                    break;
                }
            } else if (newReservation.getStartDate()
                    .equals(existingReservation.getEndDate())
                    || newReservation.getStartDate()
                    .isAfter(existingReservation.getEndDate())) {

            } else {
                result.addErrorMessage("Reservation cannot overlap an existing reservation.");
                break;
            }

        }
        return result;
    }

    public Result update(Reservation reservation) throws DataException{
        Result result = validate(reservation);
        if(!result.isSuccess()){
            return result;
        }
        if (reservation.getId()<=0){
            result.addErrorMessage(("Reservation 'id' is a required field."));
        }
        if(result.isSuccess()){
            if(reservationRepository.edit(reservation)){
                result.setPayload(reservation);
            }
            else{
                String message = String.format("Reservation 'id' could not be found.", reservation.getId());
                result.addErrorMessage(message);
            }
        }
        return result;
    }
    public Result cancelReservationById(Reservation reservation) throws DataException {
        Result result = new Result();
        if (!reservationRepository.cancel(reservation)){
            String message = String.format("Reservation 'id' %s could not be found.", reservation.getId());
            result.addErrorMessage(message);
        }
        return result;
    }


    private void validateChildrenExist(Reservation reservation, Result<Reservation> result) {

        if (reservation.getHost()
                .getId() == null
                || hostRepository.findById(reservation.getHost()
                .getId()) == null) {
            result.addErrorMessage("Host does not exist.");
        }

        if (guestRepository.findById(reservation.getGuest()
                .getId()) == null) {
            result.addErrorMessage("Guest does not exist.");
        }
    }
}
