package hr.fer.littlegreen.parkirajme.webservice.restapi.reservations;

import org.springframework.lang.NonNull;

public class ReservationResponse {

    @NonNull
    private final String reservationId;


    public ReservationResponse(@NonNull String reservationId) {this.reservationId = reservationId;}
}
