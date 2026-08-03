package by.shved.busbooking.entity;

import java.util.StringJoiner;

public class User  extends AbstractEntity {
    private Integer id;
    private String login;
    private String passwordHash;
    private String email;
    private String lastName;
    private String firstName;
    private String patronymic;
    private UserRole role;

    public User() {
    }

    public User(Integer id, String login, String passwordHash, String email, String lastName,
                String firstName, String patronymic, UserRole role) {
        this.id = id;
        this.login = login;
        this.passwordHash = passwordHash;
        this.email = email;
        this.lastName = lastName;
        this.firstName = firstName;
        this.patronymic = patronymic;
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        User that = (User) obj;
        if (id != null ? !id.equals(that.id) : that.id != null) {
            return false;
        }
        if (login != null ? !login.equals(that.login) : that.login != null) {
            return false;
        }
        if (passwordHash != null ? !passwordHash.equals(that.passwordHash) : that.passwordHash != null) {
            return false;
        }
        if (email != null ? !email.equals(that.email) : that.email != null) {
            return false;
        }
        if (lastName != null ? !lastName.equals(that.lastName) : that.lastName != null) {
            return false;
        }
        if (firstName != null ? !firstName.equals(that.firstName) : that.firstName != null) {
            return false;
        }
        if (patronymic != null ? !patronymic.equals(that.patronymic) : that.patronymic != null) {
            return false;
        }
        if (role != null ? !role.equals(that.role) : that.role != null) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (login != null ? login.hashCode() : 0);
        result = 31 * result + (passwordHash != null ? passwordHash.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (patronymic != null ? patronymic.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", User.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("login='" + login + "'")
                .add("passwordHash='" + passwordHash + "'")
                .add("email='" + email + "'")
                .add("lastName='" + lastName + "'")
                .add("firstName='" + firstName + "'")
                .add("patronymic='" + patronymic + "'")
                .add("role=" + role)
                .toString();
    }
}
