package by.shved.busbooking.entity;

import java.util.StringJoiner;

public class BusRoute {
    private Integer id;
    private String routeNumber;
    private String departureCity;
    private String arrivalCity;

    public BusRoute() {
    }

    public BusRoute(Integer id, String routeNumber, String departureCity, String arrivalCity) {
        this.id = id;
        this.routeNumber = routeNumber;
        this.departureCity = departureCity;
        this.arrivalCity = arrivalCity;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRouteNumber() {
        return routeNumber;
    }

    public void setRouteNumber(String routeNumber) {
        this.routeNumber = routeNumber;
    }

    public String getDepartureCity() {
        return departureCity;
    }

    public void setDepartureCity(String departureCity) {
        this.departureCity = departureCity;
    }

    public String getArrivalCity() {
        return arrivalCity;
    }

    public void setArrivalCity(String arrivalCity) {
        this.arrivalCity = arrivalCity;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        BusRoute that = (BusRoute) obj;
        if (id != null ? !id.equals(that.id) : that.id != null) {
            return false;
        }
        if (routeNumber != null ? !routeNumber.equals(that.routeNumber) : that.routeNumber != null) {
            return false;
        }
        if (departureCity != null ? !departureCity.equals(that.departureCity) : that.departureCity != null) {
            return false;
        }
        if (arrivalCity != null ? !arrivalCity.equals(that.arrivalCity) : that.arrivalCity != null) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (routeNumber != null ? routeNumber.hashCode() : 0);
        result = 31 * result + (departureCity != null ? departureCity.hashCode() : 0);
        result = 31 * result + (arrivalCity != null ? arrivalCity.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", BusRoute.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("routeNumber='" + routeNumber + "'")
                .add("departureCity='" + departureCity + "'")
                .add("arrivalCity='" + arrivalCity + "'")
                .toString();
    }
}
