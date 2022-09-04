package learn.wreck.data;

import learn.wreck.models.Host;

import java.util.List;

public interface HostRepository {
    List<Host> findAll();

    Host findById(String id);

    Host findByEmail(String email);
}
