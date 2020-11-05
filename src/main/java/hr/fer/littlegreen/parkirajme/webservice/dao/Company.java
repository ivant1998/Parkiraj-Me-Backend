package hr.fer.littlegreen.parkirajme.webservice.dao;

import org.springframework.lang.NonNull;

import java.util.UUID;

public class Company extends User{
    @NonNull
    private final String address;
    @NonNull
    private final String name;

    public Company(UUID id, String email, String password, char role, String oib, String address, String name) {
        super(id, email, password, role, oib);
        this.address = address;
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    @NonNull
    public String getName() {
        return name;
    }
}
