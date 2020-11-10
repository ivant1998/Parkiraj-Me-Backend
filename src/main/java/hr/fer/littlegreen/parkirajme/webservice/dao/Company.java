package hr.fer.littlegreen.parkirajme.webservice.dao;

import org.springframework.lang.NonNull;

public final class Company extends User {

    @NonNull
    private final String address;

    @NonNull
    private final String name;

    public Company(
        @NonNull String id,
        @NonNull String email,
        @NonNull String password,
        @NonNull String role,
        @NonNull String oib,
        @NonNull String address,
        @NonNull String name
    ) {
        super(id, email, password, role, oib);
        this.address = address;
        this.name = name;
    }

    @NonNull
    public String getAddress() {
        return address;
    }

    @NonNull
    public String getName() {
        return name;
    }
}
