package by.shved.busbooking.entity;

import java.util.StringJoiner;

public class BusDriver extends AbstractEntity {
    private Integer id;
    private String lastName;
    private String firstName;
    private String patronymic;
    private Integer experienceYears;
    private String phoneNumber;
    private String status;

    public BusDriver() {
    }

    public BusDriver(Integer id, String firstName, String lastName, String patronymic,
                  Integer experienceYears, String phoneNumber, String status) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.experienceYears = experienceYears;
        this.phoneNumber = phoneNumber;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public Integer getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(Integer experienceYears) {
        this.experienceYears = experienceYears;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getShortName() {
        StringBuilder builder = new StringBuilder();
        builder.append(lastName);
        if (firstName != null && !firstName.isEmpty()) {
            builder.append(" ")
                    .append(firstName.charAt(0))
                    .append(".");
        }
        if (patronymic != null && !patronymic.isEmpty()) {
            builder.append(patronymic.charAt(0))
                    .append(".");
        }
        return builder.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        BusDriver that = (BusDriver) obj;
        if (id != null ? !id.equals(that.id) : that.id != null) {
            return false;
        }
        if (firstName != null ? !firstName.equals(that.firstName) : that.firstName != null) {
            return false;
        }
        if (lastName != null ? !lastName.equals(that.lastName) : that.lastName != null) {
            return false;
        }
        if (patronymic != null ? !patronymic.equals(that.patronymic) : that.patronymic != null) {
            return false;
        }
        if (experienceYears != null ? !experienceYears.equals(that.experienceYears)
                : that.experienceYears != null) {
            return false;
        }
        if (phoneNumber != null ? !phoneNumber.equals(that.phoneNumber) : that.phoneNumber != null) {
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
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (patronymic != null ? patronymic.hashCode() : 0);
        result = 31 * result + (experienceYears != null ? experienceYears.hashCode() : 0);
        result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", BusDriver.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("firstName='" + firstName + "'")
                .add("lastName='" + lastName + "'")
                .add("patronymic='" + patronymic + "'")
                .add("experienceYears=" + experienceYears)
                .add("phoneNumber='" + phoneNumber + "'")
                .add("status='" + status + "'")
                .toString();
    }
}
