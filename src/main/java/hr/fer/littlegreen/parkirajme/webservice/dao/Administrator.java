package hr.fer.littlegreen.parkirajme.webservice.dao;

import java.util.UUID;

public class Administrator extends User{

    public Administrator(UUID id, String email, String password, char role, String oib) {
        super(id, email, password, role, oib);
    }
}
