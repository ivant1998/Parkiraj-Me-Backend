package hr.fer.littlegreen.parkirajme.webservice.domain.models;

import org.springframework.lang.NonNull;

import java.util.Objects;

public abstract class User {

    @NonNull
    protected String id;

    @NonNull
    protected String email;

    @NonNull
    protected String password;

    @NonNull
    protected String role;

    @NonNull
    protected String oib;

    public User(
        @NonNull String id,
        @NonNull String email,
        @NonNull String password,
        @NonNull String role,
        @NonNull String oib
    ) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
        this.oib = oib;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    @NonNull
    public String getPassword() {
        return password;
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
        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
