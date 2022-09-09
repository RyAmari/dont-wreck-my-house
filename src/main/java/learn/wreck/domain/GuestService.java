package learn.wreck.domain;

import learn.wreck.data.DataException;
import learn.wreck.data.GuestRepository;
import learn.wreck.models.Guest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class GuestService {

    private final GuestRepository repository;

    public GuestService(GuestRepository repository){this.repository=repository;}

    public List<Guest> findByEmail(String email){
        return repository.findAll().stream().filter(i->i.getEmail().equalsIgnoreCase(email)).collect(Collectors.toList());
    }

    public List<Guest> findAll(){
        return repository.findAll();
    }


}
