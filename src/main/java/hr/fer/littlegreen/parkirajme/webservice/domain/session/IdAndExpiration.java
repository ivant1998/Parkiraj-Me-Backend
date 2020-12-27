package hr.fer.littlegreen.parkirajme.webservice.domain.session;

import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.util.Objects;

public class IdAndExpiration {

    @NonNull
    private final String id;

    @NonNull
    private final LocalDate expiration;

    public IdAndExpiration(@NonNull String id, @NonNull LocalDate expiration) {
        this.id = id;
        this.expiration = expiration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        IdAndExpiration idAndExpiration = (IdAndExpiration) o;
        return id.equals(idAndExpiration.id) &&
            expiration.equals(idAndExpiration.expiration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, expiration);
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    LocalDate getExpiration() {
        return expiration;
    }
}
