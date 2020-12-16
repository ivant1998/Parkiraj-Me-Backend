package hr.fer.littlegreen.parkirajme.webservice.domain.models;

import org.springframework.lang.NonNull;

import java.util.List;

public final class Person extends User {

    @NonNull
    private final String firstName;

    @NonNull
    private final String lastName;

    @NonNull
    private final String creditCard;

    @NonNull
    private final List<Vehicle> vehicles;

    public Person(
        @NonNull String id,
        @NonNull String email,
        @NonNull String password,
        @NonNull String role,
        @NonNull String oib,
        @NonNull String firstName,
        @NonNull String lastName,
        @NonNull String creditCard,
        @NonNull List<Vehicle> vehicles
    ) {
        super(id, email, password, role, oib);
        this.firstName = firstName;
        this.lastName = lastName;
        this.creditCard = creditCard;
        this.vehicles = vehicles;
    }

    @NonNull
    public String getFirstName() {
        return firstName;
    }

    @NonNull
    public String getLastName() {
        return lastName;
    }

    @NonNull
    public String getCreditCard() {
        return creditCard;
    }

    @NonNull
    public List<Vehicle> getVehicles() {
        return vehicles;
    }
}
