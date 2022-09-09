package learn.wreck.models;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

public class Reservation {

    private int id;
    private LocalDate startDate;
    private LocalDate endDate;
    private Guest guest;
    private Host host;
    private BigDecimal total;

    public Reservation(){

    }
    public Reservation(LocalDate startDate, LocalDate endDate){
        this.startDate=startDate;
        this.endDate=endDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }

    public BigDecimal getTotal(Reservation reservation) {
        //need to iterate between the specific days between
        // the start date and the end date, if they land on
        // sat or sunday, multiply host-determined weekend rate by 1 or two
        //depending on if one or both weekend days are used
        // then add that to the total of the host-determined standard rate
        if(this.total!=null){
            return total;
        }
        List<LocalDate> weekDayDates = new ArrayList<>();
        List<LocalDate> weekendDayDates = new ArrayList<>();
        LocalDate startDate = reservation.getStartDate();
        LocalDate endDate = reservation.getEndDate();
        Set<DayOfWeek> weekend = EnumSet.of(DayOfWeek.FRIDAY, DayOfWeek.SATURDAY);
        while (startDate.isBefore(endDate)) {
            if (!weekend.contains(startDate.getDayOfWeek())) {
                weekDayDates.add(startDate);
            } else {
                weekendDayDates.add(startDate);
            }
            startDate = startDate.plusDays(1);
        }
        if (reservation.getHost()==null || reservation.getHost().getStandardRate()==null){
            return BigDecimal.ZERO;
        }
        BigDecimal standardRateTotal = reservation.getHost()
                .getStandardRate()
                .multiply(BigDecimal.valueOf(weekDayDates.size()));
        BigDecimal weekendRateTotal = reservation.getHost()
                .getWeekendRate()
                .multiply(BigDecimal.valueOf(weekendDayDates.size()));
         this.total = standardRateTotal.add(weekendRateTotal);
        return this.total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(startDate, that.startDate) && Objects.equals(endDate, that.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDate, endDate);
    }

    @Override
    public String  toString() {
        return "Reservation{" +
                "id=" + id +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", guest=" + guest +
                ", host=" + host +
                ", total=" + total +
                '}';
    }
}
