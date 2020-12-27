package hr.fer.littlegreen.parkirajme.webservice.restapi.registeredusers;

import hr.fer.littlegreen.parkirajme.webservice.domain.models.User;
import org.springframework.lang.NonNull;

import java.util.Objects;

public class RegisteredUser extends User {

    public RegisteredUser(
        @NonNull String id,
        @NonNull String email,
        @NonNull String role,
        @NonNull String oib
    ) {
        super(id,email,role,oib);
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
        return id.equals(user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

