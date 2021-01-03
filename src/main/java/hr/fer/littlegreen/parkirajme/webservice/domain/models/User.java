package hr.fer.littlegreen.parkirajme.webservice.domain.models;

import org.springframework.lang.NonNull;

import java.util.Objects;


public abstract class User {

    @NonNull
    protected String userUuid;

    @NonNull
    protected String email;

    @NonNull
    protected String role;

    @NonNull
    protected String oib;

    public User(
        @NonNull String userUuid,
        @NonNull String email,
        @NonNull String role,
        @NonNull String oib
    ) {
        this.userUuid = userUuid;
        this.email = email;
        this.role = role;
        this.oib = oib;
    }

    @NonNull
    public String getUserUuid() {
        return userUuid;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    @NonNull
    public String getRole() {
        return role;
    }

    @NonNull
    public String getOib() {
        return oib;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        User user = (User) o;
        return userUuid.equals(user.userUuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userUuid);
    }
}
