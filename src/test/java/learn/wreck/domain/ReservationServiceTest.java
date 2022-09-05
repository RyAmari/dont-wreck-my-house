package learn.wreck.domain;

import learn.wreck.data.*;
import learn.wreck.models.Host;
import learn.wreck.models.Reservation;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class ReservationServiceTest {

    ReservationService service = new ReservationService(
            new ReservationRepositoryDouble(),
            new HostRepositoryDouble(),
            new GuestRepositoryDouble());
    List<Reservation> noExistingReservations = new ArrayList<>();
    LocalDate existingReservationStartDate = LocalDate.now().plusMonths(1);
    LocalDate existingReservationEndDate = LocalDate.now().plusMonths(1).plusWeeks(1);
    List<Reservation> existingReservations = Arrays.asList(
            new Reservation(existingReservationStartDate,existingReservationEndDate));

    @Test
    void reservationCannotBeNull() {
        Reservation reservation = null;
        Result<Reservation> result = service.validateReservationDates(reservation, noExistingReservations);
        assertFalse(result.isSuccess());
        assertEquals(1,result.getErrorMessages().size());
        assertTrue(result.getErrorMessages().contains("Reservation cannot be null"));

    }
    @Test
    void reservationStartDateCannotBeNull() {
        Reservation reservation = new Reservation();
        reservation.setStartDate(null);
        reservation.setEndDate(LocalDate.now().plusDays(1));
        Result<Reservation> result = service.validateReservationDates(reservation, noExistingReservations);
        assertFalse(result.isSuccess());
        assertEquals(1,result.getErrorMessages().size());
        assertTrue(result.getErrorMessages().contains("Start date cannot be null"));

    }

    @Test
    void reservationStartDateAndEndDateCannotBeNull() {
        Reservation reservation = new Reservation();
        reservation.setStartDate(null);
        reservation.setEndDate(null);
        Result<Reservation> result = service.validateReservationDates(reservation, noExistingReservations);
        assertFalse(result.isSuccess());
        assertEquals(2,result.getErrorMessages().size());
        assertTrue(result.getErrorMessages().contains("Start date cannot be null"));
        assertTrue(result.getErrorMessages().contains("End date cannot be null"));

    }

    @Test
    void reservationEndDateCannotBeNull() {
        Reservation reservation = new Reservation();
        reservation.setStartDate(LocalDate.now().plusDays(1));
        reservation.setEndDate(null);
        Result<Reservation> result = service.validateReservationDates(reservation, noExistingReservations);
        assertFalse(result.isSuccess());
        assertEquals(1,result.getErrorMessages().size());
        assertTrue(result.getErrorMessages().contains("End date cannot be null"));
    }

    @Test
    void reservationStartDateCannotBeYesterday() {
        Reservation reservation = new Reservation();
        reservation.setStartDate(LocalDate.now().minusDays(1));
        reservation.setEndDate(LocalDate.now().plusDays(1));
        Result<Reservation> result = service.validateReservationDates(reservation, noExistingReservations);
        assertFalse(result.isSuccess());
        assertEquals(1,result.getErrorMessages().size());
        assertTrue(result.getErrorMessages().contains("Start date cannot be in the past."));
    }

    @Test
    void reservationStartDateCanBeToday() {
        Reservation reservation = new Reservation();
        reservation.setStartDate(LocalDate.now());
        reservation.setEndDate(LocalDate.now().plusDays(1));
        Result<Reservation> result = service.validateReservationDates(reservation, noExistingReservations);
        assertTrue(result.isSuccess());
    }

    @Test
    void reservationStartDateCanBeTomorrow() {
        Reservation reservation = new Reservation();
        reservation.setStartDate(LocalDate.now().plusDays(1));
        reservation.setEndDate(LocalDate.now().plusDays(2));
        Result<Reservation> result = service.validateReservationDates(reservation, noExistingReservations);
        assertTrue(result.isSuccess());
    }
    @Test
    void reservationStartDateCannotBeAfterEndDate(){
        Reservation reservation = new Reservation();
        reservation.setStartDate(LocalDate.now().plusDays(2));
        reservation.setEndDate(LocalDate.now().plusDays(1));
        Result<Reservation> result = service.validateReservationDates(reservation, noExistingReservations);
        assertFalse(result.isSuccess());
        assertEquals(1,result.getErrorMessages().size());
        assertEquals("Start date cannot be after end date.", result.getErrorMessages().get(0));
    }

    @Test
    void reservationStartDateCanBeOneDayBeforeEndDate() {
        Reservation reservation = new Reservation();
        reservation.setStartDate(LocalDate.now().plusDays(1));
        reservation.setEndDate(LocalDate.now().plusDays(2));
        Result<Reservation> result = service.validateReservationDates(reservation, noExistingReservations);
        assertTrue(result.isSuccess());
    }

    @Test
    void reservationStartDateCannotBeEqualToEndDate(){
        Reservation reservation = new Reservation();
        reservation.setStartDate(LocalDate.now().plusDays(1));
        reservation.setEndDate(LocalDate.now().plusDays(1));
        Result<Reservation> result = service.validateReservationDates(reservation, noExistingReservations);
        assertFalse(result.isSuccess());
        assertEquals(1,result.getErrorMessages().size());
        assertEquals("Start date cannot be equal to end date.", result.getErrorMessages().get(0));
    }
    @Test
    void newReservationCanExistWhollyBeforeExistingReservation() {
        Reservation reservation = new Reservation();
        reservation.setEndDate(existingReservationStartDate.minusDays(1));
        reservation.setStartDate(reservation.getEndDate().minusDays(1));
        Result<Reservation> result = service.validateReservationDates(reservation, existingReservations);
        assertTrue(result.isSuccess());
    }

    @Test
    void reservationCanExistWhollyAfterExistingReservation() {
        Reservation reservation = new Reservation();
        reservation.setStartDate(existingReservationEndDate.plusDays(1));
        reservation.setEndDate(reservation.getStartDate().plusDays(1));
        Result<Reservation> result = service.validateReservationDates(reservation, existingReservations);
        assertTrue(result.isSuccess());
    }

    @Test
    void reservationCanStartOnSameDayExistingReservationEnds() {
        Reservation reservation = new Reservation();
        reservation.setStartDate(existingReservationEndDate);
        reservation.setEndDate(existingReservationEndDate.plusDays(1));
        Result<Reservation> result = service.validateReservationDates(reservation, existingReservations);
        assertTrue(result.isSuccess());
    }
    @Test
    void reservationShouldCalculateTotal() {
        Reservation reservation = new Reservation();
        Host host = new Host();
        host.setStandardRate(new BigDecimal(460));
        host.setWeekendRate(new BigDecimal(500));
        reservation.setHost(host);
        reservation.setStartDate(LocalDate.now());
        reservation.setEndDate(reservation.getStartDate()
                .plusWeeks(2));
        Result<Reservation> result = service.validateReservationDates(reservation, existingReservations);
        assertEquals(new BigDecimal(6600), reservation.getTotal(reservation));
        assertTrue(result.isSuccess());
    }

    @Test
    void reservationCanEndOnSameDayExistingReservationStarts() {
        fail("Not implemented");
    }

    @Test
    void reservationCannotExistInExistingReservationEnds() {
        fail("Not implemented");
    }

    @Test
    void reservationCannotEndAfterExistingStartDate() {
        fail("Not implemented");
    }

    @Test
    void reservationCannotWhollyContainExistingReservation() {
        fail("Not implemented");
    }
}
