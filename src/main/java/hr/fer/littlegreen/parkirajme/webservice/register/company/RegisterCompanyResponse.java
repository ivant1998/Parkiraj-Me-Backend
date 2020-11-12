package hr.fer.littlegreen.parkirajme.webservice.register.company;

import org.springframework.lang.Nullable;

import java.util.Objects;

public class RegisterCompanyResponse {

    @Nullable
    private final String token;

    public RegisterCompanyResponse(@Nullable String token) { this.token = token; }

    @Nullable
    public final String getToken() { return token; }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        RegisterCompanyResponse that = (RegisterCompanyResponse) o;
        return token.equals(that.token);
    }

    @Override
    public int hashCode() { return Objects.hash(token); }

}
