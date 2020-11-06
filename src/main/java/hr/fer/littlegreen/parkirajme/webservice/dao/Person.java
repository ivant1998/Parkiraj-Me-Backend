package hr.fer.littlegreen.parkirajme.webservice.dao;

import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Person extends User{
    @NonNull
    private final String firstName;
    @NonNull
    private final String lastName;
    @NonNull
    private final String creditCard;
    @NonNull
    private final List<Vehicle> vehicleArray = new ArrayList<>();

    public Person(String id, String email, String password, String role, String oib, String firstName, String lastName, String creditCard) {
        super(id, email, password, role, oib);
        this.firstName = firstName;
        this.lastName = lastName;
        this.creditCard = creditCard;
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

    public void addNewVehicle(String registrationNumber){
            Vehicle vehicle = new Vehicle(registrationNumber, this.id);
            vehicleArray.add(vehicle);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        Person person = (Person) o;
        return firstName.equals(person.firstName) &&
            lastName.equals(person.lastName) &&
            creditCard.equals(person.creditCard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, creditCard);
    }
}
