package learn.wreck.ui;

import learn.wreck.models.Guest;
import learn.wreck.models.Reservation;
import learn.wreck.models.Host;
import learn.wreck.domain.Result;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
@Component
public class View {
    private final ConsoleIO io;

    public View(ConsoleIO io) {
        this.io = io;
    }

    public MainMenuOption selectMainMenuOption() {
        displayHeader("Main Menu");
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (MainMenuOption option : MainMenuOption.values()) {
            if (!option.isHidden()) {
                io.printf("%s. %s%n", option.getValue(), option.getMessage());
            }
            min = Math.min(min, option.getValue());
            max = Math.max(max, option.getValue());
        }

        String message = String.format("Select [%s-%s]: ", min, max);
        return MainMenuOption.fromValue(io.readInt(message, min, max));
    }
    public String getReservationHost() {
        displayHeader(MainMenuOption.VIEW_RESERVATIONS_HOST.getMessage());
        return io.readRequiredString("Select a host by email: ");
    }
    public String getReservationGuest() {
        return io.readRequiredString("Select a guest by email: ");
    }

    public Host selectHost(List<Host> hosts) {
        if (hosts.size() == 0) {
            io.println("No hosts found");
            return null;
        }

        int index = 1;
        for (Host host : hosts.stream().limit(25).collect(Collectors.toList())) {
            io.printf("%s: %s %s%n", index++, host.getLastName(), host.getEmail());
        }
        index--;

        if (hosts.size() > 5) {
            io.println("More than 5 hosts found. Showing first 25. Please refine your search.");
        }
        io.println("0: Exit");
        String message = String.format("Select a host by their index [1-%s]: ", index);

        index = io.readInt(message, 0, index);
        if (index <= 0) {
            return null;
        }
        return hosts.get(index - 1);
    }

    public Guest selectGuest(List<Guest> guests) {
        if (guests.size() == 0) {
            io.println("No guests found");
            return null;
        }

        int index = 1;
        for (Guest guest : guests.stream().limit(25).collect(Collectors.toList())) {
            io.printf("%s: %s %s%n", index++, guest.getLastName(), guest.getEmail());
        }
        index--;

        if (guests.size() > 5) {
            io.println("More than 5 hosts found. Showing first 5. Please refine your search.");
        }
        io.println("0: Exit");
        String message = String.format("Select a guest by their index [1-%s]: ", index);

        index = io.readInt(message, 0, index);
        if (index <= 0) {
            return null;
        }
        return guests.get(index - 1);
    }

    public Reservation makeReservation(Host host, Guest guest) {
        Reservation reservation = new Reservation();
        reservation.setHost(host);
        reservation.setGuest(guest);
        reservation.setStartDate(io.readLocalDate("Start date [MM/dd/yyyy]: "));
        reservation.setEndDate(io.readLocalDate("End date [MM/dd/yyyy]: "));
        reservation.setTotal(reservation.getTotal(reservation));
        return reservation;
    }

    public Reservation findReservation(List<Reservation> reservations){
        displayReservations(reservations);
        if(reservations.size()==0){
            return null;
        }
        int reservationID = io.readInt("Reservation ID: ");
        for(Reservation reservation: reservations){
            if(reservation.getId()==reservationID){
                return reservation;
            }
        }
        System.out.println("Reservation ID " +reservationID + " not found.");
        return null;
    }

    public Reservation updateReservation(List<Reservation> reservations){
        Reservation reservation = findReservation(reservations);
        if(reservation!= null){
            update(reservation);
        }
        return reservation;
    }

    private Reservation update(Reservation reservation){
        LocalDate startDate = io.readLocalDate("Start date [MM/dd/yyyy]: ");
        if(!startDate.isBefore(LocalDate.now())){
            reservation.setStartDate(startDate);
        }
        LocalDate endDate = io.readLocalDate("End date [MM/dd/yyyy]: ");
        if(!endDate.isBefore(startDate)){
            reservation.setEndDate(endDate);
        }
        reservation.setTotal(reservation.getTotal(reservation));
        return reservation;
    }
    public Reservation cancelReservation(List<Reservation> reservations){
        Reservation reservation = findReservationForCancel(reservations);
        if(reservation!= null){
            cancel(reservation);
        }
        return reservation;
    }
    private Reservation cancel(Reservation reservation){
        return reservation;
    }

    public void enterToContinue() {
        io.readString("Press [Enter] to continue.");
    }

    public void displayHeader(String message) {
        io.println("");
        io.println(message);
        io.println("=".repeat(message.length()));
    }

    public void displayException(Exception ex) {
        displayHeader("A critical error occurred:");
        io.println(ex.getMessage());
    }

    public void displayStatus(boolean success, String message) {
        displayStatus(success, List.of(message));
    }

    public void displayStatus(boolean success, List<String> messages) {
        displayHeader(success ? "Success" : "Error");
        for (String message : messages) {
            io.println(message);
        }
    }

    public void displayReservations(List<Reservation> reservations) {
        if (reservations == null || reservations.isEmpty()) {
            io.println("No reservations found.");
            return;
        }
        for (Reservation reservation : reservations) {
            io.printf("%s, %s - %s, Guest: %s, %s, Email: %s%n",
                    reservation.getId(),
                    reservation.getStartDate(),
                    reservation.getEndDate(),
                    reservation.getGuest()
                            .getFirstName(),
                    reservation.getGuest()
                            .getLastName(),
                    reservation.getGuest()
                            .getEmail());
        }
    }
    public Reservation findReservationForCancel(List<Reservation> reservations){
        displayReservationsForCancel(reservations);
        if(reservations.size()==0){
            return null;
        }
        int reservationID = io.readInt("Reservation ID: ");
        for(Reservation reservation: reservations){
            if(reservation.getId()==reservationID){
                return reservation;
            }
        }
        System.out.println("Reservation ID " +reservationID + " not found.");
        return null;
    }
    public void displayReservationsForCancel(List<Reservation> reservations) {
        if (reservations == null || reservations.isEmpty()) {
            io.println("No reservations found.");
            return;
        }
        for (Reservation reservation : reservations) {
            if (!reservation.getEndDate()
                    .isBefore(LocalDate.now())) {
                io.printf("%s, %s - %s, Guest: %s, %s, Email: %s%n",
                        reservation.getId(),
                        reservation.getStartDate(),
                        reservation.getEndDate(),
                        reservation.getGuest()
                                .getFirstName(),
                        reservation.getGuest()
                                .getLastName(),
                        reservation.getGuest()
                                .getEmail());
            }
        }
    }
}
