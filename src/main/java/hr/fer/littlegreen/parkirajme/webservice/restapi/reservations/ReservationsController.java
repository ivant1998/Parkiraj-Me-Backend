package hr.fer.littlegreen.parkirajme.webservice.restapi.reservations;

import hr.fer.littlegreen.parkirajme.webservice.data.database.DatabaseManager;
import hr.fer.littlegreen.parkirajme.webservice.domain.models.ParkingObject;
import hr.fer.littlegreen.parkirajme.webservice.domain.session.TokenManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

        if (userTokenId.equals(userId)) {
            var objects = databaseManager.getUserParkingReservations(userId);
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

    }
}
