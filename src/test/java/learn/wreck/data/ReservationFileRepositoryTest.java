package learn.wreck.data;

import com.sun.source.tree.AssertTree;
import learn.wreck.models.Guest;
import learn.wreck.models.Reservation;
import learn.wreck.models.Host;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReservationFileRepositoryTest {
    static final String SEED_FILE_PATH = "./data/reservations-testdir/reservations-seed.csv";
    static final String TEST_FILE_PATH = "./data/reservations-testdir/2e72f86c-b8fe-4265-b4f1-304dea8762db.csv";
    static final String DIR_FILE_PATH= "./data/reservations-testdir";

    final String hostId= "2e72f86c-b8fe-4265-b4f1-304dea8762db";

    ReservationFileRepository repository = new ReservationFileRepository(DIR_FILE_PATH);

    @BeforeEach
    void setup() throws IOException {
        Path seedPath = Paths.get(SEED_FILE_PATH);
        Path testPath = Paths.get(TEST_FILE_PATH);
        Files.copy(seedPath, testPath, StandardCopyOption.REPLACE_EXISTING);
    }


    @Test
    void shouldFindByHost() {
        Host host =  new Host();
        host.setId(hostId);
        List<Reservation> reservations = repository.findByHost(host);
        assertEquals(12, reservations.size());
    }
//    @Test
//    void shouldFindHostStandardRate() {
//        Host host = new Host();
//        host.setId(hostId);
//        assertEquals("NY", host.getState());
//    }

    @Test
    void shouldAdd() throws DataException {
        Reservation reservation = new Reservation();
        reservation.setId(13);
        reservation.setStartDate(LocalDate.of(2023,01,14));
        reservation.setEndDate(LocalDate.of(2023,01,21));
        reservation.setTotal(new BigDecimal(600));
        Guest guest = new Guest();
        guest.setFirstName("Sullivan");
        guest.setLastName("Lomas");
        guest.setEmail("slomas0@mediafire.com");
        guest.setPhoneNumber("(702) 7768761");
        guest.setState("NV");
        guest.setId(702);
        reservation.setGuest(guest);
        Host host = new Host();
        host.setId(hostId);
        reservation.setHost(host);
        repository.add(reservation);
        assertEquals(13,reservation.getId());
    }

    @Test
    void shouldEdit() throws DataException {
        Reservation reservation = new Reservation();
        reservation.setId(12);
        reservation.setStartDate(LocalDate.of(2024,01,14));
        reservation.setEndDate(LocalDate.of(2024,01,21));
        reservation.setTotal(new BigDecimal(600));
        Guest guest = new Guest();
        guest.setFirstName("Sullivan");
        guest.setLastName("Lomas");
        guest.setEmail("slomas0@mediafire.com");
        guest.setPhoneNumber("(702) 7768761");
        guest.setState("NV");
        guest.setId(702);
        reservation.setGuest(guest);
        Host host = new Host();
        host.setId(hostId);
        reservation.setHost(host);

        boolean success = repository.edit(reservation);
        assertTrue(success);

        Reservation actual = repository.findByHost(host).get(13);
        assertNotNull(actual);
        assertEquals("01/14/2024",actual.getStartDate());
        assertEquals("01/21/2024",actual.getEndDate());

    }
    @Test
    void shouldNotEditNonExistingReservation() throws DataException {
        Reservation reservation = new Reservation();
        reservation.setId(400);
        Host host = new Host();
        host.setId(hostId);
        reservation.setHost(host);
        boolean actual = repository.edit(reservation);
        assertFalse(actual);
    }
}

