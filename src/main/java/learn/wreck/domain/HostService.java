package learn.wreck.domain;

import learn.wreck.data.HostFileRepository;
import learn.wreck.data.HostRepository;
import learn.wreck.models.Host;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HostService {

    private final HostRepository repository;

    public HostService(HostRepository repository) {
        this.repository = repository;
    }

    public List<Host> findByEmail(String email) {
        return repository.findAll()
                .stream()
                .filter(i -> i.getEmail().equalsIgnoreCase(email))
                .collect(Collectors.toList());
    }
}
