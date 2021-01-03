package hr.fer.littlegreen.parkirajme.webservice.restapi.registeredusers;

import hr.fer.littlegreen.parkirajme.webservice.domain.models.User;
import org.springframework.lang.NonNull;

import java.util.Objects;

public class RegisteredUser extends User {

    public RegisteredUser(
        @NonNull String userUuid,
        @NonNull String email,
        @NonNull String role,
        @NonNull String oib
    ) {
        super(userUuid, email, role, oib);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        User user = (User) o;
        return userUuid.equals(user.getUserUuid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(userUuid);
    }
}

