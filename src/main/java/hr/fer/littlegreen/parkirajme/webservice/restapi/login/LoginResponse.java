package hr.fer.littlegreen.parkirajme.webservice.restapi.login;

import hr.fer.littlegreen.parkirajme.webservice.domain.models.User;
import org.springframework.lang.Nullable;

import java.util.Objects;

public class LoginResponse {

    @Nullable
    private final String token;

    @Nullable
    private final User user;

    public LoginResponse(@Nullable String token, @Nullable User user) {
        this.token = token;
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        LoginResponse that = (LoginResponse) o;
        return Objects.equals(token, that.token) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, user);
    }

    @Nullable
    public String getToken() {
        return token;
    }

    @Nullable
    public User getUser() {
        return user;
    }
}
