package hr.fer.littlegreen.parkirajme.webservice.dao;

import org.springframework.lang.NonNull;

import java.util.UUID;

public class Vehicle{
    @NonNull
    private final String registrationNumber;
    @NonNull
    private final UUID ownerId;

    public Vehicle(String registrationNumber, UUID ownerId) {
        this.registrationNumber = registrationNumber;
        this.ownerId = ownerId;
    }

    @NonNull
    public String getRegistrationNumber() {
        return registrationNumber;
    }

    @NonNull
    public UUID getOwnerId() {
        return ownerId;
    }
}
