package hr.fer.littlegreen.parkirajme.webservice.domain.models;

import org.springframework.lang.NonNull;

import java.util.Objects;

public final class Vehicle {

    @NonNull
    private final String registrationNumber;

    public Vehicle(@NonNull String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    @NonNull
    public String getRegistrationNumber() {
        return registrationNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        Vehicle vehicle = (Vehicle) o;
        return registrationNumber.equals(vehicle.registrationNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(registrationNumber);
    }
}
