package hr.fer.littlegreen.parkirajme.webservice.domain.session;

import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.util.Objects;

public class TokenAndExpiration {

    @NonNull
    private final String token;

    @NonNull
    private final LocalDate expiration;

    public TokenAndExpiration(@NonNull String token, @NonNull LocalDate expiration) {
        this.token = token;
        this.expiration = expiration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        TokenAndExpiration tokenAndExpiration = (TokenAndExpiration) o;
        return token.equals(tokenAndExpiration.token) &&
            expiration.equals(tokenAndExpiration.expiration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, expiration);
    }

    @NonNull
    public String getToken() {
        return token;
    }

    @NonNull
    LocalDate getExpiration() {
        return expiration;
    }
}
