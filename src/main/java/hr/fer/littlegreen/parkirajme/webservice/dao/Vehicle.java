package hr.fer.littlegreen.parkirajme.webservice.dao;

import org.springframework.lang.NonNull;

import java.util.UUID;

public class Vehicle{
    @NonNull
    private final String registrationNumber;
    @NonNull
    private final String ownerId;

    public Vehicle(String registrationNumber, String ownerId) {
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

}
