package hr.fer.littlegreen.parkirajme.webservice.restapi.reservations;

import hr.fer.littlegreen.parkirajme.webservice.domain.models.Reservation;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Objects;

public class ReservationListResponse {

    @NonNull
    List<Reservation> reservations;

    public ReservationListResponse(
        @NonNull List<Reservation> reservations
    ) {
        this.reservations = reservations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        ReservationListResponse that = (ReservationListResponse) o;
        return reservations.equals(that.reservations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reservations);
    }
}
