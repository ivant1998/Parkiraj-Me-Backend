package hr.fer.littlegreen.parkirajme.webservice.restapi.reservations;

import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Objects;

public class ReservationListResponse {

    @Nullable
    private final List<ReservationAndParkingObjectPair> reservations;

    public ReservationListResponse(@Nullable List<ReservationAndParkingObjectPair> reservations) {
        this.reservations = reservations;
    }

    @Nullable
    public List<ReservationAndParkingObjectPair> getReservations() {
        return reservations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        ReservationListResponse that = (ReservationListResponse) o;
        assert reservations != null;
        return reservations.equals(that.reservations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reservations);
    }
}
