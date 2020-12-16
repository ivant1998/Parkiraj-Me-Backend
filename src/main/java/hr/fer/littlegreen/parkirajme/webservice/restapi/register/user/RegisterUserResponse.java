package hr.fer.littlegreen.parkirajme.webservice.restapi.register.user;

import org.springframework.lang.Nullable;

import java.util.Objects;

public class RegisterUserResponse {

    @Nullable
    private final String token;

    public RegisterUserResponse(@Nullable String token) {
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
        RegisterUserResponse that = (RegisterUserResponse) o;
        return token.equals(that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token);
    }
}
