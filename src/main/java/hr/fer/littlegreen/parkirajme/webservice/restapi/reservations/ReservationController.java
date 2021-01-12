package hr.fer.littlegreen.parkirajme.webservice.restapi.reservations;

import hr.fer.littlegreen.parkirajme.webservice.data.database.DatabaseManager;
import hr.fer.littlegreen.parkirajme.webservice.domain.models.Reservation;
import hr.fer.littlegreen.parkirajme.webservice.domain.session.TokenManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
public class ReservationController {

    @NonNull
    private final DatabaseManager databaseManager;

    @NonNull
    private final TokenManager tokenManager;

    public ReservationController(
        @NonNull DatabaseManager databaseManager,
        @NonNull TokenManager tokenManager
    ) {
        this.databaseManager = databaseManager;
        this.tokenManager = tokenManager;
    }


    @GetMapping("/user/{userId}/reservations")
    public ResponseEntity<List<Reservation>> getUserReservations(
        @PathVariable String userId,
        @RequestHeader("Authentication-Token") String token
    ) {
        var userTokenId = tokenManager.getId(token);
        String role = databaseManager.getUserRole(userId);
        if (!role.equals("p") || userTokenId == null) { return new ResponseEntity<>(null, HttpStatus.FORBIDDEN); }


        if (userTokenId.equals(userId)) {
            var objects = databaseManager.getUserParkingReservations(userId);
            return new ResponseEntity<>(Objects.requireNonNullElseGet(objects, List::of), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

    }

    @GetMapping("/parkingObject/{objectId}/users")
    public ResponseEntity<List<Reservation>> reservationsOnParking(
        @PathVariable String objectId,
        @RequestHeader("Authentication-Token") String token
    ) {
        var companyTokenId = tokenManager.getId(token);
        String role = databaseManager.getUserRole(objectId);
        if (!role.equals("c") || companyTokenId == null) { return new ResponseEntity<>(null, HttpStatus.FORBIDDEN); }

        if (companyTokenId.equals(objectId)) {
            var objects = databaseManager.getReservationsOnParking(companyTokenId);
            return new ResponseEntity<>(Objects.requireNonNullElseGet(objects, List::of), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

    }

    @PostMapping("/user/addReservation")
    public ResponseEntity<HttpStatus> addReservation(
        @RequestBody ReservationRequestBody reservation,
        @RequestHeader("Authentication-Token") String token
    ) {
        var userId = tokenManager.getId(token);
        if (userId != null) {
            try {
                boolean id = databaseManager.addReservation(reservation, userId);
                if (id) { return new ResponseEntity<>(null, HttpStatus.OK); }
            } catch (IllegalArgumentException ex) {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
    }

    @DeleteMapping("/reservation/{reservationId}")
    public ResponseEntity<HttpStatus> deleteReservation(
        @PathVariable String reservationId,
        @RequestBody ReservationDeleteRequestBody reservation,
        @RequestHeader("Authentication-token") String token
    ) {
        var userId = tokenManager.getId(token);
        String role = databaseManager.getUserRole(userId);

        if (userId != null && role != null) {
            if (role.equals("p")) {
                databaseManager.deleteReservation(reservation, userId);
                return new ResponseEntity<>(HttpStatus.ACCEPTED);
            }
        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
