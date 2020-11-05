package hr.fer.littlegreen.parkirajme.webservice.dao;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.UUID;

public abstract class User {
    @NonNull
    protected UUID id;
    @NonNull
    protected String email;
    @NonNull
    protected String password;
    @Nullable
    protected char role;
    @NonNull
    protected String oib;


    public User(
        UUID id,
        String email,
        String password,
        char role,
        String oib
    ) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
        this.oib = oib;
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public char getRole() {
        return role;
    }

    public String getOib() {
        return oib;
    }
}
