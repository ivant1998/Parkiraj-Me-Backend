package hr.fer.littlegreen.parkirajme.webservice.domain.models;

import org.springframework.lang.NonNull;

import java.time.YearMonth;
import java.util.List;

public final class Person extends User {

    @NonNull
    private final String firstName;

    @NonNull
    private final String lastName;

    @NonNull
    private final String creditCardNumber;

    @NonNull
    private final YearMonth creditCardExpirationDate;

    @NonNull
    private final List<Vehicle> vehicles;

    public Person(
        @NonNull String userUuid,
        @NonNull String email,
        @NonNull String role,
        @NonNull String oib,
        @NonNull String firstName,
        @NonNull String lastName,
        @NonNull String creditCardNumber,
        @NonNull YearMonth creditCardExpirationDate,
        @NonNull List<Vehicle> vehicles
    ) {
        super(userUuid, email, role, oib);
        this.firstName = firstName;
        this.lastName = lastName;
        this.creditCardNumber = creditCardNumber;
        this.creditCardExpirationDate = creditCardExpirationDate;
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
    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    @NonNull
    public YearMonth getCreditCardExpirationDate() {
        return creditCardExpirationDate;
    }

    @NonNull
    public List<Vehicle> getVehicles() {
        return vehicles;
    }

}
