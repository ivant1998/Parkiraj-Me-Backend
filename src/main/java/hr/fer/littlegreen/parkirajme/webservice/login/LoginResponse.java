package hr.fer.littlegreen.parkirajme.webservice.login;

import org.springframework.lang.Nullable;

import java.util.Objects;

public class LoginResponse {

    @Nullable
    private final String token;

    public LoginResponse(@Nullable String token) {
        this.token = token;
    }

    @Nullable
    public String getToken() {
        return token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        LoginResponse that = (LoginResponse) o;
        return token.equals(that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token);
    }
}
