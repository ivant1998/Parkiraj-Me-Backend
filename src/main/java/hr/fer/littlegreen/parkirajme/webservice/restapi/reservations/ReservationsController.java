package hr.fer.littlegreen.parkirajme.webservice.restapi.reservations;

import hr.fer.littlegreen.parkirajme.webservice.data.database.DatabaseManager;
import hr.fer.littlegreen.parkirajme.webservice.domain.models.ParkingObject;
import hr.fer.littlegreen.parkirajme.webservice.domain.session.TokenManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

public class ReservationsController {

    @NonNull
    private final DatabaseManager databaseManager;

    @NonNull
    private final TokenManager tokenManager;

    public ReservationsController(
        @NonNull DatabaseManager databaseManager,
        @NonNull TokenManager tokenManager
    ) {
        this.databaseManager = databaseManager;
        this.tokenManager = tokenManager;
    }


    @GetMapping("/user/{userId}/reservations")
    public ResponseEntity<List<ParkingObject>> reservations(
        @PathVariable String userId,
        @RequestHeader("Authentication-Token") String token
    ) {
        var userTokenId = tokenManager.getId(token);
        if (userTokenId == null) { return new ResponseEntity<>(null, HttpStatus.FORBIDDEN); }
        String role = databaseManager.getUserRole(userId);
        if (!role.equals("p")) { return new ResponseEntity<>(null, HttpStatus.FORBIDDEN); }

        if (userTokenId.equals(userId)) {
            var objects = databaseManager.getUserParkingReservations(userId);
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

    }

    @GetMapping("/parkingObject/{objectId}/users")
    public ResponseEntity<List<ParkingObject>> reservationsOnParking(
        @PathVariable String objectId,
        @RequestHeader("Authentication-Token") String token
    ) {
        var companyTokenId = tokenManager.getId(token);
        if (companyTokenId == null) { return new ResponseEntity<>(null, HttpStatus.FORBIDDEN); }
        String role = databaseManager.getUserRole(objectId);
        if (!role.equals("p")) { return new ResponseEntity<>(null, HttpStatus.FORBIDDEN); }

        if (companyTokenId.equals(objectId)) {
            var objects = databaseManager.getReservationsOnParking(companyTokenId);
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

    }

    @PostMapping("/user/addReservation")
    public ResponseEntity<List<ParkingObject>> addReservation(
        @RequestHeader("Authentication-Token") String token
    ) {
        var userId = tokenManager.getId(token);
        if (userId != null) {
            try {
                String id = databaseManager.addReservation(userId);
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            } catch (IllegalArgumentException ex) {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
    }
}
