package hr.fer.littlegreen.parkirajme.webservice.domain.models;

public final class Administrator extends User {

    public Administrator(String userUuid, String email, String role, String oib) {
        super(userUuid, email, role, oib);
    }
}
