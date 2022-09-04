package learn.wreck.ui;

import learn.wreck.data.DataException;
import learn.wreck.domain.ReservationService;
import learn.wreck.domain.HostService;
import learn.wreck.domain.GuestService;
import learn.wreck.domain.Result;
import learn.wreck.models.Reservation;
import learn.wreck.models.Host;
import learn.wreck.models.Guest;

import java.time.LocalDate;
import java.util.List;

public class Controller {
    private final HostService hostService;
    private final ReservationService reservationService;
    private final GuestService guestService;
    private final View view;

    public Controller(HostService hostService, ReservationService reservationService, GuestService guestService, View view) {
        this.hostService = hostService;
        this.reservationService = reservationService;
        this.guestService = guestService;
        this.view = view;
    }

    public void run() {
        view.displayHeader("Welcome to Sustainable Foraging");
        try {
            runAppLoop();
        } catch (DataException ex) {
            view.displayException(ex);
        }
        view.displayHeader("Goodbye.");
    }

    private void runAppLoop() throws DataException {
        MainMenuOption option;
        do {
            option = view.selectMainMenuOption();
            switch (option) {
                case VIEW_RESERVATIONS_HOST:
                    viewReservationsByHostEmail();
                    break;
                case MAKE_RESERVATION:
                    makeReservation();
                    break;
                case EDIT_RESERVATION:
                    editReservation();
                    break;
                case CANCEL_RESERVATION:
                    cancelReservation();
                    break;
            }
        } while (option != MainMenuOption.EXIT);
    }

    private void viewReservationsByHostEmail() {
        String hostEmail = view.getReservationHost();
        List<Host> hosts = hostService.findByEmail(hostEmail);
        List<Reservation> reservations = reservationService.findByHost(hosts.get(0));
        view.displayReservations(reservations);
        view.enterToContinue();
    }

    private void makeReservation() throws DataException {
        view.displayHeader(MainMenuOption.MAKE_RESERVATION.getMessage());
        Host host = getHost();
        if (host == null) {
            return;
        }
        Guest guest = getGuest();
        if (guest == null) {
            return;
        }
        Reservation reservation = view.makeReservation(host, guest);
        Result<Reservation> result = reservationService.add(reservation);
        if (!result.isSuccess()) {
            view.displayStatus(false, result.getErrorMessages());
        } else {
            String successMessage = String.format("Reservation %s created.", result.getPayload().getId());
            view.displayStatus(true, successMessage);
        }
    }

    private void editReservation(){

    }

    private void cancelReservation(){

    }

    private Host getHost() {
        String hostEmail = view.getReservationHost();
        List<Host> hosts = hostService.findByEmail(hostEmail);
        return view.selectHost(hosts);
    }
    private Guest getGuest() {
        String guestEmail = view.getReservationGuest();
        List<Guest> guests = guestService.findByEmail(guestEmail);
        return view.selectGuest(guests);
    }
}
