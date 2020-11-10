package hr.fer.littlegreen.parkirajme.webservice.dao;

import org.springframework.lang.NonNull;

import java.util.Objects;

public final class Vehicle {

    @NonNull
    private final String registrationNumber;

    @NonNull
    private final String ownerId;

    public Vehicle(@NonNull String registrationNumber, @NonNull String ownerId) {
        this.registrationNumber = registrationNumber;
        this.ownerId = ownerId;
    }

    @NonNull
    public String getRegistrationNumber() {
        return registrationNumber;
    }

    @NonNull
    public String getOwnerId() {
        return ownerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        Vehicle vehicle = (Vehicle) o;
        return registrationNumber.equals(vehicle.registrationNumber) &&
            ownerId.equals(vehicle.ownerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(registrationNumber, ownerId);
    }
}
