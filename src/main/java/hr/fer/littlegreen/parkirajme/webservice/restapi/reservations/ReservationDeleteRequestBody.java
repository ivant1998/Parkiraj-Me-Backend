package hr.fer.littlegreen.parkirajme.webservice.restapi.reservations;

import org.springframework.lang.NonNull;

import java.util.Objects;

public class ReservationDeleteRequestBody {

    @NonNull
    private final String registrationNumber;

    @NonNull
    private final String parkingId;

    public ReservationDeleteRequestBody(@NonNull String registrationNumber, @NonNull String parkingId) {
        this.registrationNumber = registrationNumber;
        this.parkingId = parkingId;
    }

    @NonNull
    public String getRegistrationNumber() {
        return registrationNumber;
    }

    @NonNull
    public String getParkingId() {
        return parkingId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        ReservationDeleteRequestBody that = (ReservationDeleteRequestBody) o;
        return registrationNumber.equals(that.registrationNumber) &&
            parkingId.equals(that.parkingId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(registrationNumber, parkingId);
    }
}
