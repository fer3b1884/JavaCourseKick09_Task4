package by.shved.busbooking.entity;

public enum UserRoleType {
    ADMIN(1),
    USER(2);
    private final int id;

    UserRoleType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static UserRoleType fromId(int id) {
        for (UserRoleType role : values()) {
            if (role.id == id) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown role id: " + id);
    }
}
