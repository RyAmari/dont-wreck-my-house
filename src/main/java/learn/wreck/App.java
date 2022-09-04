package learn.wreck;

import learn.wreck.data.ReservationFileRepository;
import learn.wreck.data.HostFileRepository;
import learn.wreck.data.GuestFileRepository;
import learn.wreck.domain.ReservationService;
import learn.wreck.domain.HostService;
import learn.wreck.domain.GuestService;
import learn.wreck.ui.Controller;
import learn.wreck.ui.ConsoleIO;
import learn.wreck.ui.View;

public class App {
    public static void main(String[] args) {

                ConsoleIO io = new ConsoleIO();
                View view = new View(io);

                ReservationFileRepository reservationFileRepository = new ReservationFileRepository("./data/reservations");
                HostFileRepository hostFileRepository = new HostFileRepository("./data/hosts.csv");
                GuestFileRepository guestFileRepository = new GuestFileRepository("./data/guests.csv");

                HostService hostService = new HostService(hostFileRepository);
                ReservationService reservationService = new ReservationService(reservationFileRepository, hostFileRepository, guestFileRepository);
                GuestService guestService = new GuestService(guestFileRepository);

                Controller controller = new Controller(hostService, reservationService, guestService, view);
                controller.run();
    }
}
