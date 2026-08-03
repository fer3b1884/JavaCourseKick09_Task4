package by.shved.busbooking.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.StringJoiner;

public class Trip {
    private Integer id;
    private BusRoute route;
    private Bus bus;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private BigDecimal price;
    private Integer availableSeats;

    public Trip() {
    }

    public Trip(Integer id, BusRoute route, Bus bus, LocalDateTime departureTime,
                LocalDateTime arrivalTime, BigDecimal price, Integer availableSeats) {
        this.id = id;
        this.route = route;
        this.bus = bus;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.price = price;
        this.availableSeats = availableSeats;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BusRoute getRoute() {
        return route;
    }

    public void setRoute(BusRoute route) {
        this.route = route;
    }

    public Bus getBus() {
        return bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(Integer availableSeats) {
        this.availableSeats = availableSeats;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Trip that = (Trip) obj;
        if (id != null ? !id.equals(that.id) : that.id != null) {
            return false;
        }
        if (route != null ? !route.equals(that.route) : that.route != null) {
            return false;
        }
        if (bus != null ? !bus.equals(that.bus) : that.bus != null) {
            return false;
        }
        if (departureTime != null ? !departureTime.equals(that.departureTime) : that.departureTime != null) {
            return false;
        }
        if (arrivalTime != null ? !arrivalTime.equals(that.arrivalTime) : that.arrivalTime != null) {
            return false;
        }
        if (price != null ? !price.equals(that.price) : that.price != null) {
            return false;
        }
        if (availableSeats != null ? !availableSeats.equals(that.availableSeats) : that.availableSeats != null) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (route != null ? route.hashCode() : 0);
        result = 31 * result + (bus != null ? bus.hashCode() : 0);
        result = 31 * result + (departureTime != null ? departureTime.hashCode() : 0);
        result = 31 * result + (arrivalTime != null ? arrivalTime.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (availableSeats != null ? availableSeats.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Trip.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("route=" + route)
                .add("bus=" + bus)
                .add("departureTime=" + departureTime)
                .add("arrivalTime=" + arrivalTime)
                .add("price=" + price)
                .add("availableSeats=" + availableSeats)
                .toString();
    }
}
