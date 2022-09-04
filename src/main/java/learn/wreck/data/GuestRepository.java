package learn.wreck.data;

import learn.wreck.models.Guest;

import java.util.List;

public interface GuestRepository {
    List<Guest> findAll();

    Guest findById(int id);

    Guest findByEmail(String email);
}
