package learn.wreck.data;

import learn.wreck.models.Host;

import java.math.BigDecimal;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HostRepositoryDouble implements HostRepository {

    public final static Host HOST = makeHost();

    private final ArrayList<Host> hosts = new ArrayList<>();

    public HostRepositoryDouble() {
        hosts.add(HOST);
    }

    @Override
    public List<Host> findAll() {
        return hosts;
    }

    @Override
    public Host findById(String id) {
        return hosts.stream()
                .filter(i -> i.getId()
                        .equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Host findByEmail(String email) {
        return hosts.stream()
                .filter(i -> i.getEmail()
                        .equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    private static Host makeHost() {
        Host host = new Host();
        host.setId("3edda6bc-ab95-49a8-8962-d50b53f84b15");
        host.setLastName("Yearnes");
        host.setEmail("eyearnes0@sfgate.com");
        host.setPhoneNumber("(806) 1783815");
        host.setAddress("3 Nova Trail");
        host.setCity("Armarillo");
        host.setState("TX");
        host.setPostalCode(79182);
        host.setStandardRate(new BigDecimal(340));
        host.setWeekendRate(new BigDecimal(425));
        return host;
    }
}
