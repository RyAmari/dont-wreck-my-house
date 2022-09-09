package learn.wreck.data;

import learn.wreck.models.Guest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class GuestRepositoryDouble implements GuestRepository {

    public final static Guest GUEST  = makeGuest();

    private final ArrayList<Guest> guests = new ArrayList<>();

    public GuestRepositoryDouble() {
        guests.add(GUEST);
    }

    @Override
    public List<Guest> findAll() {
        return guests;
    }

    @Override
    public Guest findById(int id) {
        return guests.stream().filter(i->i.getId()==id).findFirst().orElse(null);
    }


    @Override
    public Guest findByEmail(String email) {
        return guests.stream()
                .filter(i -> i.getEmail()
                        .equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    private static Guest makeGuest() {
        Guest guest = new Guest();
        guest.setId(1);
        guest.setFirstName("Sullivan");
        guest.setLastName("Lomas");
        guest.setEmail("slomas0@mediafire.com");
        guest.setState("NV");
        return guest;
    }
}
