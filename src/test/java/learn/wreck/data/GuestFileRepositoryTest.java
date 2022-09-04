package learn.wreck.data;

import learn.wreck.models.Guest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class GuestFileRepositoryTest {

    @Test
    void shouldFindAll() {
        GuestFileRepository repository = new GuestFileRepository("./data/guests.csv");
        List<Guest> all = repository.findAll();
        assertEquals(1000, all.size());
    }

    @Test
    void shouldFindById() {
        GuestFileRepository repository = new GuestFileRepository("./data/guests.csv");
        Guest guest = repository.findById(1);
        assertEquals("Sullivan", guest.getFirstName());
    }
}
