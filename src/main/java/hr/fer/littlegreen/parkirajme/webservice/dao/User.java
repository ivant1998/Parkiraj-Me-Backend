package hr.fer.littlegreen.parkirajme.webservice.dao;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Objects;

public abstract class User {
    @NonNull
    protected String id;
    @NonNull
    protected String email;
    @NonNull
    protected String password;
    @Nullable
    protected String role;
    @NonNull
    protected String oib;

    public User(@NonNull String id, @NonNull String email, @NonNull String password, String role, @NonNull String oib) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
        this.oib = oib;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getOib() {
        return oib;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        User user = (User) o;
        return id.equals(user.id) &&
            email.equals(user.email) &&
            password.equals(user.password) &&
            oib.equals(user.oib);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, oib);
    }
}
