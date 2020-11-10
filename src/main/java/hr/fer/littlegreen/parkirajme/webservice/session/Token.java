package hr.fer.littlegreen.parkirajme.webservice.session;

import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.util.Objects;

public class Token {
    @NonNull
    private final String token;
    @NonNull
    private final LocalDate endDate;

    public Token(@NonNull String token, @NonNull LocalDate endDate) {
        this.token = token;
        this.endDate = endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        Token token1 = (Token) o;
        return token.equals(token1.token) &&
            endDate.equals(token1.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, endDate);
    }

    @NonNull
    public String getToken() {
        return token;
    }

    @NonNull
    public LocalDate getEndDate() {
        return endDate;
    }
}
