package hr.fer.littlegreen.parkirajme.webservice.restapi.reservations;

import hr.fer.littlegreen.parkirajme.webservice.domain.models.ParkingObject;
import hr.fer.littlegreen.parkirajme.webservice.domain.models.Reservation;
import org.springframework.lang.Nullable;

import java.util.Objects;

public final class ReservationAndParkingObjectPair {

    @Nullable
    private final Reservation reservation;

    @Nullable
    private final ParkingObject parkingObject;

    public ReservationAndParkingObjectPair(
        @Nullable Reservation reservation,
        @Nullable ParkingObject parkingObject
    ) {
        this.reservation = reservation;
        this.parkingObject = parkingObject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        ReservationAndParkingObjectPair that = (ReservationAndParkingObjectPair) o;
        return Objects.equals(reservation, that.reservation) && Objects.equals(parkingObject, that.parkingObject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reservation, parkingObject);
    }

    @Nullable
    public Reservation getReservation() {
        return reservation;
    }

    @Nullable
    public ParkingObject getParkingObject() {
        return parkingObject;
    }
}
