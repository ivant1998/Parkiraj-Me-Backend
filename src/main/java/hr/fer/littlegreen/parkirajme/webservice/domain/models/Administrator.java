package hr.fer.littlegreen.parkirajme.webservice.domain.models;

public final class Administrator extends User {

    public Administrator(String id, String email, String password, String role, String oib) {
        super(id, email, password, role, oib);
    }
}
