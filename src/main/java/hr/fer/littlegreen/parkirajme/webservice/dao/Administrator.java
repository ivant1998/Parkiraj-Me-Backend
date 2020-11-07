package hr.fer.littlegreen.parkirajme.webservice.dao;

import java.util.UUID;

public class Administrator extends User{

    public Administrator(String id, String email, String password, String role, String oib) {
        super(id, email, password, role, oib);
    }
}
