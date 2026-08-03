package by.shved.busbooking.entity;

import java.time.LocalDateTime;
import java.util.StringJoiner;

public class Booking {
    private Integer id;
    private User user;
    private Trip trip;
    private Integer seatNumber;
    private LocalDateTime bookingDate;
    private String status;

    public Booking() {
    }

    public Booking(Integer id, User user, Trip trip, Integer seatNumber,
                   LocalDateTime bookingDate, String status) {
        this.id = id;
        this.user = user;
        this.trip = trip;
        this.seatNumber = seatNumber;
        this.bookingDate = bookingDate;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public Integer getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(Integer seatNumber) {
        this.seatNumber = seatNumber;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Booking that = (Booking) obj;
        if (id != null ? !id.equals(that.id) : that.id != null) {
            return false;
        }
        if (user != null ? !user.equals(that.user) : that.user != null) {
            return false;
        }
        if (trip != null ? !trip.equals(that.trip) : that.trip != null) {
            return false;
        }
        if (seatNumber != null ? !seatNumber.equals(that.seatNumber) : that.seatNumber != null) {
            return false;
        }
        if (bookingDate != null ? !bookingDate.equals(that.bookingDate) : that.bookingDate != null) {
            return false;
        }
        if (status != null ? !status.equals(that.status) : that.status != null) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (trip != null ? trip.hashCode() : 0);
        result = 31 * result + (seatNumber != null ? seatNumber.hashCode() : 0);
        result = 31 * result + (bookingDate != null ? bookingDate.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Booking.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("user=" + user)
                .add("trip=" + trip)
                .add("seatNumber=" + seatNumber)
                .add("bookingDate=" + bookingDate)
                .add("status='" + status + "'")
                .toString();
    }
}
