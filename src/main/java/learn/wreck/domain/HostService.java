package learn.wreck.domain;

import learn.wreck.data.HostFileRepository;
import learn.wreck.data.HostRepository;
import learn.wreck.models.Host;

import java.util.List;
import java.util.stream.Collectors;


public class HostService {

    private final HostRepository repository;

    public HostService(HostRepository repository) {
        this.repository = repository;
    }

    public List<Host> findByEmail(String email) {
        return repository.findAll()
                .stream()
                .filter(i -> i.getEmail() == email)
                .collect(Collectors.toList());
    }
}
