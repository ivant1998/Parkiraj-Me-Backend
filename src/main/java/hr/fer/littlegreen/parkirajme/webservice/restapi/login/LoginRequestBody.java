package hr.fer.littlegreen.parkirajme.webservice.restapi.login;

import org.springframework.lang.NonNull;

import java.util.Objects;

public class LoginRequestBody {

    @NonNull
    private final String email;

    @NonNull
    private final String password;

    public LoginRequestBody(@NonNull String email, @NonNull String password) {
        this.email = email;
        this.password = password;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    @NonNull
    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        LoginRequestBody that = (LoginRequestBody) o;
        return email.equals(that.email) &&
            password.equals(that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, password);
    }
}
