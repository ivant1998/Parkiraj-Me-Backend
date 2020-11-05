package hr.fer.littlegreen.parkirajme.webservice.dao;

import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;
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

    public Person(UUID id, String email, String password, char role, String oib, String firstName, String lastName, String creditCard) {
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

}
