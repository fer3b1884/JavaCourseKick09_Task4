package by.shved.busbooking.entity;

import java.util.StringJoiner;

public class Bus extends AbstractEntity {
    private Integer id;
    private String busNumber;
    private String brand;
    private BusDriver driver;
    private Integer startOperationYear;
    private Integer mileage;
    private Integer seatCount;
    private String status;

    public Bus() {
    }

    public Bus(Integer id, String busNumber, String brand, BusDriver driver,
               Integer startOperationYear, Integer mileage, Integer seatCount, String status) {
        this.id = id;
        this.busNumber = busNumber;
        this.brand = brand;
        this.driver = driver;
        this.startOperationYear = startOperationYear;
        this.mileage = mileage;
        this.seatCount = seatCount;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public BusDriver getDriver() {
        return driver;
    }

    public void setDriver(BusDriver driver) {
        this.driver = driver;
    }

    public Integer getStartOperationYear() {
        return startOperationYear;
    }

    public void setStartOperationYear(Integer startOperationYear) {
        this.startOperationYear = startOperationYear;
    }

    public Integer getMileage() {
        return mileage;
    }

    public void setMileage(Integer mileage) {
        this.mileage = mileage;
    }

    public Integer getSeatCount() {
        return seatCount;
    }

    public void setSeatCount(Integer seatCount) {
        this.seatCount = seatCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Bus that = (Bus) obj;
        if (id != null ? !id.equals(that.id) : that.id != null) {
            return false;
        }
        if (busNumber != null ? !busNumber.equals(that.busNumber) : that.busNumber != null) {
            return false;
        }
        if (brand != null ? !brand.equals(that.brand) : that.brand != null) {
            return false;
        }
        if (driver != null ? !driver.equals(that.driver) : that.driver != null) {
            return false;
        }
        if (startOperationYear != null ? !startOperationYear.equals(that.startOperationYear)
                : that.startOperationYear != null) {
            return false;
        }
        if (mileage != null ? !mileage.equals(that.mileage) : that.mileage != null) {
            return false;
        }
        if (seatCount != null ? !seatCount.equals(that.seatCount) : that.seatCount != null) {
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
        result = 31 * result + (busNumber != null ? busNumber.hashCode() : 0);
        result = 31 * result + (brand != null ? brand.hashCode() : 0);
        result = 31 * result + (driver != null ? driver.hashCode() : 0);
        result = 31 * result + (startOperationYear != null ? startOperationYear.hashCode() : 0);
        result = 31 * result + (mileage != null ? mileage.hashCode() : 0);
        result = 31 * result + (seatCount != null ? seatCount.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Bus.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("busNumber='" + busNumber + "'")
                .add("brand='" + brand + "'")
                .add("driver=" + driver)
                .add("startOperationYear=" + startOperationYear)
                .add("mileage=" + mileage)
                .add("seatCount=" + seatCount)
                .add("status='" + status + "'")
                .toString();
    }
}
