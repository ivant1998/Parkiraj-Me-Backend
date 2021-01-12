package hr.fer.littlegreen.parkirajme.webservice.restapi.reservations;

import org.springframework.lang.NonNull;

import java.util.Objects;

public class ReservationResponse {

    @NonNull
    private final String reservationId;

    public ReservationResponse(String id) {
        this.reservationId = id;
    }

    @NonNull
    public String getReservationId() {
        return reservationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        ReservationResponse that = (ReservationResponse) o;
        return reservationId.equals(that.reservationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reservationId);
    }
}
