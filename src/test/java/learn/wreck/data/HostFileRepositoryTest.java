package learn.wreck.data;

import learn.wreck.models.Host;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HostFileRepositoryTest {
    @Test
    void shouldFindAll() {
        HostFileRepository repository = new HostFileRepository("./data/hosts.csv");
        List<Host> all=repository.findAll();
        assertEquals(1000,all.size());
    }

    @Test
    void shouldFindById() {
        HostFileRepository repository = new HostFileRepository("./data/hosts.csv");
        Host host=repository.findById("3edda6bc-ab95-49a8-8962-d50b53f84b15");
        assertEquals("Yearnes", host.getLastName());

    }

    @Test
    void shouldFindByEmail() {
        HostFileRepository repository = new HostFileRepository("./data/hosts.csv");
        Host host = repository.findByEmail("eyearnes0@sfgate.com");
        assertEquals("Yearnes",host.getLastName());
    }
}
