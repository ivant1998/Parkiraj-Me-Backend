package hr.fer.littlegreen.parkirajme.webservice.dao;

import org.springframework.lang.NonNull;

import java.util.Objects;

public class Company extends User{
    @NonNull
    private final String address;
    @NonNull
    private final String name;

    public Company(String id, String email, String password, String role, String oib, String address, String name) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        Company company = (Company) o;
        return address.equals(company.address) &&
            name.equals(company.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, name);
    }
}
